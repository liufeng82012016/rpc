package com.my.liufeng.rpc.handler;

import com.my.liufeng.rpc.model.RpcResponse;
import com.my.liufeng.rpc.utils.SerialUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

/**
 * 客户端handler，负责反序列化服务器返回值
 */
public class SimpleClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("client receive msg" + msg);
        // todo 更新反序列化方法
        RpcResponse response = SerialUtil.deserialize(String.valueOf(msg), RpcResponse.class);
        ctx.channel().attr(AttributeKey.valueOf(response.getRequestId())).set(response.getData());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("connect to server");
    }
}
