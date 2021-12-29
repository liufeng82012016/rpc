package com.my.liufeng.rpc.context;

import com.my.liufeng.rpc.annotation.MethodStub;
import com.my.liufeng.rpc.exception.OuterException;
import com.my.liufeng.rpc.model.RpcRequest;
import com.my.liufeng.rpc.model.RpcResponse;
import com.my.liufeng.rpc.netty.NettyClient;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author liufeng
 * @Description: 管理socket连接
 * @since 2021/8/4 19:19
 */
public class SocketContainer {
    private static InternalLogger logger = InternalLoggerFactory.getInstance(SocketContainer.class);


    private static final Map<String, NettyClient> clientMap = new ConcurrentHashMap<>();

    public static <T> T sendMsg(RpcRequest rpcRequest, MethodStub methodStub) {
        // 处理methodStub 负载均衡。。AbstractServiceMatcher
        // todo -- 直接取配置的第一个
        String[] serverAddresses = methodStub.serverAddresses();
        // 获取服务器地址
        String address = serverAddresses[0].intern();
        NettyClient netClient = clientMap.get(address);
        if (netClient == null) {
            synchronized (address) {
                if (netClient == null || netClient.isInvaliable()) {
                    netClient = new NettyClient(address);
                    clientMap.put(address, netClient);
                }
            }
        }
        // 发送请求
        // todo 一切统计相关的内容在这儿做
        CompletableFuture future = netClient.sendMsg(rpcRequest);
        try {
            // todo 返回值处理/code判断
            Object obj = future.get(methodStub.timeout(), TimeUnit.MILLISECONDS);
            if (obj instanceof RpcResponse) {
                RpcResponse response = (RpcResponse) obj;
                if (response.getCode() != 0) {
                    throw new OuterException(response.getMsg());
                }
                return (T) response.getData();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            logger.info("Time out. mills: {} ", System.currentTimeMillis());
            e.printStackTrace();
        }
        return null;
    }

}
