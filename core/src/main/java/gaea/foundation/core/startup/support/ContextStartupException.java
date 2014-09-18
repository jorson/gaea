package gaea.foundation.core.startup.support;

import gaea.foundation.core.exception.BaseRuntimeException;

/**
 * 上下文启动异常类
 *
 * @author wuhy
 */
public class ContextStartupException extends BaseRuntimeException {

    /**
     * 构造一个上下文启动异常类
     *
     * @param message 信息描述
     */
    public ContextStartupException(String message) {
        super(message);
    }

    /**
     * 构造一个上下文启动异常类
     *
     * @param message 信息描述
     * @param cause   根异常类（可以存入任何异常）
     */
    public ContextStartupException(String message, Throwable cause) {
        super(message, cause);
    }
}
