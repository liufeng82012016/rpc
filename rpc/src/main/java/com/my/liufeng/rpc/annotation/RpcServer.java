package com.my.liufeng.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author liufeng
 * @Description: 标识是服务提供者，暂时用不上
 * @since 2021/5/27 19:25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RpcServer {
}
