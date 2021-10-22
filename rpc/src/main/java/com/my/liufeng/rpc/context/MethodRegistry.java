package com.my.liufeng.rpc.context;

import com.my.liufeng.rpc.annotation.MethodStub;
import com.my.liufeng.rpc.scan.ClassScanner;
import com.my.liufeng.rpc.scan.MethodStubSelector;
import com.my.liufeng.rpc.model.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 方法注册中心 -- 动态代理远程调用方法
 */
public class MethodRegistry {

    private static Map<Class<?>, Object> proxyInstanceMap = null;

    public static synchronized void scan(String[] packages) {
        Set<Class<?>> classes = ClassScanner.scan(packages, new MethodStubSelector());
        if (classes.isEmpty()) {
            return;
        }
        proxyInstanceMap = new HashMap<>();
        for (Class<?> clazz : classes) {
            MethodStub methodStub = clazz.getAnnotation(MethodStub.class);
            if (methodStub == null) {
                return;
            }
            Method[] declaredMethods = clazz.getDeclaredMethods();
            Set<Method> methods = Arrays.stream(declaredMethods).collect(Collectors.toSet());
            Object proxy = Proxy.newProxyInstance(ClassScanner.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (!methods.contains(method)) {
                        //  判断是接口声明的方法，还是Object附带的方法 -- 如果是object声明的方法，忽略，不做代理
                        return method.invoke(this, args);
                    }
                    RpcRequest rpcRequest = new RpcRequest();
                    // todo 生成requestId
                    rpcRequest.setRequestId(UUID.randomUUID().toString());
                    rpcRequest.setMethodName(method.getName());
                    rpcRequest.setParameterTypes(method.getParameterTypes());
                    rpcRequest.setParams(args);
                    rpcRequest.setServiceClass(methodStub.className());
                    System.out.println(String.format("%s %s invoke", clazz.getName(), method.getName()));
                    return ClientSocketContainer.sendMsg(rpcRequest, methodStub, method.getReturnType());
                }
            });
            proxyInstanceMap.put(clazz, proxy);
        }
    }

    /**
     * 获取代理实例
     */
    public static <T> T get(Class<T> clazz) {
        if (proxyInstanceMap == null) {
            throw new RuntimeException(clazz.getSimpleName() + " not init.");
        }
        return (T) proxyInstanceMap.get(clazz);
    }

    public static Map<Class<?>, Object> getInstances() {
        return proxyInstanceMap;
    }
}
