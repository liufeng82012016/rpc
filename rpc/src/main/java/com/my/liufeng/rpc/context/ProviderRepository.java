package com.my.liufeng.rpc.context;

import com.my.liufeng.rpc.utils.ClassScanner;
import com.my.liufeng.rpc.annotation.selector.RpcServiceSelector;
import com.my.liufeng.rpc.utils.CollectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * provider service注册中心
 * 扫描指定包下，使用了RpcService注解的类，并放到容器中
 */
public class ProviderRepository {

    private static Map<Class<?>, Object> singletonInstanceMap = null;

    /**
     * 扫描指定包，将服务提供者添加到单例map
     */
    public static synchronized void scan(String[] packages) {
        Set<Class<?>> classes = ClassScanner.scan(packages, new RpcServiceSelector());
        if (CollectionUtil.isEmpty(classes)) {
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

    /**
     * 根据类class对象获取服务提供者
     */
    public static <T> T get(Class<T> clazz) {
        if (singletonInstanceMap == null) {
            throw new RuntimeException(clazz.getSimpleName() + " not init.");
        }
        return (T) singletonInstanceMap.get(clazz);
    }

    /**
     * 暴露put方法，可以将没有使用RpcService注解的类添加到容器
     *
     * @param clazz class对象
     * @param obj   class对象对应的实例
     */
    public static void put(Class<?> clazz, Object obj) {
        singletonInstanceMap.put(clazz, obj);
    }


}
