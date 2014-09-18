package gaea.foundation.core.cache.redis;

import gaea.foundation.core.cache.AbstractCache;
import gaea.foundation.core.cache.CacheItem;
import gaea.foundation.core.cache.CacheItemResult;
import gaea.foundation.core.cache.support.EntryRefreshPolicy;
import gaea.foundation.core.exception.BaseRuntimeException;
import gaea.foundation.core.utils.ObjectUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Redis缓存，使用Redis来处理分布式缓存
 *
 * @author wuhy
 */
public class RedisCache extends AbstractCache {

    private Jedis jedis;

    private String cacheName;

    public RedisCache(Jedis jedis, String cacheName) {
        this(jedis, cacheName, false);
    }

    public RedisCache(Jedis jedis, String cacheName, boolean emptySupport) {
        this.jedis = jedis;
        this.cacheName = cacheName;
        this.setEmptySupport(emptySupport);
    }

    @Override
    protected void clearInner() {
        jedis.flushDB();
    }

    @Override
    protected List<CacheItemResult> getBatchResultInner(Iterable<String> keys) {
        List<CacheItemResult> results = new ArrayList<CacheItemResult>();
        if (keys == null) {
            return results;
        }
        try {
            for (Iterator<String> iterator = keys.iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                CacheItemResult result = new CacheItemResult(key);
                byte[] value = jedis.get(key.getBytes());
                if (value != null && value.length > 0) {
                    result.setValue(ObjectUtils.unserialize(value));  //返回一个全新的对象
                }
                results.add(result);
            }
        } catch (ClassNotFoundException ex) {
            throw new BaseRuntimeException("get redis value happen error!", ex);
        } catch (IOException ex) {
            throw new BaseRuntimeException("get redis value happen error!", ex);
        }
        return results;
    }

    @Override
    protected void putBatchInner(Iterable<CacheItem> items, EntryRefreshPolicy policy) {
        try {
            for (Iterator<CacheItem> iterator = items.iterator(); iterator.hasNext(); ) {
                CacheItem item = iterator.next();
                String key = item.getKey();
                jedis.set(key.getBytes(), ObjectUtils.serialize(item.getValue()));
                if (policy != null) {
                    jedis.expire(key.getBytes(), policy.getTimes());
                } else {
                    //jedis.persist(key.getBytes());
                }
            }
        } catch (IOException ex) {
            throw new BaseRuntimeException("set redis value happen error!", ex);
        }
    }

    @Override
    public String getCacheName() {
        return this.cacheName;
    }

    @Override
    public void remove(String key) {
        jedis.del(key);
    }

    @Override
    public List<String> getKeys() {
        List<String> keys = new ArrayList<String>();
        keys.addAll(jedis.keys("*"));
        return keys;
    }
}
