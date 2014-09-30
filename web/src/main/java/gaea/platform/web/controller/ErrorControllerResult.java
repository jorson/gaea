package gaea.platform.web.controller;

import gaea.foundation.core.utils.StringUtils;

/**
 * 保存失败Controller的返回值
 *
 * @author bifeng.liu
 */
public class ErrorControllerResult extends ControllerResult {
    /**
     * 异常
     */
    private Throwable exception;

    public ErrorControllerResult() {
        this(-1, "Unknown Error");
    }

    public ErrorControllerResult(String message) {
        this(-1, message);
    }

    public ErrorControllerResult(String message, Throwable exception) {
        this(-1, message, exception);
    }

    public ErrorControllerResult(int code, String message) {
        super(code, message);
    }

    public ErrorControllerResult(int code, String message, Throwable exception) {
        super(code, message);
        this.setException(exception);
    }

    /**
     * 使用默认的消息源填充消息
     *
     * @return
     */
    public String populate() {
        String temp = this.getMessage();
        if (this.exception != null) {
            temp = !StringUtils.isEmpty(temp) ? temp + "," : "";
            temp += this.exception.getMessage();
        }
        return temp;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }
}
