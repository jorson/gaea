package com.nd.gaea.web.exception;

import com.nd.gaea.core.exception.BaseRuntimeException;

/**
 * 控制器的异常类
 *
 * @author bifeng.liu
 */
public class ControllerException extends BaseRuntimeException {
    /**
     * 构造一个控制器异常类.
     */
    public ControllerException() {
        super();
    }

    /**
     * 构造一个控制器异常类.
     *
     * @param message 信息描述
     */
    public ControllerException(String message) {
        super(message);
    }

    /**
     * 构造一个控制器异常类.
     *
     * @param cause 根异常类（可以存入任何异常）
     */
    public ControllerException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造一个控制器异常类.
     *
     * @param code    错误编码
     * @param message 信息描述
     */
    public ControllerException(String code, String message) {
        super(code, message);
    }

    /**
     * 构造一个控制器异常类.
     *
     * @param code    错误编码
     * @param message 信息描述
     */
    public ControllerException(String code, String message, Throwable cause) {
        super(code, message, cause);

    }

    /**
     * 构造一个控制器异常类.
     *
     * @param message 信息描述
     * @param cause   根异常类（可以存入任何异常）
     */
    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }

}
