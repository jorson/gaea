package gaea.foundation.core.cache;

/**
 * 缓存提供者接口
 * <p/>
 * 用于取得并创建缓存对象
 *
 * @author wuhy
 */
public interface CacheProvider {

    /**
     * 根据Name创建Cache，默认不支持空值处理
     *
     * @param cacheName
     * @return
     */
    public Cache createCache(String cacheName);

    /**
     * 根据Name创建Cache
     *
     * @param cacheName
     * @param emptySupport 是否支持空值处理
     * @return
     */
    public Cache createCache(String cacheName, boolean emptySupport);

}
