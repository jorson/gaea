package com.nd.gaea.core.exception;

/**
 * 运行期基本异常类
 * <p/>
 *
 * @author bifeng.liu
 */
public class BaseRuntimeException extends RuntimeException {
    /**
     * 错误编码
     */
    private String code;

    /**
     * 构造一个基本异常.
     */
    public BaseRuntimeException() {
        super();
    }

    /**
     * 构造一个基本异常.
     *
     * @param message 信息描述
     */
    public BaseRuntimeException(String message) {
        super(message);
    }

    /**
     * 构造一个基本异常.
     *
     * @param code    错误编码
     * @param message 信息描述
     */
    public BaseRuntimeException(String code, String message) {
        super(message);
        this.setCode(code);
    }

    /**
     * 构造一个基本异常.
     *
     * @param code    错误编码
     * @param message 信息描述
     */
    public BaseRuntimeException(String code, String message, Throwable cause) {
        super(message, cause);
        this.setCode(code);

    }

    /**
     * 构造一个基本异常.
     *
     * @param cause 根异常类（可以存入任何异常）
     */
    public BaseRuntimeException(Throwable cause) {
        super(cause);
    }


    /**
     * 构造一个基本异常.
     *
     * @param message 信息描述
     * @param cause   根异常类（可以存入任何异常）
     */
    public BaseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
