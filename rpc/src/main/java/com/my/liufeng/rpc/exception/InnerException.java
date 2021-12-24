package com.my.liufeng.rpc.exception;

/**
 * rpc框架内部异常
 */
public class InnerException extends OuterException {

    public InnerException(String msg) {
        super(msg);
    }

}
