package com.my.liufeng.rpc.utils;

import com.my.liufeng.rpc.serial.ProtostuffSerializer;
import com.my.liufeng.rpc.serial.Serializer;

public class SerialUtil {
    private static volatile Serializer serializer = null;

    /**
     * 将消息转换为byte[]
     */
    public static byte[] serialize(Object obj) {
        checkSerializerIfInit();
        return serializer.serialize(obj);
    }

    /**
     * 将byte[]转换为相应内容
     */
    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        checkSerializerIfInit();
        return serializer.deserialize(bytes, clazz);
    }

    /**
     * 将String转换为相应内容
     */
    public static <T> T deserialize(String s, Class<T> clazz) {
        return deserialize(s.getBytes(), clazz);
    }


    /**
     * 检查序列化器是否为初始化过
     */
    private static void checkSerializerIfInit() {
        if (serializer == null) {
            synchronized (SerialUtil.class) {
                if (serializer == null) {
                    serializer = new ProtostuffSerializer();
                }
            }
        }
    }

    /**
     * 提供set方法
     * 可供SPI调用
     */
    public static void setMsgSerializer(Serializer serializer) {
        SerialUtil.serializer = serializer;
    }
}
