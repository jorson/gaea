package gaea.foundation.core.cache.ehcache;

import gaea.foundation.core.cache.AbstractCache;
import gaea.foundation.core.cache.CacheItem;
import gaea.foundation.core.cache.CacheItemResult;
import gaea.foundation.core.cache.support.EntryRefreshPolicy;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * EhCache缓存对象
 *
 * @author wuhy
 */
public class EhCache extends AbstractCache {
    /**
     * Logger对象
     */
    private static final Log logger = LogFactory.getLog(EhCache.class);
    private Cache cache = null;
    private String cacheName;

    public EhCache(Cache cache, String cacheName) {
        this(cache, cacheName, false);
    }

    public EhCache(Cache cache, String cacheName, boolean emptySupport) {
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
                Object value = null;
                Element element = cache.get(key);
                if (element != null) {
                    value = element.getObjectValue();
                }
                result.setValue(value);
            } catch (CacheException e) {
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
        for (Iterator<CacheItem> iterator = items.iterator(); iterator.hasNext(); ) {
            CacheItem item = iterator.next();
            String key = item.getKey();
            Element element = null;
            if (policy == null) {
                element = new Element(key, item.getValue());
            } else {
                element = new Element(key, item.getValue(), policy.getTimes(), policy.getTimes());
            }
            cache.put(element);
        }
    }

    /**
     * 刷新缓存所有的值，使用设置缓存时间都变成当前
     */
    public void flush() {
        cache.flush();
    }

    /**
     * 清除缓存中的值
     *
     * @param key Key
     */
    public void remove(String key) {
        cache.remove(key);
    }

    /**
     * 删除缓存中所有的值
     */
    public void clearInner() {
        cache.removeAll();
    }

    /**
     * 取得所有的Keys
     * 会先刷新缓存，去掉过期的缓存值
     *
     * @return
     */
    public List<String> getKeys() {
        return cache.getKeys();
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
