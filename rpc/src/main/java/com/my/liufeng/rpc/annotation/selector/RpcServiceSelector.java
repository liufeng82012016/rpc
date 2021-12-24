package com.my.liufeng.rpc.annotation.selector;

import com.my.liufeng.rpc.annotation.RpcService;

/**
 * 扫描使用了RpcService注解的类
 */
public class RpcServiceSelector implements ClassSelector {
    @Override
    public boolean select(Class<?> clazz) {
        // 获取注解
        RpcService rpcService = clazz.getAnnotation(RpcService.class);
        if (rpcService == null) {
            return false;
        }
        if (clazz.isInterface()) {
            // 只扫描普通类
            return false;
        }
        return true;
    }
}
