package com.my.liufeng.rpc.context;

import com.my.liufeng.rpc.utils.ClassScanner;
import com.my.liufeng.rpc.annotation.selector.RpcServiceSelector;
import com.my.liufeng.rpc.utils.CollectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * provider service注册中心
 */
public class ProviderRepository {

    private static Map<Class<?>, Object> singletonInstanceMap = null;

    public static synchronized void scan(String[] packages) {
        Set<Class<?>> classes = ClassScanner.scan(packages, new RpcServiceSelector());
        if (CollectionUtil.isEmpty(classes)){
            return;
        }
        singletonInstanceMap = new HashMap<>();
        classes.forEach(clazz -> {
            try {
                // todo 这里太粗暴了，强制要求该类提供无参构造器，而且使用该构造器实例化
                //  -- 后续改成spring容器，不然自己实现容器；或者提供put方法
                singletonInstanceMap.put(clazz, clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public static <T> T get(Class<T> clazz) {
        if (singletonInstanceMap == null) {
            throw new RuntimeException(clazz.getSimpleName() + " not init.");
        }
        return (T) singletonInstanceMap.get(clazz);
    }

    public static void put(Class<?> clazz, Object obj) {
        singletonInstanceMap.put(clazz, obj);
    }


}
