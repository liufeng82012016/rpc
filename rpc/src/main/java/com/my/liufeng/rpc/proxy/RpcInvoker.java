package com.my.liufeng.rpc.proxy;

import com.my.liufeng.rpc.annotation.MethodStub;
import com.my.liufeng.rpc.context.SocketContainer;
import com.my.liufeng.rpc.exception.InnerException;
import com.my.liufeng.rpc.model.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 动态代理实现，代理接口发起远程调用
 */
public class RpcInvoker implements InvocationHandler {
    /**
     * 当前类声明的方法的集合
     */
    private final Set<Method> methods;
    /**
     * 该远程服务使用的注解
     */
    private final MethodStub methodStub;
    // todo 配置化，接口超时时间，响应时间，负责均衡策略

    public RpcInvoker(Class<?> remoteService) {
        if (!remoteService.isInterface()) {
            throw new InnerException("class " + remoteService.getName() + " is not a interface");
        }
        methodStub = remoteService.getAnnotation(MethodStub.class);
        if (methodStub == null) {
            throw new InnerException("class " + remoteService.getName() + " discard methodStub annotation");
        }
        Method[] declaredMethods = remoteService.getDeclaredMethods();
        methods = Arrays.stream(declaredMethods).collect(Collectors.toSet());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!methods.contains(method)) {
            //  判断是接口声明的方法，还是Object附带的方法 -- 如果是object声明的方法，忽略，不做代理
            return method.invoke(this, args);
        }
        RpcRequest rpcRequest = new RpcRequest();
        // todo 生成requestId -- 分布式环境下，需要生成唯一ID
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setParams(args);
        rpcRequest.setServiceClass(methodStub.className());
        return SocketContainer.sendMsg(rpcRequest, methodStub);
    }
}
