package com.my.liufeng.rpc.netty.handler;

import com.my.liufeng.rpc.model.RpcHeartMsg;
import com.my.liufeng.rpc.utils.SerialUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * https://juejin.cn/post/6844904150090645512
 */
public class Pinger extends ChannelInboundHandlerAdapter {
    private static InternalLogger logger = InternalLoggerFactory.getInstance(Pinger.class);

    private Random random = new Random();
    private int baseRandom = 8;
    private Channel channel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.channel = ctx.channel();
        ping(this.channel);
    }

    private void ping(Channel channel) {
        int second = Math.max(1, random.nextInt(baseRandom));

        ScheduledFuture<?> future = channel.eventLoop().schedule((Runnable) () -> {
            if (channel.isActive()) {
                channel.writeAndFlush(SerialUtil.serialize(RpcHeartMsg.PING));
                logger.info("client 发送心跳包");
            } else {
                channel.closeFuture();
                logger.warn("channel closed");
            }
        }, second, TimeUnit.SECONDS);
        future.addListener((GenericFutureListener) future1 -> {
            if (future.isSuccess()) {
                ping(channel);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        channel.close();
        logger.info("channel close.cause :", cause);
    }
}
