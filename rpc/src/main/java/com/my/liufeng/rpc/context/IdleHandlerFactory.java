package com.my.liufeng.rpc.context;

import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class IdleHandlerFactory {
    public static IdleStateHandler getIdleHandler() {
        return new IdleStateHandler(10, 10, 10, TimeUnit.SECONDS);
    }
}
