package gaea.foundation.core.cache.oscache;

import gaea.foundation.core.cache.AbstractCacheProvider;
import gaea.foundation.core.cache.Cache;
import gaea.foundation.core.cache.CacheProvider;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

import java.util.Properties;

/**
 * OsCache缓存提供者
 */
public class OsCacheProvider extends AbstractCacheProvider {

    protected OsCacheProvider() {

    }

    /**
     * 创建内部使用Cache
     *
     * @return
     */
    protected Cache doCreateCache(String cacheName, boolean emptySupport) {
        Properties prop = new Properties();
        return new OsCache(new GeneralCacheAdministrator(prop), cacheName, emptySupport);
    }

    public static CacheProvider createProvider() {
        return new OsCacheProvider();
    }
}
