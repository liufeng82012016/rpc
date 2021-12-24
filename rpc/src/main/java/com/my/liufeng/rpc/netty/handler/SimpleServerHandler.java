package com.my.liufeng.rpc.netty.handler;

import com.my.liufeng.rpc.context.ProviderRepository;
import com.my.liufeng.rpc.exception.InnerException;
import com.my.liufeng.rpc.model.RpcRequest;
import com.my.liufeng.rpc.model.RpcResponse;
import com.my.liufeng.rpc.utils.SerialUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;

/**
 * 服务端handler，解析请求，处理并返回
 */
public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server receive msg: " + msg);
        RpcRequest rpcRequest;
        if (msg instanceof RpcRequest) {
            rpcRequest = (RpcRequest) msg;
        } else {
            rpcRequest = SerialUtil.deserialize(String.valueOf(msg), RpcRequest.class);
        }
        // 初始化相应值
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        try {
            // 通过请求参数获取对应的实例
            Class<?> clazz = Class.forName(rpcRequest.getServiceClass());
            Object o = ProviderRepository.get(clazz);
            if (o == null) {
                throw new InnerException("instance" + rpcRequest.getServiceClass() + " not found");
            }
            Method method = clazz.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
            // todo 这里可以同步可以异步
            Object data = method.invoke(o, rpcRequest.getParams());
            rpcResponse.setData(data);
        } catch (ClassNotFoundException e) {
            rpcResponse.setCode(1);
            rpcResponse.setData("class " + rpcRequest.getServiceClass() + " not found");
        } catch (Exception e) {
            e.printStackTrace();
            rpcResponse.setMsg(e.getMessage());
            rpcResponse.setCode(1);
        } finally {
            // todo 这里是异步发送，如果没有对结果进行处理，可能会丢失
            ctx.channel().writeAndFlush(rpcResponse);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel connect");
    }

}
