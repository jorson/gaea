package gaea.foundation.core.cache;

import gaea.foundation.core.cache.support.*;
import gaea.foundation.core.context.WorkContext;
import gaea.foundation.core.utils.ArrayUtils;
import gaea.foundation.core.utils.CollectionUtils;
import gaea.foundation.core.utils.StringUtils;
import gaea.foundation.core.utils.Utils;
import gaea.foundation.core.utils.collection.CollectionFilter;

import java.util.*;

/**
 * 抽象缓存对象的实现，
 * 实现一些通用的方法
 *
 * @author wuhy
 */
public abstract class AbstractCache implements Cache {

    /**
     * 默认的缓存前缀
     */
    public static final String CACHEKEY_PRE = "__CACHE__";

    /**
     * 是否已经初始化
     */
    private boolean inited = false;

    /**
     * 是否支持空值缓存
     */
    private boolean emptySupport = false;

    /**
     * 从缓存中取得值
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        List<Object> datas = getBatch(new String[]{key});
        if (datas != null && datas.size() > 0) {
            return datas.get(0);
        }
        return null;
    }

    /**
     * 从缓存中批量取得值
     *
     * @param keys
     * @return
     */
    public List<Object> getBatch(String[] keys) {
        return getBatch(ArrayUtils.asList(keys));
    }

    /**
     * 从缓存中批量取得值
     *
     * @param keys
     * @return
     */
    public List<Object> getBatch(Iterable<String> keys) {
        return (List<Object>) CollectionUtils.select(CollectionUtils.where(getBatchResult(keys), new CollectionFilter() {
            @Override
            public boolean filter(Object data) {
                CacheItemResult item = (CacheItemResult) data;
                return item.hasData();
            }
        }), CacheValueCollectionSelector.getInstance());
    }

    /**
     * 从缓存中批量取得值，包括状态
     *
     * @param keys
     * @return
     */
    public List<CacheItemResult> getBatchResult(Iterable<String> keys) {
        ensureInit();
        // 从上下文获取
        List<CacheItemResult> results = this.getContext(keys);  //返回的为List<CacheItemResult>
        //int contextHits = results.GetHitKeys().Count();
        // 从缓存获取上下文缺失的对象
        List<CacheItemResult> innerResults = getBatchResultInner(CacheUtils.getMissingKeys(results));
        // 将缓存获取到的对象写入上下文
        List<CacheItemResult> innerHits = CacheUtils.getHitItems(innerResults);
        if (innerHits.size() > 0) {
            CacheUtils.merge(results, innerHits);
            setContext(CacheUtils.resultList2ItemList(results));
        }
        //处理CacheEvent
        return results;
    }

    /**
     * 使用默认的时间策略，在缓存中插入值
     *
     * @param key   键
     * @param value 值
     */
    @Override
    public void put(String key, Object value) {
        put(key, value, null);
    }

    /**
     * 使用特定的过期时间，在缓存中插入值
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间
     */
    @Override
    public void put(String key, Object value, int timeout) {
        put(key, value, new ExpiresRefreshPolicy(timeout));
    }

    /**
     * 使用特定的时间策略，在缓存中插入值
     *
     * @param key    键
     * @param value  值
     * @param policy 过期策略
     */
    public void put(String key, Object value, EntryRefreshPolicy policy) {
        putBatch(new CacheItem[]{new CacheItem(key, value)}, policy);
    }

    /**
     * 使用默认的时间策略，在缓存中插入批量的值
     *
     * @param items 批量数据
     * @return
     */
    public void putBatch(CacheItem[] items) {
        putBatch(items, null);
    }

    /**
     * 使用特定的过期时间，在缓存中插入批量的值
     *
     * @param items   批量数据
     * @param timeout 过期时间(秒)
     * @return
     */
    public void putBatch(CacheItem[] items, int timeout) {
        putBatch(items, new ExpiresRefreshPolicy(timeout));
    }

    /**
     * 使用特定的时间策略，在缓存中插入批量的值
     *
     * @param items  批量数据
     * @param policy 过期策略
     * @return
     */
    public void putBatch(CacheItem[] items, EntryRefreshPolicy policy) {
        putBatch(ArrayUtils.asList(items), policy);
    }

    /**
     * 使用默认的时间策略，在缓存中插入批量的值
     *
     * @param items 批量数据
     * @return
     */
    public void putBatch(Iterable<CacheItem> items) {
        putBatch(items, null);
    }

