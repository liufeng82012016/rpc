package com.my.liufeng.rpc.context;

import com.my.liufeng.rpc.annotation.selector.MethodStubSelector;
import com.my.liufeng.rpc.proxy.RpcInvoker;
import com.my.liufeng.rpc.utils.ClassScanner;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 方法注册中心 -- 动态代理远程调用方法
 */
public class MethodProxyRepository {
    private static InternalLogger logger = InternalLoggerFactory.getInstance(MethodProxyRepository.class);
    /**
     * 代理类map
     * class -- proxyInstance
     */
    private static Map<Class<?>, Object> proxyInstanceMap = null;

    /**
     * 扫描远程服务，生成对应的代理类
     *
     * @param packages 包名
     */
    public static synchronized void scan(String[] packages) {
        Set<Class<?>> classes = ClassScanner.scan(packages, new MethodStubSelector());
        if (classes.isEmpty()) {
            logger.info("service with annotation @MethodStub is empty");
            return;
        }
        proxyInstanceMap = new HashMap<>();
        // 扫描所有的远程服务，创建代理类，加入到map
        for (Class<?> clazz : classes) {
            Object proxy = Proxy.newProxyInstance(ClassScanner.getClassLoader(), new Class[]{clazz},
                    new RpcInvoker(clazz));
            proxyInstanceMap.put(clazz, proxy);
        }
    }

    /**
     * 获取代理实例
     * 目前只能手动调用；如果是Spring的容器，注入对应接口即可
     */
    public static <T> T getProxy(Class<T> clazz) {
        if (proxyInstanceMap == null) {
            throw new RuntimeException(clazz.getSimpleName() + " not init.");
        }
        return (T) proxyInstanceMap.get(clazz);
    }


}
