package gaea.foundation.core.cache.support;

/**
 * 缓存元素刷新策略接口
 *
 * @author wuhy
 */
public interface EntryRefreshPolicy {
    /**
     * 是否过期
     *
     * @return
     */
    boolean isTimeout();

    /**
     * 取得过期时间
     *
     * @return
     */
    int getTimes();
}