    /**
     * 使用特定的过期时间，在缓存中插入批量的值
     *
     * @param items   批量数据
     * @param timeout 过期时间(秒)
     * @return
     */
    public void putBatch(Iterable<CacheItem> items, int timeout) {
        putBatch(items, new ExpiresRefreshPolicy(timeout));
    }

    /**
     * 使用特定的时间策略，在缓存中插入批量的值
     *
     * @param items  批量数据
     * @param policy 过期策略
     * @return
     */
    public void putBatch(Iterable<CacheItem> items, EntryRefreshPolicy policy) {
        ensureInit();
        List<CacheItem> outputs = new ArrayList<CacheItem>();
        //处理空值
        int count = 0;
        for (Iterator<CacheItem> iterator = items.iterator(); iterator.hasNext(); ) {
            CacheItem cacheItem = iterator.next();
            count++;
            if (cacheItem.getValue() == null) {
                if (!this.isEmptySupport()) {  //如果不支持null，则不保存null
                    continue;
                }
                cacheItem.setValue(EmptyData.value);
            }
            outputs.add(cacheItem);
        }

        if (outputs.size() > 0) {
            // 保存到上下文
            this.setContext(items);
            // 保存到缓存
            putBatchInner(outputs, policy);
        }
        if (count > 0) {
            //处理CacheEvent
        }
    }

    /**
     * 初始化缓存
     */
    protected void initCache() {
        if (StringUtils.isEmpty(this.getCacheName())) {
            throw new IllegalArgumentException("缓存名称不能为空。");
        }
        inited = true;
    }

    /**
     * 确认是否初始化
     * <p/>如果没有初始化，则进行初始化操作
     */
    protected void ensureInit() {
        if (!inited) {
            initCache();
        }
    }

    /**
     * 刷新先直接调用清除方法，有的Cache不提供刷新过期方法
     */
    public void flush() {
        this.clear();
    }

    /**
     * 删除缓存中所有的值
     */
    public void clear() {
        clearInner();
        clearContext();
        //TODO 发布事件
    }

    /**
     * 取得WorkContext里面的值
     *
     * @param keys
     * @return
     */
    protected List<CacheItemResult> getContext(Iterable<String> keys) {
        List<CacheItemResult> results = new ArrayList<CacheItemResult>();
        Map datas = (Map) WorkContext.getValue(CACHEKEY_PRE + getCacheName());
        //if (datas == null || datas.size() == 0) return results;
        for (Iterator<String> iterator = keys.iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            Object data = Utils.getMapValue(datas, key);
            CacheItemResult cir = new CacheItemResult(key, data);
            results.add(cir);
        }
        return results;
    }

    /**
     * 设置WorkContext缓存中的值
     *
     * @param results
     */
    protected synchronized void setContext(Iterable<CacheItem> results) {
        Map datas = (Map) WorkContext.getValue(CACHEKEY_PRE + getCacheName());
        if (datas == null) datas = new HashMap();
        for (Iterator<CacheItem> iterator = results.iterator(); iterator.hasNext(); ) {
            CacheItem result = iterator.next();
            datas.put(result.getKey(), result.getValue());
        }
        WorkContext.setValue(CACHEKEY_PRE + getCacheName(), datas);
    }

    /**
     * 清空WorkContext缓存中所对应的值
     */
    protected synchronized void clearContext() {
        WorkContext.setValue(CACHEKEY_PRE + getCacheName(), new HashMap());
    }

    protected boolean isInited() {
        return inited;
    }

    /**
     * 取得是否支持空值缓存
     *
     * @return
     */
    public boolean isEmptySupport() {
        return this.emptySupport;
    }

    /**
     * 设置是否支持空值缓存
     */
    public void setEmptySupport(boolean value) {
        if (isInited()) {
            throw new CacheException("在初始化后无法变更设置 EmptySupport。");
        }
        this.emptySupport = value;
    }

    /**
     * 清空从缓存中的值
     *
     * @return
     */
    protected abstract void clearInner();

    /**
     * 从缓存中批量取得值，如果没有命中，则CacheItemResult里面的值为null且Hit为false
     *
     * @param keys
     * @return
     */
    protected abstract List<CacheItemResult> getBatchResultInner(Iterable<String> keys);

    /**
     * 使用特定的时间策略，在缓存中插入批量的值，特定的缓存处理
     *
     * @param items  批量数据
     * @param policy 过期策略
     */
    protected abstract void putBatchInner(Iterable<CacheItem> items, EntryRefreshPolicy policy);
}
