package com.my.liufeng.rpc.constants;

import java.nio.charset.StandardCharsets;

public interface RpcConstants {
    /**
     * 消息分隔符
     */
    byte[] SEPARATOR = "&;".getBytes(StandardCharsets.UTF_8);
    /**
     * 消息最大长度
     */
    long MAX_LENGTH = 2 * 1024 * 1024;
    /**
     * 消息 长度，用int标识，占用4字节
     */
    int MSG_BODY_LENGTH = 4;
    /**
     * 消息 类型，用byte标识，占用1字节
     */
    int MSG_BODY_TYPE = 1;
    /**
     * 消息 魔数 用byte[] 标识，占用16byte -- 准备改成分隔符
     */
    int MSG_MAGIC_NUMBER = SEPARATOR.length;
    /**
     * 消息，除了消息体，其他字段所占用的长度
     */
    int MSG_EXTRA_LENGTH = MSG_BODY_LENGTH + MSG_BODY_TYPE + MSG_MAGIC_NUMBER * 2;
}
