package gaea.foundation.core.cache;

import gaea.foundation.core.cache.redis.RedisCache;
import gaea.foundation.core.cache.redis.config.Configuration;
import gaea.foundation.core.cache.redis.config.RedisConfiguration;
import gaea.foundation.core.cache.redis.config.RedisPoolManager;
import gaea.foundation.core.utils.ArrayUtils;
import gaea.foundation.core.utils.CollectionUtils;
import gaea.foundation.core.utils.collection.CollectionFilter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * 请在这里输入说明
 *
 * @author wuhy
 */
public class RedisCacheTest {

    JedisPool redisPool = null;
    JedisPool secondRedisPool = null;

    @Before
    public void init() {
        Configuration configuration = mock(Configuration.class);
        Map<String, RedisConfiguration> configurationMap = new HashMap<String, RedisConfiguration>();
        RedisConfiguration rc = new RedisConfiguration();
        rc.setName("redisDefault");
        rc.setHost("192.168.205.3");
        rc.setPort(6379);
        rc.setDatabase(3);
        configurationMap.put("redisDefault", rc);
        RedisConfiguration rc_02 = new RedisConfiguration();
        rc_02.setName("redisRead");
        rc_02.setHost("192.168.205.3");
        rc_02.setPort(6379);
        rc_02.setDatabase(4);
        configurationMap.put("redisRead", rc_02);
        when(configuration.getRedisConfigurations()).thenReturn(configurationMap);
        //System.out.println(configuration.getRedisConfigurations());
        RedisPoolManager manager = RedisPoolManager.getInstance();
        manager.setConfiguration(configuration);
        redisPool = manager.getRedisPool("redisDefault");
        secondRedisPool = manager.getRedisPool("redisRead");
    }


