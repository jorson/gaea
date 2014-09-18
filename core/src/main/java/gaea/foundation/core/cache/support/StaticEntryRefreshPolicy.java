package gaea.foundation.core.cache.support;

/**
 * 静态的缓存元素刷新策略
 * <p/>
 * 静态策略将不会自动刷新缓存内容
 *
 * @author wuhy
 */
public class StaticEntryRefreshPolicy implements EntryRefreshPolicy {

    private StaticEntryRefreshPolicy() {

    }

    public boolean isTimeout() {
        return false;
    }

    public int getTimes() {
        return -1;
    }

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
     */
    private static class StaticEntryRefreshPolicyHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static StaticEntryRefreshPolicy instance = new StaticEntryRefreshPolicy();
    }

    /**
     * 延迟加载StaticEntryRefreshPolicy对象
     *
     * @return
     */
    public static StaticEntryRefreshPolicy getInstance() {
        return StaticEntryRefreshPolicyHolder.instance;
    }
}