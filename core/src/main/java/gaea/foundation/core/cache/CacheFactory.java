package gaea.foundation.core.cache;

import gaea.foundation.core.exception.BaseRuntimeException;
import gaea.foundation.core.utils.ClassUtils;
import gaea.foundation.core.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存工厂类
 * <p/>
 * 用于保存在系统中使用到的缓存对象，缓存对象生成必须使用基于CacheProvider的提供者
 */
public class CacheFactory {
    /**
     * Logger对象
     */
    private static final Log logger = LogFactory.getLog(CacheFactory.class);
    private final Object SYNC_OBJECT = new Object();
    private Map<String, Cache> caches = new HashMap<String, Cache>();
    private Map<String, CacheProvider> cacheProviders = new HashMap<String, CacheProvider>();

    private CacheFactory() {

    }

    /**
     * 取得Cache
     *
     * @param name
     * @return
     */
    public Cache getCache(String name) {
        if (!StringUtils.hasText(name)) {
            throw new BaseRuntimeException("缓存名称不能为空");
        }
        Cache cache = caches.get(name);
        if (cache == null) {
            // 如果还没有创建，则从配置中取到配置信息并创建
            synchronized (SYNC_OBJECT) {
                // TODO 取得设置
                boolean emptySupport = false;
                String providerName = "";
                CacheProvider cacheProvider = getCacheProvider(providerName);
                cache = cacheProvider.createCache(name, emptySupport);
                caches.put(name, cache);
            }
        }
        return cache;
    }

    /**
     * 取得缓存提供者
     *
     * @param providerName
     * @return
     */
    public CacheProvider getCacheProvider(String providerName) {
        if (!StringUtils.hasText(providerName)) {
            throw new BaseRuntimeException("缓存提供者名称不能为空");
        }
        CacheProvider provider = cacheProviders.get(providerName);
        if (provider == null) {
            synchronized (SYNC_OBJECT) {
                // 如果还没有创建，则先从Spring中读取，
                // 如果没有读取到，则把providerName当做Class Name创建，如果创建不成功，则返回null
                if (provider == null) {
                    provider = createCacheProvider(providerName);
                }
            }
        }
        return provider;
    }

    /**
     * 根据provider类的名称创建Provider
     *
     * @param providerName
     * @return
     */
    protected CacheProvider createCacheProvider(String providerName) {
        CacheProvider provider = null;
        try {
            Class clazz = ClassUtils.forName(providerName, CacheFactory.class.getClassLoader());
            provider = (CacheProvider) clazz.getDeclaredConstructor().newInstance();
            cacheProviders.put(providerName, provider);
        } catch (Exception e) {
            logger.warn("Create cache provider[" + providerName + "] error," + e.getMessage());
        }
        return provider;
    }

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
     */
    private static class CacheFactoryHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static CacheFactory instance = new CacheFactory();
    }

    /**
     * 延迟加载CacheFactory对象
     *
     * @return
     */
    public static CacheFactory getInstance() {
        return CacheFactoryHolder.instance;
    }
}
