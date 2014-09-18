package gaea.foundation.core.cache.support;

import gaea.foundation.core.cache.CacheItemResult;
import gaea.foundation.core.utils.collection.CollectionSelector;

/**
 * 缓存对象中键的选择器
 *
 * @author wuhy
 */
public class CacheKeyCollectionSelector implements CollectionSelector {

    private static CacheKeyCollectionSelector instance = new CacheKeyCollectionSelector();

    @Override
    public Object select(Object data) {
        CacheItemResult item = (CacheItemResult) data;
        return item.getKey();
    }

    public static CacheKeyCollectionSelector getInstance() {
        return instance;
    }
}
