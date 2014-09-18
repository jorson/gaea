package gaea.foundation.core.cache.support;

import gaea.foundation.core.cache.CacheItemResult;
import gaea.foundation.core.utils.collection.CollectionFilter;

/**
 * 命中Cache对象的过滤器
 *
 * @author wuhy
 */
public class HitCacheCollectionFilter implements CollectionFilter {

    private static HitCacheCollectionFilter instance = new HitCacheCollectionFilter();

    @Override
    public boolean filter(Object data) {
        CacheItemResult item = (CacheItemResult) data;
        return item.isHit();
    }

    public static HitCacheCollectionFilter getInstance() {
        return instance;
    }
}
