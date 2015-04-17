package com.nd.gaea.utils;

import com.nd.gaea.utils.object.TestObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用工具类的测试类
 */
public class UtilsTest {
    private Map testData = null;
    private TestObject testObject = null;

    @Before
    public void init() {
        testData = new HashMap();
        testObject = new TestObject();
        testData.put("StringKey", "String");
        testData.put("StringIntKey", "200");
        testData.put("IntKey", 100);
        testData.put("LongKey", 9000000099L);
        testData.put("ObjectKey", testObject);
    }

    @Test
    public void testToBoolean() {
        Assert.assertTrue("转换TRUE字符串，返回值有误", Utils.toBoolean("true"));
        Assert.assertTrue("转换YES字符串，返回值有误", Utils.toBoolean("yeS"));
        Assert.assertTrue("转换OK字符串，返回值有误", Utils.toBoolean("OK"));
        Assert.assertTrue("转换1字符串，返回值有误", Utils.toBoolean("1"));
        Assert.assertTrue("转换无效字符串，返回值有误", !Utils.toBoolean("aaaa"));
        Assert.assertTrue("转换无效字符串，返回值有误", !Utils.toBoolean("0"));
        Assert.assertTrue("转换空字符串，返回值有误", !Utils.toBoolean(""));
        Assert.assertTrue("转换NULL字符串，返回值有误", !Utils.toBoolean(null));
    }

    @Test
    public void testToInt() {
        Assert.assertEquals("转换-1字符串，返回值有误", Utils.toInt("-1", 0), -1);
        Assert.assertEquals("转换-1.1字符串，返回值有误", Utils.toInt("-1.1", 0), -1);
        Assert.assertEquals("转换100a字符串，返回值有误", Utils.toInt("100a", 0), 100);
        Assert.assertEquals("转换100.9字符串，返回值有误", Utils.toInt("100.9", 0), 100);
        Assert.assertEquals("转换100字符串，返回值有误", Utils.toInt("100", 0), 100);
        Assert.assertEquals("转换2147483647字符串，返回值有误", Utils.toInt("2147483647", 0), 2147483647);
        Assert.assertEquals("转换无效字符串，返回值有误", Utils.toInt("aaa", 0), 0);
        Assert.assertEquals("转换空字符串，返回值有误", Utils.toInt("", 0), 0);
        Assert.assertEquals("转换NULL字符串，返回值有误", Utils.toInt(null, 0), 0);
    }


    @Test
    public void testToLong() {
        Assert.assertEquals("转换-1字符串，返回值有误", Utils.toLong("-1", 0), -1);
        Assert.assertEquals("转换-1.1字符串，返回值有误", Utils.toLong("-1.1", 0), -1);
        Assert.assertEquals("转换100a字符串，返回值有误", Utils.toLong("100a", 0), 100);
        Assert.assertEquals("转换100.9字符串，返回值有误", Utils.toLong("100.9", 0), 100);
        Assert.assertEquals("转换100字符串，返回值有误", Utils.toLong("100", 0), 100);
        Assert.assertEquals("转换2147483647字符串，返回值有误", Utils.toLong("2147483647", 0), 2147483647);
        Assert.assertEquals("转换无效字符串，返回值有误", Utils.toLong("aaa", 0), 0);
        Assert.assertEquals("转换空字符串，返回值有误", Utils.toLong("", 0), 0);
        Assert.assertEquals("转换NULL字符串，返回值有误", Utils.toLong(null, 0), 0);
    }

    @Test
    public void testToDouble() {
        Assert.assertEquals("转换-1字符串，返回值有误", Utils.toDouble("-1", 0), -1, 0.0);
        Assert.assertEquals("转换-1.1字符串，返回值有误", Utils.toDouble("-1.1", 0), -1.1, 0.0);
        Assert.assertEquals("转换100a字符串，返回值有误", Utils.toDouble("100a", 0), 0.0, 0.0);
        Assert.assertEquals("转换100.123456789字符串，返回值有误", Utils.toDouble("100.123456789", 0), 100.123456789, 0.0);
        Assert.assertEquals("转换100字符串，返回值有误", Utils.toDouble("100", 0), 100, 0.0);
        Assert.assertEquals("转换无效字符串，返回值有误", Utils.toDouble("aaa", 0), 0, 0.0);
        Assert.assertEquals("转换空字符串，返回值有误", Utils.toDouble("", 0), 0, 0.0);
        Assert.assertEquals("转换NULL字符串，返回值有误", Utils.toDouble(null, 0), 0, 0.0);
    }
}
