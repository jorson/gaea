package gaea.foundation.core.cache.oscache;

import gaea.foundation.core.cache.AbstractCache;
import gaea.foundation.core.cache.CacheItem;
import gaea.foundation.core.cache.CacheItemResult;
import gaea.foundation.core.cache.support.EntryRefreshPolicy;
import com.opensymphony.oscache.base.CacheEntry;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

import java.util.*;

/**
 * OsCache缓存
 * <p/>
 * 使用OsCache中的GeneralCacheAdministrator来管理Cache
 *
 * @author wuhy
 */
public class OsCache extends AbstractCache {

    private final Object SYNC_OBJECT = new Object();

    private GeneralCacheAdministrator cache = null;

    private String cacheName;

    private List<String> keys = new ArrayList<String>();

    public OsCache(GeneralCacheAdministrator cache, String cacheName) {
        this(cache, cacheName, false);
    }

    public OsCache(GeneralCacheAdministrator cache, String cacheName, boolean emptySupport) {
        this.cache = cache;
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
        if (keys == null) {
            return results;
        }
        for (Iterator<String> iterator = keys.iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            CacheItemResult result = new CacheItemResult(key);
            try {
                Object value = cache.getFromCache(key);
                result.setValue(value);
            } catch (com.opensymphony.oscache.base.NeedsRefreshException e) {
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
    protected void putBatchInner(Iterable<CacheItem> items, final EntryRefreshPolicy policy) {
        synchronized (SYNC_OBJECT) {
            for (Iterator<CacheItem> iterator = items.iterator(); iterator.hasNext(); ) {
                CacheItem item = iterator.next();
                String key = item.getKey();
                if (!keys.contains(key)) {
                    keys.add(key);
                }
                if (policy == null) {
                    cache.putInCache(key, item.getValue());
                } else {
                    cache.putInCache(key, item.getValue(), new com.opensymphony.oscache.base.EntryRefreshPolicy() {
                        public boolean needsRefresh(CacheEntry cacheEntry) {
                            return policy.isTimeout();
                        }
                    });
                }
            }
        }
    }

    /**
     * 刷新缓存所有的值，使用设置缓存时间都变成当前
     */
    public void flush() {
        super.flush();
//        synchronized (SYNC_OBJECT) {
//            Iterator<Map.Entry<String, EntryRefreshPolicy>> iterator = timers.entrySet().iterator();
//            while (iterator.hasNext()) {
//                Map.Entry<String, EntryRefreshPolicy> policyEntry = iterator.next();
//                String key = policyEntry.getKey();
//                EntryRefreshPolicy policy = policyEntry.getValue();
//                if (policy != null) {
//                    timers.put(key, new ExpiresRefreshPolicy(policy.getTimes()));
//                }
//            }
//        }
    }

    /**
     * 清除缓存中的值
     *
     * @param key Key
     */
    public void remove(String key) {
        if (keys.contains(key)) {
            synchronized (SYNC_OBJECT) {
                cache.removeEntry(key);
                keys.remove(key);
            }
        }
    }

    /**
     * 删除缓存中所有的值
     */
    public void clearInner() {
        synchronized (SYNC_OBJECT) {
            cache.flushAll();
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
