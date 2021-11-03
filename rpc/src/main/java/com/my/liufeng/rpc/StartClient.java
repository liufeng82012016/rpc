package com.my.liufeng.rpc;

import com.my.liufeng.rpc.context.MethodProxyRepository;
import com.my.liufeng.rpc.mock.MockResp;
import com.my.liufeng.rpc.mock.RemoteHelloService;
import com.my.liufeng.rpc.serial.ProtostuffSerializer;
import com.my.liufeng.rpc.utils.CollectionUtil;
import com.my.liufeng.rpc.utils.SerialUtil;

public class StartClient {
    public static void start(String[] packages) {
        if (CollectionUtil.isEmpty(packages)) {
            // 默认扫描本包
            packages = new String[]{StartClient.class.getPackage().getName()};
        }
        MethodProxyRepository.scan(packages);
        System.out.println(MethodProxyRepository.getInstances());
    }

    public static void main(String[] args) {
        SerialUtil.setMsgSerializer(new ProtostuffSerializer());

        start(null);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RemoteHelloService remoteHelloService = MethodProxyRepository.getProxy(RemoteHelloService.class);
        System.out.println("get remoteHelloService");
        MockResp result = remoteHelloService.obj();
        System.out.println("result: " + result + ",time is " + System.currentTimeMillis());
    }
}
