package com.my.liufeng.rpc.start;

import com.my.liufeng.rpc.handler.SimpleClientHandler;
import com.my.liufeng.rpc.model.RpcRequest;
import com.my.liufeng.rpc.utils.IpUtil;
import com.my.liufeng.rpc.utils.SerialUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;

import java.util.Scanner;


/**
 * @Author liufeng
 * @Description: todo
 * @since 2021/5/27 19:48
 */
public class NetClient {

    private String serverHost;
    private Integer serverPort;
    private Channel channel;

    public NetClient() {
        // 不建立连接，等待请求打过来，走注册中心获取端口和ip
    }

    public NetClient(String serverAddress) {
        String[] strings = IpUtil.splitAddress(serverAddress);
        this.serverHost = strings[0];
        this.serverPort = Integer.parseInt(strings[1]);
        connect();
    }


    public NetClient(Integer serverPort, String serverHost) {
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
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new SimpleClientHandler());
                    }
                });
        try {
            ChannelFuture connectFuture = bootstrap.connect(serverHost, serverPort).sync();
            channel = connectFuture.channel();
//            connectFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(String.format("connect interrupted. parameter %s:%s", serverHost, serverPort));
        }
    }

    public static void main(String[] args) {
        NetClient b = new NetClient();
        b.serverHost = "localhost";
        b.serverPort = 8080;
        b.connect();
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNext()) {
                String next = scanner.next();
                b.channel.writeAndFlush(next);
                System.out.println("send msg" + next);
            }
        }
    }

    /**
     * 是否不可用
     */
    public boolean isInvaliable() {
        return false;
    }

    public <T> T sendMsg(RpcRequest request, Class<T> returnType) {
        // 序列化不需要自己做，netty有附带
        ChannelFuture channelFuture = channel.writeAndFlush(SerialUtil.serialize(request));
        AttributeKey<Object> key = AttributeKey.newInstance(request.getRequestId());
        while (channel.attr(key).get() == null) {
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                return null;
            }
        }
        // todo 利用future实现定时等待，@MethodStub可以加上超时时间参数，加上序列化参数
        return (T) channel.attr(key).getAndSet(null);
    }
}
