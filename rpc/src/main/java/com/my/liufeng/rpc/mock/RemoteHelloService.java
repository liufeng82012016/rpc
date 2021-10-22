package com.my.liufeng.rpc.mock;

import com.my.liufeng.rpc.annotation.MethodStub;

/**
 * 模拟api方法
 */
@MethodStub(serverAddresses = {"127.0.0.1:8080"}, className = "com.my.liufeng.rpc.mock.HelloService", serverName = "A")
public interface RemoteHelloService {

    String hello();

    Long copy(Long max);
}
