package com.my.liufeng.rpc;

import com.my.liufeng.rpc.context.MethodProxyRepository;
import com.my.liufeng.rpc.context.ProviderRepository;
import com.my.liufeng.rpc.serial.ProtostuffSerializer;
import com.my.liufeng.rpc.netty.NettyServer;
import com.my.liufeng.rpc.utils.CollectionUtil;
import com.my.liufeng.rpc.utils.SerialUtil;

public class StartServer {
    public static void start(String[] packages, int port) {
        if (CollectionUtil.isEmpty(packages)) {
            // 默认扫描本包
            packages = new String[]{StartServer.class.getPackage().getName()};
        }
        ProviderRepository.scan(packages);
        MethodProxyRepository.scan(packages);
        new NettyServer(port);
    }

    public static void main(String[] args) {
        SerialUtil.setMsgSerializer(new ProtostuffSerializer());
        start(null, 9080);
    }
}
