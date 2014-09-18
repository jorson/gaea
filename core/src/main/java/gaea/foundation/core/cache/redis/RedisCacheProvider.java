package gaea.foundation.core.cache.redis;

import gaea.foundation.core.cache.AbstractCacheProvider;
import gaea.foundation.core.cache.Cache;
import gaea.foundation.core.cache.CacheProvider;
import gaea.foundation.core.cache.redis.config.RedisPoolManager;
import gaea.foundation.core.cache.support.CacheException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Redis缓存对象提供者
 *
 * @author wuhy
 */
public class RedisCacheProvider extends AbstractCacheProvider {

    /**
     * Logger对象
     */
    private static Log logger = LogFactory.getLog(RedisCacheProvider.class);

    private static final int RETRY_NUM = 5;

    private RedisPoolManager manager;

    protected RedisCacheProvider() {
        init();
    }

    private void init() {
        try {
            manager = RedisPoolManager.getInstance();
        } catch (Exception ce) {
            logger.error("Redis pool could not be initialized", ce);
            throw new CacheException(ce);
        }
    }

    @Override
    protected Cache doCreateCache(String cacheName, boolean emptySupport) {
        return new RedisCache(getJedis(cacheName), cacheName, emptySupport);
    }

    /**
     * 获取Redis实例.
     *
     * @return Redis工具类实例
     */
    protected Jedis getJedis(String cacheName) {
        if (!manager.poolExists(cacheName)) {
            try {
                manager.addRedisPool(cacheName);
            } catch (Exception ce) {
                logger.error(ce, ce);
                throw new CacheException(ce);
            }
        }
        JedisPool pool = manager.getRedisPool(cacheName);
        return pool.getResource();
    }

    //    /**
//     * 释放redis实例到连接池.
//     *
//     * @param jedis redis实例
//     */
//    public void closeJedis(Jedis jedis) {
//        if (jedis != null) {
//            getPool(RedisConfiguration.Instance.getProperty(Constants.REDIS_HOST),
//                    RedisConfiguration.Instance.getIntProperty(Constants.REDIS_PORT)).returnResource(jedis);
//        }
//    }
//
//    /**
//     * 释放redis实例到连接池.
//     *
//     * @param jedis redis实例
//     */
//    public void closeJedis(Jedis jedis, String ip, int port) {
//        if (jedis != null) {
//            getPool(ip, port).returnResource(jedis);
//        }
//    }
    public static CacheProvider createProvider() {
        return new RedisCacheProvider();
    }
}
