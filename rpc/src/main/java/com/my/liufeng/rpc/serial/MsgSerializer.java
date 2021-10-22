package com.my.liufeng.rpc.serial;

import com.my.liufeng.rpc.model.Request;
import com.my.liufeng.rpc.model.Response;

/**
 * @Author liufeng
 * @Description: 序列化器
 * @since 2021/5/27 19:30
 */
public interface MsgSerializer {
    /**
     * 将消息转换为string
     *
     * @param request
     * @return
     */
    String serialize(Request request);

    /**
     * 将String转换为相应内容
     *
     * @param resp
     * @return
     */
    Response deserialize(String resp);
}
