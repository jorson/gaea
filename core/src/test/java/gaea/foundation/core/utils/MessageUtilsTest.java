package gaea.foundation.core.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ND on 14-4-28.
 */
public class MessageUtilsTest {

    private Object[] arrayParameters = null;
    private Map<String, Object> mapParameters = null;


    @Before
    public void init() {
        Date date = DateUtils.parse("2014-04-24 22:45:56");
        arrayParameters = new Object[]{"测试文本", "TestText", 1.1, true, "InnerTestText01"};
        mapParameters = new HashMap<String, Object>();
        mapParameters.put("中文", "测试文本");
        mapParameters.put("eng", "TestText");
        mapParameters.put("num", 1.1);
        mapParameters.put("bool", true);
        mapParameters.put("inner", "InnerTestText01");
    }

    @Test
    public void testArrayFormat() {
        Assert.assertEquals("format方法不传入参数，返回字符串与比较字符串不一致", MessageUtils.format("{0},{1},{2},{3},{4},{0}aa"), "{0},{1},{2},{3},{4},{0}aa");
        Assert.assertEquals("format方法传入数组参数，返回字符串与比较字符串不一致",
                MessageUtils.format("{0},{1},{2},{3},{4},{0},{5}", arrayParameters),
                "测试文本,TestText,1.1,true,InnerTestText01,测试文本,{5}");
        Assert.assertEquals("format方法存在特殊字符，返回字符串与比较字符串不一致",
                MessageUtils.format("{{0}},\\{11},&^%$#@!()*{3a},{Inner{1}01},{0},{半边{", arrayParameters),
                "{测试文本},\\{11},&^%$#@!()*{3a},{InnerTestText01},测试文本,{半边{");
    }

    @Test
    public void testMapFormat() {
        Assert.assertEquals("format方法传入数组参数，返回字符串与比较字符串不一致",
                MessageUtils.format("{中文},{eng},{num},{bool},{inner},{中文},{5}", mapParameters),
                "测试文本,TestText,1.1,true,InnerTestText01,测试文本,{5}");
        Assert.assertEquals("format方法存在特殊字符，返回字符串与比较字符串不一致",
                MessageUtils.format("{{中文}},\\{11},&^%$#@!()*{3a},{Inner{eng}01},{中文},{半边{", mapParameters),
                "{测试文本},\\{11},&^%$#@!()*{3a},{InnerTestText01},测试文本,{半边{");
    }
}
