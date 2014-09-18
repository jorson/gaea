package gaea.foundation.core.cache;

/**
 * 用于存储缓存对象的结果
 * <p/>
 * 包括本次缓存是否命中，有存在但为空值
 */
public class CacheItemResult {
    private CacheItem cacheItem;
    private boolean hit;

    public CacheItemResult(String key) {
        this(key, null);
    }

    public CacheItemResult(String key, Object value) {
        this.cacheItem = new CacheItem(key, value);
        hit = value != null;
    }

    public String getKey() {
        return this.cacheItem.getKey();
    }

    public Object getValue() {
        if (isEmpty() || isMissing()) {
            return null;
        }
        return this.cacheItem.getValue();
    }

    public void setValue(Object value) {
        this.cacheItem.setValue(value);
        hit = value != null;
    }

    /**
     * 是否存在于缓存中，如果支持空值缓存(Empty）并存在,该值为 true
     */
    public boolean isHit() {
        return hit;
    }

    /**
     * 是否不存在于缓存中，如果支持空值缓存(Empty）并存在,该值为 false
     *
     * @return
     */
    public boolean isMissing() {
        return !hit;
    }

    /**
     * 是否存在于缓存中，如果支持空值缓存(Empty）并存在,该值为 true
     *
     * @return
     */
    public boolean hasData() {
        return hit && cacheItem.getValue() != null && cacheItem.getValue() != EmptyData.value;
    }

    /**
     * 是否为空值，即数据在数据源中不存在
     */
    public boolean isEmpty() {
        return cacheItem.getValue() == null || cacheItem.getValue().equals(EmptyData.value);
    }

    /**
     * 设置空值，表示在缓存已经存在，但取到的是空值
     */
    public void setEmpty() {
        cacheItem.setValue(EmptyData.value);
        hit = true;
    }

    public CacheItem getCacheItem() {
        return cacheItem;
    }
}
