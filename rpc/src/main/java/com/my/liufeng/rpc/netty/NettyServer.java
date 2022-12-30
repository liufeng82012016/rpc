package com.my.liufeng.rpc.netty;

import com.my.liufeng.rpc.context.IdleHandlerFactory;
import com.my.liufeng.rpc.netty.codec.CustomDecoder;
import com.my.liufeng.rpc.netty.codec.CustomEncoder;
import com.my.liufeng.rpc.netty.handler.SimpleServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * @Author liufeng
 * @Description: 管理服务器长链接
 * @since 2021/5/27 19:49
 */
public class NettyServer {
    private static InternalLogger logger = InternalLoggerFactory.getInstance(NettyServer.class);

    /**
     * 处理业务逻辑的线程数
     */
    private int workers;
    /**
     * 处理socket连接的线程数
     */
    private int bosses;
    /**
     * 绑定端口
     */
    private int port;

    private ChannelInitializer<SocketChannel> channelInitializer;


    public NettyServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        if (channelInitializer == null) {
            channelInitializer = new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // todo
                    ch.pipeline().addLast(IdleHandlerFactory.getIdleHandler());
                    ch.pipeline().addLast(new CustomDecoder());
                    ch.pipeline().addLast(new CustomEncoder());
                    ch.pipeline().addLast(new SimpleServerHandler());
                }
            };
        }
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
                    .childHandler(channelInitializer);
            ChannelFuture bindFuture = serverBootstrap.bind(port).sync();
            logger.info("open server ,port :{} ", port);
            bindFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
        logger.info(" server init end,port:{}", port);
    }

    public static InternalLogger getLogger() {
        return logger;
    }

    public static void setLogger(InternalLogger logger) {
        NettyServer.logger = logger;
    }

    public int getWorkers() {
        return workers;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

    public int getBosses() {
        return bosses;
    }

    public void setBosses(int bosses) {
        this.bosses = bosses;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ChannelInitializer<SocketChannel> getChannelInitializer() {
        return channelInitializer;
    }

    public void setChannelInitializer(ChannelInitializer<SocketChannel> channelInitializer) {
        this.channelInitializer = channelInitializer;
    }
}
