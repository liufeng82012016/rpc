package com.my.liufeng.rpc.serial;

import com.alibaba.fastjson.JSONObject;

import java.nio.charset.StandardCharsets;

/**
 * 默认使用的序列化器
 */
public class JsonSerializer implements Serializer {

    @Override
    public byte[] serialize(Object obj) {
        return JSONObject.toJSONString(obj).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return JSONObject.parseObject(new String(bytes), clazz);
    }

}
