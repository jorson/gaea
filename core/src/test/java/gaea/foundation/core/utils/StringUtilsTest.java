package gaea.foundation.core.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * StringUtils的测试类
 */
public class StringUtilsTest {
    /**
     * 测试StringUtils.subString方法
     */
    @Test
    public void testSubString() {
        Assert.assertEquals("传入英文字符，截取的字符串与比较字符串不一致", StringUtils.subString("aaa", 3), "aaa");
        Assert.assertEquals("传入英文字符和开始位置，截取的字符串与比较字符串不一致", StringUtils.subString("aaa", 1, 3), "aa");
        Assert.assertEquals("传入中文字符，截取的字符串与比较字符串不一致", StringUtils.subString("a中a文aaaa", 2, 4), "a文a");
        Assert.assertEquals("传入中文字符且长度在中文字符的中间，截取的字符串与比较字符串不一致", StringUtils.subString("a中a文aaaa", 0, 2), "a");
        Assert.assertEquals("传入全角字符，截取的字符串与比较字符串不一致", StringUtils.subString("ＡＢＣＤ", 1, 3), "Ｂ");
        Assert.assertEquals("传入无效的长度，截取的字符串与比较字符串不一致", StringUtils.subString("aaa", 1, -1), "");
        Assert.assertEquals("传入超过的长度，截取的字符串与比较字符串不一致", StringUtils.subString("aaa", 1, 100), "aa");
    }

    /**
     * 测试StringUtils.toUpperFirstLetter方法
     */
    @Test
    public void testToUpperFirstLetter() {
        Assert.assertEquals("传入英文字符，返回与比较字符串不一致", StringUtils.toUpperFirstLetter("aaa"), "Aaa");
        Assert.assertEquals("传入首字符为英文的混合字符，返回与比较字符串不一致", StringUtils.toUpperFirstLetter("a中a文"), "A中a文");
        Assert.assertEquals("传入中文字符，返回与比较字符串不一致", StringUtils.toUpperFirstLetter("中文"), "中文");
    }


    /**
     * 测试StringUtils.toUpperFirstLetter方法
     */
    @Test
    public void testToLowerFirstLetter() {
        Assert.assertEquals("传入英文字符，返回与比较字符串不一致", StringUtils.toLowerFirstLetter("AAAA"), "aAAA");
        Assert.assertEquals("传入首字符为英文的混合字符，返回与比较字符串不一致", StringUtils.toLowerFirstLetter("A中a文"), "a中a文");
        Assert.assertEquals("传入中文字符，返回与比较字符串不一致", StringUtils.toLowerFirstLetter("中文"), "中文");
    }

    @Test
    public void testToJsonQuote() {
        Assert.assertEquals("传入英文字符，返回与比较字符串不一致", StringUtils.toJsonQuote("abc"), "\"abc\"");
        Assert.assertEquals("传入中文字符，返回与比较字符串不一致", StringUtils.toJsonQuote("测试文本"), "\"\\u6d4b\\u8bd5\\u6587\\u672c\"");
        Assert.assertEquals("传入双引号字符，返回与比较字符串不一致", StringUtils.toJsonQuote("\""), "\"\\\"\"");
        Assert.assertEquals("传入布尔字符，返回与比较字符串不一致", StringUtils.toJsonQuote("trUE"), "true");
    }
}
