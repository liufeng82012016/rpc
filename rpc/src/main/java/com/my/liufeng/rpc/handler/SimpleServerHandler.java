package com.my.liufeng.rpc.handler;

import com.my.liufeng.rpc.context.ServiceRegistry;
import com.my.liufeng.rpc.exception.DispatcherException;
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
        RpcRequest rpcRequest = SerialUtil.deserialize(String.valueOf(msg), RpcRequest.class);
        // 初始化相应值
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        try {
            // 通过请求参数获取对应的实例
            Class<?> clazz = Class.forName(rpcRequest.getServiceClass());
            Object o = ServiceRegistry.get(clazz);
            if (o == null) {
                throw new DispatcherException("instance" + rpcRequest.getServiceClass() + " not found");
            }
            Method method = clazz.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
            // todo 这里可以同步可以异步
            Object data = method.invoke(o, rpcRequest.getParams());
            rpcResponse.setData(data);
        } catch (ClassNotFoundException e) {
            rpcResponse.setData("class " + rpcRequest.getServiceClass() + " not found");
        } catch (DispatcherException e) {
            rpcResponse.setCode(e.getCode());
            rpcResponse.setMsg(e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            rpcResponse.setMsg(e.getMessage());
            rpcResponse.setCode(1);
        } finally {
            ctx.channel().writeAndFlush(SerialUtil.serialize(rpcResponse));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel connect");
    }

}
