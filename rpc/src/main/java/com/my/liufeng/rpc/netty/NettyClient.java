package com.my.liufeng.rpc.netty;

import com.my.liufeng.rpc.exception.InnerException;
import com.my.liufeng.rpc.netty.codec.CustomDecoder;
import com.my.liufeng.rpc.netty.codec.CustomEncoder;
import com.my.liufeng.rpc.netty.handler.SimpleClientHandler;
import com.my.liufeng.rpc.model.RpcRequest;
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

import java.util.concurrent.CompletableFuture;


/**
 * @author liufeng
 * @since 2021/5/27 19:48
 */
public class NettyClient {

    private String serverHost;
    private Integer serverPort;
    private Channel channel;

    public NettyClient() {
        // 不建立连接，等待请求打过来，走注册中心获取端口和ip
    }

    public NettyClient(String serverAddress) {
        String[] strings = IpUtil.splitAddress(serverAddress);
        this.serverHost = strings[0];
        this.serverPort = Integer.parseInt(strings[1]);
        connect();
    }


    public NettyClient(Integer serverPort, String serverHost) {
        this.serverPort = serverPort;
        this.serverHost = serverHost;
        // 建立服务端连接
        connect();
    }


    private void connect() {
        if (serverHost == null || serverPort == null) {
            throw new RuntimeException(String.format("connect fail. parameter %s:%s", serverHost, serverPort));
        }
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new CustomDecoder());
                        ch.pipeline().addLast(new CustomEncoder());
                        ch.pipeline().addLast(new SimpleClientHandler());
                    }
                });
        try {
            ChannelFuture connectFuture = bootstrap.connect(serverHost, serverPort).sync();
            connectFuture.addListener((ChannelFutureListener) channelFuture -> {
                if (!channelFuture.isSuccess()) {
                    throw new InnerException(serverHost + ":" + serverPort + " connect failed");
                }
            });
            channel = connectFuture.channel();
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
        if (!channel.isActive()) {
            // todo 状态判定用哪个？
            connect();
        }
        // 序列化不需要自己做，netty有附带
        ChannelFuture channelFuture = channel.writeAndFlush(request);
        CompletableFuture<?> responseFuture = new CompletableFuture<>();
        System.out.println("Write begin. mills:  " + System.currentTimeMillis());

        channelFuture.addListener((ChannelFutureListener) future -> {
            if (channelFuture.isSuccess()) {
                System.out.println("Write successful. mills:  " + System.currentTimeMillis());
                AttributeKey<Object> key = AttributeKey.newInstance(request.getRequestId());
                channel.attr(key).set(responseFuture);
            } else {
                Throwable cause = channelFuture.cause();
                if (cause != null) {
                    cause.printStackTrace();
                }
                System.out.println("Error writing message to Abaca host");
                responseFuture.completeExceptionally(future.cause());
            }
        });
        return responseFuture;
    }
}
