package com.nd.gaea.web.exception;

/**
 * 异常的工厂类，处理一些通用的方法
 *
 * @author bifeng.liu
 */
public class ControllerExceptionFactory {

    private ControllerExceptionFactory() {
    }

    /**
     * 把异常类包装成ControllerException
     *
     * @param ex
     * @return
     */
    public static ControllerException wrapControllerException(Exception ex) {
        return new ControllerException(ex);
    }

    /**
     * 把异常类包装成ControllerException
     *
     * @param message
     * @param ex
     * @return
     */
    public static ControllerException wrapControllerException(String message, Exception ex) {
        return new ControllerException(message, ex);
    }

    /**
     * 创建ControllerException异常类
     *
     * @param message
     * @return
     */
    public static ControllerException createControlException(String message) {
        return new ControllerException(message);
    }
}
