package com.my.liufeng.rpc.model;

import com.my.liufeng.rpc.enums.RpcMessageType;

public class RpcHeartMsg extends RpcMessage{
    public RpcHeartMsg() {
        this.type = RpcMessageType.TYPE_REQUEST.getType();
    }


}
