package gaea.foundation.core.cache.support;

import gaea.foundation.core.exception.BaseRuntimeException;

/**
 * 缓存异常类
 *
 * @author wuhy
 */
public class CacheException extends BaseRuntimeException {
    public CacheException() {
        super();
    }

    public CacheException(Throwable cause) {
        super(cause);
    }

    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }
}
