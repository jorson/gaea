package gaea.foundation.core.cache.support;

/**
 * 特定时间值缓存刷新策略，当经过特定时间时，过期
 *
 * @author wuhy
 */
public class ExpiresRefreshPolicy implements EntryRefreshPolicy {
    private int times;
    private long lastUpdate;

    /**
     * @param secs 秒
     */
    public ExpiresRefreshPolicy(int secs) {
        this.times = secs;
        update();
    }

    /**
     * 当更新后，缓存刷新时间从头开始算起
     */
    public void update() {
        this.lastUpdate = System.currentTimeMillis();
    }

    /**
     * 是否过期
     *
     * @return
     */
    public boolean isTimeout() {
        return (lastUpdate + times * 1000) < System.currentTimeMillis();
    }

    public int getTimes() {
        return times;
    }
}