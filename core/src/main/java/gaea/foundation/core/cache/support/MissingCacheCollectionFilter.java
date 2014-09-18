package gaea.foundation.core.cache.support;

import gaea.foundation.core.cache.CacheItemResult;
import gaea.foundation.core.utils.collection.CollectionFilter;

/**
 * 未命中Cache对象的过滤器
 *
 * @author wuhy
 */
public class MissingCacheCollectionFilter implements CollectionFilter {

    private static MissingCacheCollectionFilter instance = new MissingCacheCollectionFilter();

    @Override
    public boolean filter(Object data) {
        CacheItemResult item = (CacheItemResult) data;
        return item.isMissing();
    }

    public static MissingCacheCollectionFilter getInstance() {
        return instance;
    }
}
