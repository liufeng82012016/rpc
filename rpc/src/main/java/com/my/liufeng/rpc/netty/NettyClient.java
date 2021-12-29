package com.my.liufeng.rpc.netty;

import com.my.liufeng.rpc.exception.InnerException;
import com.my.liufeng.rpc.model.RpcRequest;
import com.my.liufeng.rpc.netty.codec.CustomDecoder;
import com.my.liufeng.rpc.netty.codec.CustomEncoder;
import com.my.liufeng.rpc.netty.handler.SimpleClientHandler;
import com.my.liufeng.rpc.utils.IpUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.concurrent.CompletableFuture;


/**
 * @author liufeng
 * @since 2021/5/27 19:48
 */
public class NettyClient {
    private static InternalLogger logger = InternalLoggerFactory.getInstance(NettyClient.class);

    /**
     * 服务器ip
     */
    private String serverHost;
    /**
     * 服务器端口
     */
    private Integer serverPort;
    /**
     * 连接
     */
    private Channel channel;

    private static ChannelInitializer<SocketChannel> channelInitializer;

    public NettyClient() {
        // 不建立连接，等待请求打过来，走注册中心获取端口和ip
    }

    public NettyClient(String serverAddress) {
        String[] strings = IpUtil.splitAddress(serverAddress);
        this.serverHost = strings[0];
        this.serverPort = Integer.parseInt(strings[1]);
    }


    public NettyClient(Integer serverPort, String serverHost) {
        this.serverPort = serverPort;
        this.serverHost = serverHost;
        // 建立服务端连接
    }

    /**
     * 连接服务器
     */
    public void connect() {
        if (serverHost == null || serverPort == null) {
            throw new RuntimeException(String.format("connect fail. parameter %s:%s", serverHost, serverPort));
        }
        if (channelInitializer == null) {
            channelInitializer = new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new CustomDecoder());
                    ch.pipeline().addLast(new CustomEncoder());
                    ch.pipeline().addLast(new SimpleClientHandler());
                }
            };
        }
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(channelInitializer);
        try {
            ChannelFuture connectFuture = bootstrap.connect(serverHost, serverPort).sync();
            connectFuture.addListener((ChannelFutureListener) channelFuture -> {
                if (channelFuture.isSuccess()) {
                    channel = connectFuture.channel();
                    logger.info("connect success.host: {},port:{}");
                } else {
                    throw new InnerException(serverHost + ":" + serverPort + " connect failed");
                }
            });
            // 关闭之后无法发送接收消息
//            channel.closeFuture().sync().addListener(future -> {
//                if (future.isSuccess()) {
//                    System.out.println("channel close success");
//                } else {
//                    System.out.println("channel close fail");
//                }
//            });
        } catch (InterruptedException e) {
            throw new RuntimeException(String.format("connect interrupted. parameter %s:%s", serverHost, serverPort));
        }
    }


    /**
     * 是否不可用
     */
    public boolean isInvaliable() {
        return false;
    }

    public CompletableFuture<?> sendMsg(RpcRequest request) {
        int retryTimes = 0;
        while (channel == null || !channel.isActive()) {
            // todo 状态判定用哪个？
            connect();
            retryTimes++;
            try {
                retryTimes++;
                Thread.sleep(10);
            } catch (InterruptedException e) {
                //
            }
            if (retryTimes > 3) {
                break;
            }
        }
        // 序列化不需要自己做，netty有附带
        ChannelFuture channelFuture = channel.writeAndFlush(request);
        CompletableFuture<?> responseFuture = new CompletableFuture<>();

        channelFuture.addListener((ChannelFutureListener) future -> {
            if (channelFuture.isSuccess()) {
                logger.info("Write successful. mills: {} ", System.currentTimeMillis());
                AttributeKey<Object> key = AttributeKey.newInstance(request.getRequestId());
                channel.attr(key).set(responseFuture);
            } else {
                Throwable cause = channelFuture.cause();
                if (cause != null) {
                    cause.printStackTrace();
                }
                logger.warn("Error writing message to host:{} port:{}", serverHost, serverPort);
                responseFuture.completeExceptionally(future.cause());
            }
        });
        return responseFuture;
    }

    public static void setChannelInitializer(ChannelInitializer<SocketChannel> channelInitializer) {
        NettyClient.channelInitializer = channelInitializer;
    }
}
