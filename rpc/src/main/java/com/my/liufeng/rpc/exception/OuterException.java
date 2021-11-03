package com.my.liufeng.rpc.exception;

import com.my.liufeng.rpc.enums.ResultCode;

public class OuterException extends RuntimeException {

    private String msg;
    private Integer code;

    public OuterException() {
    }

    public OuterException(Integer code) {
        this.code = code;
    }

    public OuterException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public OuterException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    public OuterException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
