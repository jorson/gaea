package gaea.platform.security.exception;

/**
 * 安全模块使用到通用异常，继承自<code>AuthenticationException</code>
 * <p/>
 * 为了统一安全模块的异常处理
 */
public class AuthenticationException extends org.springframework.security.core.AuthenticationException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
