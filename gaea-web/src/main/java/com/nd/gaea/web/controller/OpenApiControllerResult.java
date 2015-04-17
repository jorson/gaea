package com.nd.gaea.web.controller;

/**
 * OpenApi Controller的返回值
 *
 * @author bifeng.liu
 */
public class OpenApiControllerResult<T> extends ControllerResult {

    /**
     * 数据
     */
    private T data;

    public OpenApiControllerResult() {
        this(0, "SUCCESS", null);
    }

    public OpenApiControllerResult(int code, String message, T data) {
        super(code, message);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
