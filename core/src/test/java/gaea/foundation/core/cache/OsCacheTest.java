package gaea.foundation.core.cache;

import gaea.foundation.core.cache.oscache.OsCacheProvider;
import gaea.foundation.core.utils.ArrayUtils;
import gaea.foundation.core.utils.CollectionUtils;
import gaea.foundation.core.utils.collection.CollectionFilter;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * 请在这里输入说明
 *
 * @author wuhy
 */
public class OsCacheTest {
    /**
     * Put、Get、Remove三个方法联合测试
     */
    @Test
    public void testPutGetRemove() {
        final Cache firstCache = getCache("Group_01", false);
        final Cache secondCache = getCache("Group_02", false);
        firstCache.put("cache_01", 100);
        firstCache.put("cache_02", "abc", 1); //1秒后过期
        firstCache.put("cache_03", "edf");    //
        firstCache.put("cache_04", "cache_04_01");    //
        firstCache.put("cache_05", null);    //
        secondCache.put("cache_01", 200);
        secondCache.put("cache_04", "secondCache");
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
                Assert.assertEquals("从缓存中取得与第一个缓存对象相同Key的值，返回值与比较值不一致", secondCache.get("cache_04"), "secondCache");
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
                Assert.assertNull("从缓存中取得过期的值，返回值不为NULL", firstCache.get("cache_02"));
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
        final Cache thirdCache = getCache("Group_03", true);
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
        final Cache firstCache = getCache("Group_04", false);
        final Cache secondCache = getCache("Group_05", true);
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
        final Cache firstCache = getCache("Group_06", false);
        final Cache secondCache = getCache("Group_07", true);
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
        final Cache firstCache = getCache("Group_08", false);
        final Cache secondCache = getCache("Group_09", true);
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
                firstCache.get("cache_02"); //先取一次，在上下文中保存
                Assert.assertArrayEquals("取得Key列表，键列表返回出错", firstCache.getKeys().toArray(),
                        new String[]{"cache_01", "cache_02", "cache_03"});
                sleepThread(1200);
                firstCache.get("cache_02");  //取得后会自动删除Key，从上下文中去取得
                Assert.assertArrayEquals("再次取得Key列表，键列表返回出错", firstCache.getKeys().toArray(),
                        new String[]{"cache_01", "cache_02", "cache_03"});
                Assert.assertArrayEquals("取得允许空缓存的Key列表，键列表返回出错", secondCache.getKeys().toArray(),
                        new String[]{"cache_01", "cache_02", "cache_03", "cache_04"});
            }
        }).start();
        sleepThread(1100);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Assert.assertArrayEquals("取得已过期的Key列表，键列表返回出错", firstCache.getKeys().toArray(),
                        new String[]{"cache_01", "cache_02", "cache_03"});
                sleepThread(300);
                firstCache.get("cache_02");  // 取得后会自动删除Key
                Assert.assertArrayEquals("再次已过期的取得Key列表，键列表返回出错", firstCache.getKeys().toArray(),
                        new String[]{"cache_01", "cache_03"});
            }
        }).start();
        sleepThread(1000);
    }


    @Test
    public void testClear() {
        final Cache firstCache = getCache("Group_10", false);
        firstCache.put("cache_01", 100);
        firstCache.put("cache_02", "abc", 1); //1秒后过期
        firstCache.put("cache_03", "edf");    //
        firstCache.put("cache_04", null);    //
        Assert.assertEquals("未清空缓存时，返回的值与比较值不一致", firstCache.get("cache_01"), 100);
        firstCache.clear();
        Assert.assertNull("清空缓存后，返回值不为NULL", firstCache.get("cache_01"));

    }

    protected void sleepThread(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Cache getCache(String cacheName, boolean emptySupport) {
        return OsCacheProvider.createProvider().createCache(cacheName, emptySupport);
    }
}
