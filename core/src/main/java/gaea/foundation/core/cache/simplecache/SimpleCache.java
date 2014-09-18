package gaea.foundation.core.cache.simplecache;

import gaea.foundation.core.cache.AbstractCache;
import gaea.foundation.core.cache.CacheItem;
import gaea.foundation.core.cache.CacheItemResult;
import gaea.foundation.core.cache.support.EntryRefreshPolicy;
import gaea.foundation.core.cache.support.ExpiresRefreshPolicy;
import gaea.foundation.core.utils.ObjectUtils;

import java.util.*;

/**
 * 简单的缓存，使用HashMap来保存
 * <p/>
 * 该缓存使用懒更新，也就可以如果数据没有手动去刷新，则不会自动去掉已经过期的值
 *
 * @author wuhy
 */
public class SimpleCache extends AbstractCache {

    private final Object SYNC_OBJECT = new Object();

    private Map<String, Object> cache = new HashMap<String, Object>();
    private Map<String, EntryRefreshPolicy> timers = new HashMap<String, EntryRefreshPolicy>();
    private List<String> keys = new ArrayList<String>();
    private String cacheName;

    public SimpleCache(String cacheName) {
        this(cacheName, false);
    }

    public SimpleCache(String cacheName, boolean emptySupport) {
        this.cacheName = cacheName;
        this.setEmptySupport(emptySupport);
    }

    /**
     * 从缓存中批量取得值，如果没有命中，则CacheItemResult里面的值为null且Hit为false
     *
     * @param keys
     * @return
     */
    @Override
    public List<CacheItemResult> getBatchResultInner(Iterable<String> keys) {
        List<CacheItemResult> results = new ArrayList<CacheItemResult>();
        if (keys == null) return results;
        for (Iterator<String> iterator = keys.iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            EntryRefreshPolicy policy = timers.get(key);
            CacheItemResult result = new CacheItemResult(key);
            if (policy == null || !policy.isTimeout()) {
                Object value = cache.get(key);
                result.setValue(ObjectUtils.deepClone(value));  //返回一个全新的对象
            } else {
                remove(key);
            }
            results.add(result);
        }
        return results;
    }

    /**
     * 使用特定的时间策略，在缓存中插入批量的值，特定的缓存处理
     *
     * @param items  批量数据
     * @param policy 过期策略
     */
    protected void putBatchInner(Iterable<CacheItem> items, EntryRefreshPolicy policy) {
        synchronized (SYNC_OBJECT) {
            for (Iterator<CacheItem> iterator = items.iterator(); iterator.hasNext(); ) {
                CacheItem item = iterator.next();
                String key = item.getKey();
                cache.put(key, item.getValue());
                if (policy != null) {
                    timers.put(key, policy);
                } else {
                    timers.remove(key);
                }
                if (!keys.contains(key)) {
                    keys.add(key);
                }
            }
        }
    }

    /**
     * 刷新缓存所有的值，使用设置缓存时间都变成当前
     */
    public void flush() {
        synchronized (SYNC_OBJECT) {
            Iterator<Map.Entry<String, EntryRefreshPolicy>> iterator = timers.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, EntryRefreshPolicy> policyEntry = iterator.next();
                String key = policyEntry.getKey();
                EntryRefreshPolicy policy = policyEntry.getValue();
                if (policy != null) {
                    timers.put(key, new ExpiresRefreshPolicy(policy.getTimes()));
                }
            }
        }
    }

    /**
     * 清除缓存中的值
     *
     * @param key Key
     */
    public void remove(String key) {
        if (keys.contains(key)) {
            synchronized (SYNC_OBJECT) {
                cache.remove(key);
                timers.remove(key);
                keys.remove(key);
            }
        }
    }

    /**
     * 删除缓存中所有的值
     */
    public void clearInner() {
        synchronized (SYNC_OBJECT) {
            cache.clear();
            timers.clear();
            keys.clear();
        }
    }

    /**
     * 取得所有的Keys
     * 会先刷新缓存，去掉过期的缓存值
     *
     * @return
     */
    public List<String> getKeys() {
        return keys;
    }

    /**
     * 取得缓存的名称
     *
     * @return
     */
    public String getCacheName() {
        return cacheName;
    }

}
