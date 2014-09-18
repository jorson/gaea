package gaea.foundation.core.cache.support;

import gaea.foundation.core.cache.CacheItemResult;
import gaea.foundation.core.utils.collection.CollectionSelector;


/**
 * 缓存对象中值的选择器
 *
 * @author wuhy
 */
public class CacheValueCollectionSelector implements CollectionSelector {

    private static CacheValueCollectionSelector instance = new CacheValueCollectionSelector();

    @Override
    public Object select(Object data) {
        CacheItemResult item = (CacheItemResult) data;
        return item.getValue();
    }

    public static CacheValueCollectionSelector getInstance() {
        return instance;
    }
}
