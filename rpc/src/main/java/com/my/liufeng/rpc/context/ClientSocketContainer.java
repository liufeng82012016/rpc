package com.my.liufeng.rpc.context;

import com.my.liufeng.rpc.annotation.MethodStub;
import com.my.liufeng.rpc.start.NetClient;
import com.my.liufeng.rpc.model.RpcRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author liufeng
 * @Description: 管理socket连接
 * @since 2021/8/4 19:19
 */
public class ClientSocketContainer {
    private static Map<String, NetClient> clientMap = new ConcurrentHashMap<>();

    public static <T> T sendMsg(RpcRequest rpcRequest, MethodStub methodStub, Class<T> returnType) {
        // 处理methodStub 负载均衡。。AbstractServiceMatcher
        // todo 模拟
        String[] serverAddresses = methodStub.serverAddresses();
        // 获取服务器地址
        String address = serverAddresses[0].intern();
        NetClient netClient = clientMap.get(address);
        if (netClient == null) {
            synchronized (address) {
                if (netClient == null || netClient.isInvaliable()) {
                    netClient = new NetClient(address);
                    clientMap.put(address, netClient);
                }
            }
        }
        // 发送请求
        return netClient.sendMsg(rpcRequest, returnType);
    }
}
