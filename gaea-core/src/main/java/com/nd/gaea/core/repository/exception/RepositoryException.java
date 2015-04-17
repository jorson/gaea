package com.nd.gaea.core.repository.exception;

import com.nd.gaea.core.exception.BaseRuntimeException;

/**
 * 数据仓储的异常类
 *
 * @author bifeng.liu
 */
public class RepositoryException extends BaseRuntimeException {
    /**
     * 构造一个数据仓储异常类.
     */
    public RepositoryException() {
        super();
    }

    /**
     * 构造一个数据仓储异常类.
     *
     * @param message 信息描述
     */
    public RepositoryException(String message) {
        super(message);
    }

    /**
     * 构造一个数据仓储异常类.
     *
     * @param code    错误编码
     * @param message 信息描述
     */
    public RepositoryException(String code, String message) {
        super(code, message);
    }

    /**
     * 构造一个数据仓储异常类.
     *
     * @param code    错误编码
     * @param message 信息描述
     */
    public RepositoryException(String code, String message, Throwable cause) {
        super(code, message, cause);

    }

    /**
     * 构造一个数据仓储异常类.
     *
     * @param message 信息描述
     * @param cause   根异常类（可以存入任何异常）
     */
    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

}
