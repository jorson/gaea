package gaea.foundation.core.cache.simplecache;

import gaea.foundation.core.cache.AbstractCacheProvider;
import gaea.foundation.core.cache.Cache;
import gaea.foundation.core.cache.CacheProvider;

/**
 * 默认缓存对象提供者
 *
 * @author wuhy
 */
public class SimpleCacheProvider extends AbstractCacheProvider {
    protected SimpleCacheProvider() {
    }

    /**
     * 创建内部使用Cache
     *
     * @return
     */
    protected Cache doCreateCache(String cacheName, boolean emptySupport) {
        return new SimpleCache(cacheName, emptySupport);
    }

    public static CacheProvider createProvider() {
        return new SimpleCacheProvider();
    }
}
