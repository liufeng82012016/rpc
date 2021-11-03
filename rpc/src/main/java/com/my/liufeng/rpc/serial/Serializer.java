package com.my.liufeng.rpc.serial;

/**
 * @author liufeng
 * @description: 序列化器
 * @since 2021/5/27 19:30
 */
public interface Serializer {

    /**
     * 将对象转换为string
     */
    byte[] serialize(Object obj);

    /**
     * 将String转换为对象
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
