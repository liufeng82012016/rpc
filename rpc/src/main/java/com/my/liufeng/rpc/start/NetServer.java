package com.my.liufeng.rpc.start;

import com.my.liufeng.rpc.handler.SimpleServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Author liufeng
 * @Description: 管理服务器长链接
 * @since 2021/5/27 19:49
 */
public class NetServer {

    private int workers;
    private int bosses;
    private int port;

    public NetServer(int port) {
        this.port = port;
        try {
            init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void init() throws Exception {
        if (workers == 0) {
            workers = Runtime.getRuntime().availableProcessors();
        }
        if (bosses == 0) {
            bosses = Runtime.getRuntime().availableProcessors() / 2;
        }
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(bosses);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(workers);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // todo
                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new SimpleServerHandler());
                        }
                    });
            ChannelFuture bindFuture = serverBootstrap.bind(port).sync();
            System.out.println("open server " + port);
            bindFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
        System.out.println(" server init end");
    }


}
