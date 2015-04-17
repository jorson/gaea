package com.nd.gaea.web.controller;


public class ControllerResult {
    /**
     * 消息CODE，大于0为成功，小于等于0为失败
     */
    private int code;
    /**
     * 消息
     */
    private String message;


    public ControllerResult() {
        this(1, "成功");
    }

    public ControllerResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
