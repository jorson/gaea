package gaea.foundation.core.cache;

import gaea.foundation.core.cache.support.EntryRefreshPolicy;

import java.util.List;

/**
 * Cache接口
 * <p/>
 *
 * @author wuhy
 */
public interface Cache {
    /**
     * 默认过期时间
     */
    public final static int DEFAULT_EXPIRED_TIME = 2 * 60;

    /**
     * 取得缓存的名称
     *
     * @return
     */
    public String getCacheName();

    /**
     * 是否支持空值缓存
     *
     * @return
     */
    public boolean isEmptySupport();

    /**
     * 从缓存中取得值
     *
     * @param key
     * @return
     */
    public Object get(String key);

    /**
     * 从缓存中批量取得值
     *
     * @param keys
     * @return
     */
    public List<Object> getBatch(String[] keys);

    /**
     * 从缓存中批量取得值
     *
     * @param keys
     * @return
     */
    public List<Object> getBatch(Iterable<String> keys);

    /**
     * 从缓存中批量取得值，包括状态
     *
     * @param keys
     * @return
     */
    public List<CacheItemResult> getBatchResult(Iterable<String> keys);

    /**
     * 使用默认的时间策略，在缓存中插入值
     *
     * @param key   键
     * @param value 值
     */
    public void put(String key, Object value);

    /**
     * 使用特定的过期时间，在缓存中插入值
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间(秒)
     */
    public void put(String key, Object value, int timeout);

    /**
     * 使用特定的时间策略，在缓存中插入值
     *
     * @param key    键
     * @param value  值
     * @param policy 策略
     */
    public void put(String key, Object value, EntryRefreshPolicy policy);

    /**
     * 使用默认的时间策略，在缓存中插入批量的值
     *
     * @param items
     * @return
     */
    public void putBatch(Iterable<CacheItem> items);

    /**
     * 使用特定的过期时间，在缓存中插入批量的值
     *
     * @param items
     * @return
     */
    public void putBatch(Iterable<CacheItem> items, int timeout);

    /**
     * 使用特定的时间策略，在缓存中插入批量的值
     *
     * @param items
     * @return
     */
    public void putBatch(Iterable<CacheItem> items, EntryRefreshPolicy policy);

    /**
     * 使用默认的时间策略，在缓存中插入批量的值
     *
     * @param items
     * @return
     */
    public void putBatch(CacheItem[] items);

    /**
     * 使用特定的过期时间，在缓存中插入批量的值
     *
     * @param items
     * @return
     */
    public void putBatch(CacheItem[] items, int timeout);

    /**
     * 使用特定的时间策略，在缓存中插入批量的值
     *
     * @param items
     * @return
     */
    public void putBatch(CacheItem[] items, EntryRefreshPolicy policy);

    /**
     * 刷新缓存中所有过期的值
     */
    public void flush();

    /**
     * 删除缓存中的某个值
     *
     * @param key
     */
    public void remove(String key);

    /**
     * 删除缓存中所有的值
     */
    public void clear();

    /**
     * 取得所有的Key
     *
     * @return
     */
    public List<String> getKeys();
}
