package com.nd.gaea.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DateUtils的测试类
 */
public class DateUtilsTest {
    private Date date = null;

    @Before
    public void init() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DEFAULT_DATEFORMAT);
            date = sdf.parse("2014-04-24 22:45:56");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 测试DateUtils.format(date);
     */
    @Test
    public void testFormat() {
        Assert.assertEquals("format方法传入特定日期，返回与比较字符串不相同", DateUtils.format(date), "2014-04-24 22:45:56");
        Assert.assertEquals("format方法传入特定日期和日期格式，返回与比较字符串不相同", DateUtils.format(date, "yyyy-MM-dd"), "2014-04-24");
        Assert.assertEquals("format方法传入特定日期和时间格式，返回与比较字符串不相同", DateUtils.format(date, "HH:mm:ss"), "22:45:56");
    }

    /**
     * 测试DateUtils.parse("");
     */
    @Test
    public void testParse() {
        Assert.assertEquals("parse方法传入特定的字符串，返回与比较日期不相同", DateUtils.parse("2014-04-24 22:45:56"), date);
        Assert.assertEquals("parse方法传入特定的字符串和特定的格式，返回与比较日期不相同", DateUtils.parse("2014-04-24 22:45:56", "yyyy-MM-dd HH:mm:ss"), date);
    }

    /**
     * 测试DateUtils.calcTimeDiff(long),DateUtils.calcTimeDiff(long,format)
     */
    public void testCalcTimeDiff() {
        Assert.assertEquals("calcTimeDiff方法传入正数，返回与比较字符串不相同", DateUtils.calcTimeDiff(1000 * 63 * 5), "0 Day 0 Hour 5 Minute 15 Second");
        Assert.assertEquals("calcTimeDiff方法传入负数，返回与比较字符串不相同", DateUtils.calcTimeDiff(-1000 * 64 * 6), "0 Day 0 Hour 6 Minute 24 Second");
        Assert.assertEquals("calcTimeDiff方法传入零，返回与比较字符串不相同", DateUtils.calcTimeDiff(0), "0 Day 0 Hour 0 Minute 0 Second");
        Assert.assertEquals("calcTimeDiff方法传入正数和格式字符串，返回与比较字符串不相同", DateUtils.calcTimeDiff(988 * 62 * 63 * 24 * 6, "{0}天{1}时{2}分钟{3}秒{4}毫秒"), "6天10时21分钟54秒432毫秒");
    }


}
