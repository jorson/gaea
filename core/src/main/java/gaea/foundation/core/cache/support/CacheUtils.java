package gaea.foundation.core.cache.support;

import gaea.foundation.core.cache.CacheItem;
import gaea.foundation.core.cache.CacheItemResult;
import gaea.foundation.core.cache.EmptyData;
import gaea.foundation.core.utils.CollectionUtils;

import java.util.*;

/**
 * 缓存工具类
 *
 * @author wuhy
 */
public class CacheUtils {

    /**
     * 取得CacheItemResult中所有没有命中的Key
     *
     * @param items
     * @return
     */
    public static List<String> getMissingKeys(List items) {
        return items == null ? new ArrayList<String>() : (List<String>) CollectionUtils.select(
                CollectionUtils.where(items, MissingCacheCollectionFilter.getInstance()), CacheKeyCollectionSelector.getInstance());
    }

    /**
     * 取得CacheItemResult中所有命中的Key
     *
     * @param items
     * @return
     */
    public static List<String> getHitKeys(List items) {
        return (List<String>) CollectionUtils.select(getHitItems(items), CacheKeyCollectionSelector.getInstance());
    }

    /**
     * 取得CacheItemResult中所有命中的缓存对象
     *
     * @param items
     * @return
     */
    public static List<CacheItemResult> getHitItems(List items) {
        return items == null ? new ArrayList<CacheItemResult>() : (List<CacheItemResult>) CollectionUtils.where(items, HitCacheCollectionFilter.getInstance());
    }

    /**
     * 把CacheItemResult列表转换成CacheItem列表
     *
     * @param datas
     * @return
     */
    public static List<CacheItem> resultList2ItemList(Iterable<CacheItemResult> datas) {
        List<CacheItem> results = new ArrayList<CacheItem>();
        for (Iterator<CacheItemResult> iterator = datas.iterator(); iterator.hasNext(); ) {
            CacheItemResult result = iterator.next();
            results.add(result.getCacheItem());
        }
        return results;
    }

    /**
     * 合并两个Result的列表，目标列表会覆盖源列表中的值
     *
     * @param sources
     * @param dests
     * @return
     */
    public static List<CacheItemResult> merge(List<CacheItemResult> sources, List<CacheItemResult> dests) {
        Map<String, CacheItemResult> results = toMap(sources);
        for (int i = 0; i < dests.size(); i++) {
            CacheItemResult destItem = dests.get(i);
            CacheItemResult srcItem = results.get(destItem.getKey());
            srcItem.setValue(destItem.getValue() == null && destItem.isHit() ? EmptyData.value : destItem.getValue());
        }
        return sources;
    }

    /**
     * 把列表转换成Map
     *
     * @param datas
     * @return
     */
    public static Map<String, CacheItemResult> toMap(List<CacheItemResult> datas) {
        Map<String, CacheItemResult> results = new HashMap<String, CacheItemResult>();
        for (int i = 0; i < datas.size(); i++) {
            CacheItemResult cacheItemResult = datas.get(i);
            results.put(cacheItemResult.getKey(), cacheItemResult);
        }
        return results;
    }

}