    /**
     * Put、Get、Remove三个方法联合测试
     */
    @Test
    public void testPutGetRemove() {
        final Cache firstCache = getCache(false, "redisDefault", false);
        firstCache.clear();
        final Cache secondCache = getCache(true, "redisRead", false);
        secondCache.clear();
        firstCache.put("cache_01", 100);
        firstCache.put("cache_02", "abc", 1); //1秒后过期
        firstCache.put("cache_03", "edf");    //
        firstCache.put("cache_04", "cache_04_01");    //
        firstCache.put("cache_05", null);    //
        secondCache.put("cache_01", 200);
        secondCache.put("cache_04", "1100");
        secondCache.put("second_cache_04", "secondCache");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Assert.assertEquals("设置数字并从缓存中取值，返回值与比较值不一致", firstCache.get("cache_01"), 100);
                Assert.assertEquals("从缓存中取得未过期的值，返回值与比较值不一致", firstCache.get("cache_02"), "abc");
                Assert.assertNull("从缓存中取得NULL的值，返回值不为NULL", firstCache.get("cache_05"));
                Assert.assertNull("从缓存中取得不存在的值，返回值不为NULL", firstCache.get("cache_noexist"));
                sleepThread(300);
                firstCache.put("cache_03", "thread_01");
                firstCache.remove("cache_04");    //
                Assert.assertNull("从缓存中取得已经删除的值，返回值不为NULL", firstCache.get("cache_04"));
                Assert.assertEquals("从缓存中取得与第一个缓存对象相同Key的值，返回值与比较值不一致", secondCache.get("cache_04"), "1100");
            }
        }).start();
        sleepThread(100);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Assert.assertEquals("从缓存中取得未设置的值，返回值与比较值不一致", firstCache.get("cache_03"), "edf");
                sleepThread(500);
                Assert.assertEquals("从缓存中取得被另一个线程设置的值，返回值与比较值不一致", firstCache.get("cache_03"), "edf");
            }
        }).start();
        sleepThread(1000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Assert.assertNull("从缓存中取得过期的值，返回值为NULL", firstCache.get("cache_02"));  //
                Assert.assertEquals("从缓存中取得被另一个线程设置的值，返回值与比较值不一致", firstCache.get("cache_03"), "thread_01");
            }
        }).start();
        sleepThread(1000);
    }


    /**
     * 测试空值支持的代码
     */
    @Test
    public void testEmptySupport() {
        final Cache thirdCache = getCache(false, "Group_03", true);
        thirdCache.clear();
        thirdCache.put("cache_01", "");    //
        thirdCache.put("cache_02", null);
        thirdCache.put("cache_03", "abc", 1); //1秒后过期
        thirdCache.put("cache_04", "cache_04_01");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Assert.assertEquals("设置空并从缓存中取值，返回值与比较值不一致", thirdCache.get("cache_01"), "");
                Assert.assertNull("设置NULL并从缓存中取值，返回值与比较值不一致", thirdCache.get("cache_02"));
                Assert.assertNull("从缓存中取得不存在的值，返回值不为NULL", thirdCache.get("cache_noexist"));
                // 再取一次，值未变化
                Assert.assertNull("从缓存中取得不存在的值，返回值不为NULL", thirdCache.get("cache_noexist"));
                thirdCache.put("cache_05", null);
            }
        }).start();
        sleepThread(1020);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Assert.assertNull("从缓存中取得被另一个线程设置的NULL值，返回值与比较值不一致", thirdCache.get("cache_05"));
                Assert.assertNull("从缓存中取得过期的值，返回值不为NULL", thirdCache.get("cache_03"));
            }
        }).start();
        sleepThread(1000);
    }

    /**
     * 测试批量处理的代码
     */
    @Test
    public void testGetPutBatch() {
        final Cache firstCache = getCache(false, "Group_04", false);
        firstCache.clear();
        final Cache secondCache = getCache(true, "Group_05", true);
        secondCache.clear();
        firstCache.put("cache_01", 100);
        firstCache.put("cache_02", "abc", 1); //1秒后过期
        firstCache.put("cache_03", "edf");    //
        firstCache.put("cache_04", "cache_04_01", 1);    //
        firstCache.put("cache_05", null);    //
        secondCache.put("cache_01", 200);
        secondCache.put("cache_02", "abc_02", 5); //5秒后过期
        secondCache.put("cache_03", null);    //
        secondCache.put("cache_04", "secondCache");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Assert.assertArrayEquals("从缓存中取得多个Key的值，包括NULL，返回值与比较值不一致", firstCache.getBatch(
                        new String[]{"cache_01", "cache_02", "cache_noexist", "cache_03", "cache_05"}).toArray(), new Object[]{100, "abc", "edf"});
                Assert.assertArrayEquals("从支持空的缓存中取得多个Key的值，包括NULL，返回值与比较值不一致", secondCache.getBatch(
                        new String[]{"cache_01", "cache_02", "cache_noexist", "cache_03", "cache_04"}).toArray(), new Object[]{200, "abc_02", "secondCache"});
                sleepThread(300);
                firstCache.putBatch(new CacheItem[]{new CacheItem("cache_01", "str"), new CacheItem("cache_02", "10000"),
                        new CacheItem("cache_03", null), new CacheItem("cache_05", 500)});
                Assert.assertArrayEquals("设置多个值后，从缓存中取得多个Key的值，包括NULL，返回值与比较值不一致", firstCache.getBatch(
                        new String[]{"cache_01", "cache_02", "cache_noexist", "cache_03", "cache_05"}).toArray(), new Object[]{"str", "10000", "edf", 500});
            }
        }).start();
        sleepThread(100);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Assert.assertArrayEquals("从缓存中取得多个Key的值，包括NULL，返回值与比较值不一致", firstCache.getBatch(
                        new String[]{"cache_01", "cache_02", "cache_noexist", "cache_03", "cache_05"}).toArray(), new Object[]{100, "abc", "edf"});
                sleepThread(500);
                Assert.assertArrayEquals("从缓存中取得被另一个线程设置的值，返回值与比较值不一致", firstCache.getBatch(
                        new String[]{"cache_01", "cache_02", "cache_noexist", "cache_03", "cache_05"}).toArray(), new Object[]{100, "abc", "edf", 500});
            }
        }).start();
        sleepThread(1000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Assert.assertArrayEquals("从缓存中取得被另一个线程设置的值，包括NULL和过期的KEY，返回值与比较值不一致", firstCache.getBatch(
                        new String[]{"cache_01", "cache_02", "cache_04", "cache_noexist", "cache_03", "cache_05"}).toArray(), new Object[]{"str", "10000", "edf", 500});
            }
        }).start();
        sleepThread(1000);
    }

    /**
     * 测试取得状态
     */
    @Test
    public void testGetBatchResult() {
        final Cache firstCache = getCache(false, "Group_06", false);
        firstCache.clear();
        final Cache secondCache = getCache(true, "Group_07", true);
        secondCache.clear();
        firstCache.put("cache_01", 100);
        firstCache.put("cache_02", "abc", 1); //1秒后过期
        firstCache.put("cache_03", "edf");    //
        firstCache.put("cache_04", "cache_04_01", 1);    //
        firstCache.put("cache_05", null);    //
        secondCache.put("cache_01", 200);
        secondCache.put("cache_02", "abc_02", 1); //5秒后过期
        secondCache.put("cache_03", null);    //
        secondCache.put("cache_04", "secondCache");
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<CacheItemResult> results = firstCache.getBatchResult(ArrayUtils.asList("cache_01", "cache_02", "cache_noexist", "cache_03", "cache_05"));
                Assert.assertEquals("从缓存中取得多个Key的值，包括NULL，命中数出错", CollectionUtils.where(results, new CollectionFilter() {
                    @Override
                    public boolean filter(Object data) {
                        CacheItemResult cir = (CacheItemResult) data;
                        return cir.isHit();
                    }
                }).size(), 3);
                results = secondCache.getBatchResult(ArrayUtils.asList("cache_01", "cache_02", "cache_noexist", "cache_03", "cache_04"));
                Assert.assertEquals("从允许空缓存中取得多个Key的值，包括NULL，命中数出错", CollectionUtils.where(results, new CollectionFilter() {
                    @Override
                    public boolean filter(Object data) {
                        CacheItemResult cir = (CacheItemResult) data;
                        return cir.isHit();
                    }
                }).size(), 4);
            }
        });
        sleepThread(1020);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<CacheItemResult> results = firstCache.getBatchResult(ArrayUtils.asList("cache_01", "cache_02", "cache_noexist", "cache_03", "cache_04"));
                Assert.assertEquals("从缓存中取得多个Key的值，包括NULL和过期的Key，命中数出错", CollectionUtils.where(results, new CollectionFilter() {
                    @Override
                    public boolean filter(Object data) {
                        CacheItemResult cir = (CacheItemResult) data;
                        return cir.isHit();
                    }
                }).size(), 2);
                results = secondCache.getBatchResult(ArrayUtils.asList("cache_01", "cache_02", "cache_noexist", "cache_03", "cache_04"));
                Assert.assertEquals("从允许空缓存中取得多个Key的值，包括NULL和过期的Key，命中数出错", CollectionUtils.where(results, new CollectionFilter() {
                    @Override
                    public boolean filter(Object data) {
                        CacheItemResult cir = (CacheItemResult) data;
                        return cir.isHit();
                    }
                }).size(), 3);
            }
        }).start();
        sleepThread(1000);
    }

    @Test
    public void testGetKeys() {
        final Cache firstCache = getCache(false, "Group_08", false);
        firstCache.clear();
        final Cache secondCache = getCache(true, "Group_09", true);
        secondCache.clear();
        sleepThread(100);
        firstCache.put("cache_01", 100);
        firstCache.put("cache_02", "abc", 1); //1秒后过期
        firstCache.put("cache_03", "edf");    //
        firstCache.put("cache_04", null);    //
        secondCache.put("cache_01", 200);
        secondCache.put("cache_02", "abc_02", 1); //5秒后过期
        secondCache.put("cache_03", null);    //
        secondCache.put("cache_04", "secondCache");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Assert.assertEquals("取得Key列表，键列表返回出错", firstCache.getKeys().size(), 3);
                sleepThread(1200);
                Assert.assertEquals("再次取得Key列表，键列表返回出错", firstCache.getKeys().size(), 2);
                Assert.assertEquals("取得允许空缓存的Key列表，键列表返回出错", secondCache.getKeys().size(), 3);
            }
        }).start();
        sleepThread(1100);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Assert.assertEquals("取得已过期的Key列表，键列表返回出错", firstCache.getKeys().size(), 2);
                sleepThread(300);
                Assert.assertEquals("再次已过期的取得Key列表，键列表返回出错", firstCache.getKeys().size(), 2);
            }
        }).start();
        sleepThread(1000);
    }


    @Test
    public void testClear() {
        final Cache firstCache = getCache(false, "Group_10", false);
        firstCache.put("cache_01", 100);
        firstCache.put("cache_02", "abc", 1); //1秒后过期
        firstCache.put("cache_03", "edf");    //
        firstCache.put("cache_04", null);    //
        Assert.assertEquals("未清空缓存时，返回的值与比较值不一致", firstCache.get("cache_01"), 100);
        firstCache.clear();
        Assert.assertNull("清空缓存后，返回值不为NULL", firstCache.get("cache_01"));
    }


    public Cache getCache(boolean second, String cacheName, boolean emptySupport) {
        if (second) {
            return new RedisCache(secondRedisPool.getResource(), cacheName, emptySupport);
        } else {
            return new RedisCache(redisPool.getResource(), cacheName, emptySupport);
        }
        //return new RedisCache(cacheName, emptySupport);
    }

    private void sleepThread(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
