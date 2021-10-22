package com.my.liufeng.rpc.utils;

import com.alibaba.fastjson.JSONObject;
import com.my.liufeng.rpc.serial.DefaultMsgSerializer;
import com.my.liufeng.rpc.serial.MsgSerializer;

public class SerialUtil {
    private static MsgSerializer msgSerializer = new DefaultMsgSerializer();

    /**
     * 将消息转换为string
     */
    public static String serialize(Object obj) {
        return JSONObject.toJSONString(obj);
    }

    /**
     * 将String转换为相应内容
     */
    public static <T> T deserialize(String msg, Class<T> clazz) {
        return JSONObject.parseObject(msg, clazz);
    }
}
