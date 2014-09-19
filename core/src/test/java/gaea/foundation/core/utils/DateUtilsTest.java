package gaea.foundation.core.utils;

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
}
