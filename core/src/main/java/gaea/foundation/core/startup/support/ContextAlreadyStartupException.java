package gaea.foundation.core.startup.support;

import gaea.foundation.core.exception.BaseRuntimeException;

/**
 * 上下文重复启动异常类
 *
 * @author wuhy
 */
public class ContextAlreadyStartupException extends BaseRuntimeException {

    public ContextAlreadyStartupException() {
        super("the spring gaea.platform.security.context is alread started, please don't set new config locations");
    }
}
