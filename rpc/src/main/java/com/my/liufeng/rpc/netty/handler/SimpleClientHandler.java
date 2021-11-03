package com.my.liufeng.rpc.netty.handler;

import com.my.liufeng.rpc.model.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

import java.util.concurrent.CompletableFuture;

/**
 * 客户端handler，负责反序列化服务器返回值
 */
public class SimpleClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("client receive msg" + msg);
        // todo 更新反序列化方法
        if (msg instanceof RpcResponse) {
            RpcResponse response = (RpcResponse) msg;
            CompletableFuture future = (CompletableFuture) ctx.channel().attr(AttributeKey.valueOf(response.getRequestId())).getAndSet(null);
            future.complete(response);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "connect to server");
    }
}
