package gaea.foundation.core.exception;

import gaea.foundation.core.exception.BaseRuntimeException;

/**
 * 业务的异常类
 *
 * @author wuhy
 */
public class BusinessException extends BaseRuntimeException {
    /**
     * 构造一个业务异常类.
     */
    public BusinessException() {
        super();
    }

    /**
     * 构造一个业务异常.
     *
     * @param cause 根异常类（可以存入任何异常）
     */
    public BusinessException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造一个业务异常类.
     *
     * @param message 信息描述
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * 构造一个业务异常类.
     *
     * @param code    错误编码
     * @param message 信息描述
     */
    public BusinessException(String code, String message) {
        super(code, message);
    }

    /**
     * 构造一个业务异常类.
     *
     * @param code    错误编码
     * @param message 信息描述
     */
    public BusinessException(String code, String message, Throwable cause) {
        super(code, message, cause);

    }

    /**
     * 构造一个业务异常类.
     *
     * @param message 信息描述
     * @param cause   根异常类（可以存入任何异常）
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

}
