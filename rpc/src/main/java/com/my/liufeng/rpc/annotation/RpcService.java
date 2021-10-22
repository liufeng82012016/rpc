package com.my.liufeng.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识一个类为服务端提供服务的类
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RpcService {
    /**
     * 和MethodStub的className想对应。如果前者设置了类的全限定名，这里不需设置；否则需要一一对应
     */
    String className() default "";
}
