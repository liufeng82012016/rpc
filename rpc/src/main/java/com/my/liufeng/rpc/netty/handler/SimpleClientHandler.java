package com.my.liufeng.rpc.netty.handler;

import com.my.liufeng.rpc.model.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * 客户端handler，负责反序列化服务器返回值
 */
public class SimpleClientHandler extends ChannelInboundHandlerAdapter {
    private static InternalLogger logger = InternalLoggerFactory.getInstance(SimpleClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // todo 更新反序列化方法
        if (msg instanceof RpcResponse) {
            RpcResponse response = (RpcResponse) msg;
            CompletableFuture future = (CompletableFuture) ctx.channel().attr(AttributeKey.valueOf(response.getRequestId())).getAndSet(null);
            future.complete(response);
        } else {
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("connect to server:{}", ctx.channel().remoteAddress());
    }
}
