package com.my.liufeng.rpc.mock;

import com.my.liufeng.rpc.annotation.RpcService;

/**
 * 模拟服务提供者
 */
@RpcService
public class HelloService {

    public String hello() {
        return "world";
    }

    public Long copy(Long max) {
        return max;
    }
}
