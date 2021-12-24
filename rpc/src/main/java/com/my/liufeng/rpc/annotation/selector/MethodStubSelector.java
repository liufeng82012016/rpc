package com.my.liufeng.rpc.annotation.selector;

import com.my.liufeng.rpc.annotation.MethodStub;
import com.my.liufeng.rpc.utils.CollectionUtil;

/**
 * 扫描使用了MethodStub注解的接口
 */
public class MethodStubSelector implements ClassSelector {
    @Override
    public boolean select(Class<?> clazz) {
        if (!clazz.isInterface()) {
            // 只扫描接口
            return false;
        }
        // 获取注解
        MethodStub methodStub = clazz.getAnnotation(MethodStub.class);
        if (methodStub == null) {
            return false;
        }
        // 注解参数校验
        if (CollectionUtil.isEmpty(methodStub.className())) {
            throw new RuntimeException(clazz.getSimpleName() + " className is null");
        }
        if (CollectionUtil.isEmpty(methodStub.serverName()) && CollectionUtil.isEmpty(methodStub.serverAddresses())) {
            throw new RuntimeException(clazz.getSimpleName() + " serverName and serverAddresses is null");
        }
        return true;
    }
}
