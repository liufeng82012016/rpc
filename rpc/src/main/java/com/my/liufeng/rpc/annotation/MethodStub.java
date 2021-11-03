package com.my.liufeng.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法签名注解，类似@FeignClient
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface MethodStub {
    /**
     * 标识调用哪一个服务提供者
     */
    String serverName() default "";

    /**
     * 标识调用服务提供者哪一个类的方法
     */
    String className();

    /**
     * 可用服务列表，serverName/serverAddresses 取一个，前者取注册中心，后者写死
     */
    String[] serverAddresses() default {};

    /**
     * 接口调用超时时间
     */
    int timeout() default 1000;
}
