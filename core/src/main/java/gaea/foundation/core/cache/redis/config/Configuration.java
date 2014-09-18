package gaea.foundation.core.cache.redis.config;

import gaea.foundation.core.utils.xstream.XStreamBuilder;
import com.thoughtworks.xstream.XStream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请在这里输入说明
 *
 * @author wuhy
 */
public class Configuration {
    private final Map<String, RedisConfiguration> redisConfigurations = new ConcurrentHashMap<String, RedisConfiguration>();

    private RedisConfiguration defaultRedisConfiguration = null;

    private static final XStream xStream;

    static {
        XStreamBuilder builder = new XStreamBuilder();
        Map<String, Class> aliasClassMap = new HashMap<String, Class>();
        aliasClassMap.put("configuration", List.class);
        aliasClassMap.put("redis", RedisConfiguration.class);
        builder.addAliases(aliasClassMap);
        xStream = builder.build();
        xStream.useAttributeFor("name", String.class);
        xStream.useAttributeFor("isDefault", Boolean.class);
    }

    public Configuration() {
    }

    public void parseRedisConfiguration(String xmlString) {
        List<RedisConfiguration> rcs = (List<RedisConfiguration>) xStream.fromXML(xmlString);
        for (int i = 0; i < rcs.size(); i++) {
            RedisConfiguration redisConfiguration = rcs.get(i);
            redisConfigurations.put(redisConfiguration.getName(), redisConfiguration);
            if (redisConfiguration.isDefault()) {
                defaultRedisConfiguration = redisConfiguration;
            }
        }
        if (defaultRedisConfiguration == null && rcs.size() > 0) {
            defaultRedisConfiguration = rcs.get(0);
        }
    }

    public RedisConfiguration getDefaultRedisConfiguration() {
        return defaultRedisConfiguration;
    }

    public void setDefaultRedisConfiguration(RedisConfiguration defaultRedisConfiguration) {
        this.defaultRedisConfiguration = defaultRedisConfiguration;
    }

    public Map<String, RedisConfiguration> getRedisConfigurations() {
        return redisConfigurations;
    }

}
