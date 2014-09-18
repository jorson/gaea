package gaea.foundation.core.cache;

/**
 * 抽象缓存提供者
 * <p/>
 * 实现一些通用的方法
 *
 * @author wuhy
 */
public abstract class AbstractCacheProvider implements CacheProvider {

    /**
     * 根据Name创建Cache，默认不支持空值处理
     *
     * @param cacheName
     * @return
     */
    @Override
    public Cache createCache(String cacheName) {
        return createCache(cacheName, false);
    }

    @Override
    public Cache createCache(String cacheName, boolean emptySupport) {
        return doCreateCache(cacheName, emptySupport);
    }

    /**
     * 创建内部使用Cache
     *
     * @return
     */
    protected abstract Cache doCreateCache(String cacheName, boolean emptySupport);
}
