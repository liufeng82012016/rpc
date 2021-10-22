package com.my.liufeng.rpc.exception;

public class DispatcherException extends RuntimeException {
    private String msg;
    private Integer code;

    public DispatcherException() {
    }

    public DispatcherException(Integer code) {
        this.code = code;
    }

    public DispatcherException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public DispatcherException(Integer code, String msg) {
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
