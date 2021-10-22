package com.my.liufeng.rpc;

import com.my.liufeng.rpc.context.MethodRegistry;
import com.my.liufeng.rpc.context.ServiceRegistry;
import com.my.liufeng.rpc.start.NetServer;
import com.my.liufeng.rpc.utils.CollectionUtil;

public class StartServer {
    public static void start(String[] packages, int port) {
        if (CollectionUtil.isEmpty(packages)) {
            packages = new String[]{StartServer.class.getPackage().getName()};
        }
        ServiceRegistry.scan(packages);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MethodRegistry.scan(packages);
        new NetServer(port);
    }

    public static void main(String[] args) {
        start(null, 8080);
    }
}
