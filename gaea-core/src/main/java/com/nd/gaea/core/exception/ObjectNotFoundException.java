package com.nd.gaea.core.exception;

/**
 * 对象无法找到异常
 * <p/>
 *
 * @author bifeng.liu
 * @since 2014/12/9
 */
public class ObjectNotFoundException extends BaseRuntimeException {
    /**
     * 构造一个对象无法找到异常
     */
    public ObjectNotFoundException() {
    }

    /**
     * 构造一个对象无法找到异常
     *
     * @param message
     */
    public ObjectNotFoundException(String message) {
        super(message);
    }

    /**
     * 构造一个对象无法找到异常
     *
     * @param cause
     */
    public ObjectNotFoundException(Throwable cause) {
        super(cause);
    }
}
