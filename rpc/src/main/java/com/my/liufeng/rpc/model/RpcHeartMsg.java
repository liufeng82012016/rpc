package com.my.liufeng.rpc.model;

import com.my.liufeng.rpc.enums.RpcMessageType;

public class RpcHeartMsg extends RpcMessage {
    public static final RpcHeartMsg PING = new RpcHeartMsg(RpcMessageType.TYPE_PING);
    public static final RpcHeartMsg PONG = new RpcHeartMsg(RpcMessageType.TYPE_PONG);

    private RpcHeartMsg(RpcMessageType rpcMessageType) {
        this.type = rpcMessageType.getType();
    }
}
