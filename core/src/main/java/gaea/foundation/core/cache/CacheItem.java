package gaea.foundation.core.cache;

/**
 * 用于保存缓存的键和值
 *
 * @author wuhy
 */
public class CacheItem {
    /**
     * 缓存的键
     */
    private String key;
    /**
     * 缓存的值
     */
    private Object value;

    public CacheItem(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
