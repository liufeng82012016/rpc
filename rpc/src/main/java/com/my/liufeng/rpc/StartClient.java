package com.my.liufeng.rpc;

import com.my.liufeng.rpc.context.MethodRegistry;
import com.my.liufeng.rpc.mock.RemoteHelloService;
import com.my.liufeng.rpc.utils.CollectionUtil;

public class StartClient {
    public static void start(String[] packages) {
        if (CollectionUtil.isEmpty(packages)) {
            packages = new String[]{StartClient.class.getPackage().getName()};
        }
        MethodRegistry.scan(packages);
        System.out.println(MethodRegistry.getInstances());
    }

    public static void main(String[] args) {
        start(null);
        RemoteHelloService remoteHelloService = MethodRegistry.get(RemoteHelloService.class);
        System.out.println("get remoteHelloService");
        Long result = remoteHelloService.copy(23L);
        System.out.println("result: " + result);
    }
}
