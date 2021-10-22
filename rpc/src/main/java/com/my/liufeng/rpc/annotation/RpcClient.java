package com.my.liufeng.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author liufeng
 * @Description: 标识这是一个rpc客户端，会建立与服务端的长链接 -- 用不上了，会自动根据MethodStub注解去扫描
 * @since 2021/5/27 19:25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RpcClient {

}
