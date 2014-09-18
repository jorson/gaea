package gaea.foundation.core.cache.redis.config;

import gaea.foundation.core.cache.support.CacheException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请在这里输入说明
 *
 * @author wuhy
 */
public class RedisPoolManager {
    /**
     * Logger对象
     */
    private static final Log logger = LogFactory.getLog(RedisPoolManager.class);
    private static RedisPoolManager instance = null;
    private final Map<String, JedisPool> redisPools = new ConcurrentHashMap<String, JedisPool>();

    private Configuration configuration = null;

    protected RedisPoolManager() {
        init();
    }

    public void init() {
        try {
            configuration = ConfigurationFactory.parseConfiguration();
            createRedisPools();
        } catch (Exception ex) {
            logger.error("初始化Redis出错");
        }
    }

    /**
     * 创建已经配置好的RedisPool
     *
     * @return
     */
    public final void createRedisPools() {
        synchronized (RedisPoolManager.class) {
            Set cacheConfigurations = configuration.getRedisConfigurations().entrySet();
            for (Iterator iterator = cacheConfigurations.iterator(); iterator.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iterator.next();
                RedisConfiguration redisConfiguration = (RedisConfiguration) entry.getValue();
                if (!redisPools.containsKey(redisConfiguration.getName())) {
                    JedisPool pool = createPool(redisConfiguration);
                    redisPools.put(redisConfiguration.getName(), pool);
                }
            }
        }
    }


    /**
     * 创建已经配置好的RedisPool
     *
     * @return
     */
    public final JedisPool addRedisPool(String name) {
        synchronized (RedisPoolManager.class) {
            if (redisPools.containsKey(name)) {
                throw new CacheException("已经存在该Redis缓存");
            }
            RedisConfiguration redisConfiguration = configuration.getRedisConfigurations().get(name);
            if (redisConfiguration == null) {
                redisConfiguration = configuration.getDefaultRedisConfiguration();
            }
            if (redisConfiguration == null) {
                throw new CacheException("未读取到Redis的配置，无法创建");
            }
            JedisPool pool = createPool(redisConfiguration);
            redisPools.put(redisConfiguration.getName(), pool);
            return pool;
        }
    }

    /**
     * 取得Redis池
     *
     * @param name
     * @return
     */
    public final JedisPool getRedisPool(String name) {
        return redisPools.get(name);
    }

    /**
     * 是否存在Pool
     *
     * @param name
     * @return
     */
    public final boolean poolExists(String name) {
        return redisPools.containsKey(name);
    }

    /**
     * 获取连接池.
     *
     * @return 连接池实例
     */
    private JedisPool createPool(RedisConfiguration redisConfiguration) {
        JedisPool pool = null;
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(redisConfiguration.getMaxTotal());
        config.setMaxIdle(redisConfiguration.getMaxIdle());
        config.setMinIdle(redisConfiguration.getMinIdle());
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        /**
         *如果你遇到 java.net.SocketTimeoutException: Read timed out exception的异常信息
         *请尝试在构造JedisPool的时候设置自己的超时值. JedisPool默认的超时时间是2秒(单位毫秒)
         */
        pool = new JedisPool(config, redisConfiguration.getHost(), redisConfiguration.getPort(),
                redisConfiguration.getTimeout(), redisConfiguration.getPassword(),
                redisConfiguration.getDatabase(), redisConfiguration.getClientName());
        return pool;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        createRedisPools();
    }

    /**
     * 延迟加载RedisPoolManager对象
     *
     * @return
     */
    public static RedisPoolManager getInstance() {
        if (instance == null || instance.configuration == null) {
            instance = new RedisPoolManager();
        }
        return instance;
    }
}
