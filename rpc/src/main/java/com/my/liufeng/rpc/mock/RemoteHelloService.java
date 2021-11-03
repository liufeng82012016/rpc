package com.my.liufeng.rpc.mock;

import com.my.liufeng.rpc.annotation.MethodStub;

/**
 * 模拟api方法 -- 实际上应该放到独立的包
 */
@MethodStub(serverAddresses = {"127.0.0.1:9080"}, className = "com.my.liufeng.rpc.mock.HelloService", serverName = "A")
public interface RemoteHelloService {

    String hello();

    Long copy(Long max);

    MockResp obj();
}
