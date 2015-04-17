package com.nd.gaea.core.exception;

/**
 * 异常的工厂类，处理一些通用的方法
 *
 * @author bifeng.liu
 */
public class BusinessExceptionFactory {

    private BusinessExceptionFactory() {
    }

    /**
     * 把异常类包装成BusinessException
     *
     * @param ex
     * @return
     */
    public static BusinessException wrapBusinessException(Exception ex) {
        return new BusinessException(ex);
    }

    /**
     * 把异常类包装成BusinessException
     *
     * @param message
     * @param ex
     * @return
     */
    public static RuntimeException wrapBusinessException(String message, Exception ex) {
        return new BusinessException(message, ex);
    }
}
