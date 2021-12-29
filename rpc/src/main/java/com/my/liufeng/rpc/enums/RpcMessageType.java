package com.my.liufeng.rpc.enums;

/**
 * rpc消息类型枚举
 */
public enum RpcMessageType {
    /**
     * 请求
     */
    TYPE_REQUEST((byte) 1),
    /**
     * 响应
     */
    TYPE_RESPONSE((byte) 2),
    /**
     * 心跳
     */
    TYPE_PING((byte) 3),
    /**
     * 心跳响应
     */
    TYPE_PONG((byte) 4);

    private byte type;


    RpcMessageType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }
}
