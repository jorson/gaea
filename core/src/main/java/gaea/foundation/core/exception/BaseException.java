package gaea.foundation.core.exception;

/**
 * 基本异常类
 * <p/>
 *
 * @author wuhy
 */
public class BaseException extends Exception {
    /**
     * 错误编码
     */
    private String code;

    /**
     * 构造一个基本异常.
     */
    public BaseException() {
        super();
    }

    /**
     * 构造一个基本异常.
     *
     * @param message 信息描述
     */
    public BaseException(String message) {
        super(message);
    }

    /**
     * 构造一个基本异常.
     *
     * @param code    错误编码
     * @param message 信息描述
     */
    public BaseException(String code, String message) {
        super(message);
        this.setCode(code);
    }

    /**
     * 构造一个基本异常.
     *
     * @param code    错误编码
     * @param message 信息描述
     */
    public BaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.setCode(code);

    }

    /**
     * 构造一个基本异常.
     *
     * @param cause 根异常类（可以存入任何异常）
     */
    public BaseException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造一个基本异常.
     *
     * @param message 信息描述
     * @param cause   根异常类（可以存入任何异常）
     */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
