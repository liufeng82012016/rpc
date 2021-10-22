package com.my.liufeng.rpc.serial;

import com.my.liufeng.rpc.model.Request;
import com.my.liufeng.rpc.model.Response;

public class DefaultMsgSerializer implements MsgSerializer {
    @Override
    public String serialize(Request request) {
        return null;
    }

    @Override
    public Response deserialize(String resp) {
        return null;
    }
}
