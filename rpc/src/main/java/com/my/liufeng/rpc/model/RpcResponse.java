package com.my.liufeng.rpc.model;

import com.my.liufeng.rpc.enums.RpcMessageType;

/**
 * @Author liufeng
 * @Description: rpc响应
 * @since 2021/5/27 19:33
 */
public class RpcResponse<T> extends RpcMessage implements Response {
    /**
     * 请求id
     */
    private String requestId;
    /**
     * 服务内容
     */
    private T data;
    private Integer code;
    private String msg;

    public RpcResponse() {
        this.code = 0;
        this.type = RpcMessageType.TYPE_RESPONSE.getType();
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public T getData() {
        return data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "requestId='" + requestId + '\'' +
                ", data=" + data +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
