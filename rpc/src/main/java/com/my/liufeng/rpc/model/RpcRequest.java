package com.my.liufeng.rpc.model;

import com.my.liufeng.rpc.enums.RpcMessageType;

/**
 * @Author liufeng
 * @Description: rpc请求
 * @since 2021/5/27 19:33
 */
public class RpcRequest extends RpcMessage implements Request {

    /**
     * 请求id
     */
    private String requestId;
    /**
     * 服务者 服务名
     */
    private String providerName;
    /**
     * 服务方处理类
     */
    private String serviceClass;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * 实参
     */
    private Object[] params;

    public RpcRequest() {
        type = RpcMessageType.TYPE_REQUEST.getType();
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

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }


}
