package gaea.foundation.core.cache.ehcache;


import gaea.foundation.core.cache.AbstractCacheProvider;
import gaea.foundation.core.cache.Cache;
import gaea.foundation.core.cache.CacheProvider;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * EhCache缓存提供者
 *
 * @author wuhy
 */
public class EhCacheProvider extends AbstractCacheProvider {
    /**
     * Logger对象
     */
    private static final Log logger = LogFactory.getLog(EhCacheProvider.class);

    private CacheManager manager;

    protected EhCacheProvider() {
        init();
    }

    public void init() {
        try {
            manager = CacheManager.create();
        } catch (CacheException ce) {
            logger.error("EhCache could not be initialized", ce);
            throw new gaea.foundation.core.cache.support.CacheException(ce);
        }
    }

    /**
     * 创建内部使用Cache
     *
     * @return
     */
    protected Cache doCreateCache(String cacheName, boolean emptySupport) {
        if (!manager.cacheExists(cacheName)) {
            try {
                manager.addCache(cacheName);
            } catch (CacheException ce) {
                logger.error(ce, ce);
                throw new gaea.foundation.core.cache.support.CacheException(ce);
            }
        }
        return new EhCache(manager.getCache(cacheName), cacheName, emptySupport);
    }

    public static CacheProvider createProvider() {
        return new EhCacheProvider();
    }
}
