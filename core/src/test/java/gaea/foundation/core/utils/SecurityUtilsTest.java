package gaea.foundation.core.utils;

import gaea.foundation.core.Constant;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;
import sun.misc.BASE64Encoder;

import java.util.UUID;

/**
 * 安全工具类的测试类
 *
 * @author wuhy
 */
public class SecurityUtilsTest {
    @Test
    public void testBase64EncodeDecode() {
        Assert.assertEquals("使用BASE64转换空字符串，返回的字符串与原来的字符串不一致",
                SecurityUtils.decode4Base64(SecurityUtils.encode4Base64("")), "");
        Assert.assertEquals("使用BASE64转换NULL字符串，返回的字符串与原来的字符串不一致",
                SecurityUtils.decode4Base64(SecurityUtils.encode4Base64(null)), null);
        Assert.assertEquals("使用BASE64转换空格字符串，返回的字符串与原来的字符串不一致",
                SecurityUtils.decode4Base64(SecurityUtils.encode4Base64("  ")), "  ");
        String value = "测试文本123!@#$%)(&*&";
        Assert.assertEquals("使用BASE64转换有效字符串，返回的字符串与原来的字符串不一致",
                SecurityUtils.decode4Base64(SecurityUtils.encode4Base64(value)), value);
        value = "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&" +
                "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&" +
                "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&" +
                "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&" +
                "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&" +
                "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&" +
                "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&" +
                "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&" +
                "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&" +
                "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&" +
                "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&";
        Assert.assertEquals("使用BASE64转换非常长的字符串，返回的字符串与原来的字符串不一致",
                SecurityUtils.decode4Base64(SecurityUtils.encode4Base64(value)), value);
    }

    /**
     * 测试MD5的加密
     * <p/>
     * 比较字符串是由ASP.net程序生成，字符串全部使用UTF-8编码
     */
    @Test
    public void testGetMD5Digest() {
        Assert.assertEquals("使用MD5加密有效字符串，返回的字符串与比较字符串不一致",
                SecurityUtils.getMD5Digest("000000"), "670b14728ad9902aecba32e22fa4f6bd");
        Assert.assertEquals("使用MD5加密null字符串，返回的字符串与比较字符串不一致",
                SecurityUtils.getMD5Digest(null), null);
        Assert.assertEquals("使用MD5加密空字符串，返回的字符串与比较字符串不一致",
                SecurityUtils.getMD5Digest(""), "d41d8cd98f00b204e9800998ecf8427e");
        Assert.assertEquals("使用MD5加密空格字符串，返回的字符串与比较字符串不一致",
                SecurityUtils.getMD5Digest("   "), "628631f07321b22d8c176c200c855e1b");
        Assert.assertEquals("使用MD5加密空格字符串，返回的字符串与比较字符串不一致",
                SecurityUtils.getMD5Digest("工作"), "9a018b21ab114a51dd7b5979198a941b");

        String value = "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&" +
                "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&" +
                "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&" +
                "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&" +
                "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&" +
                "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&" +
                "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&" +
                "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&" +
                "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&" +
                "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&" +
                "测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&测试文本123!@#$%)(&*&";
        Assert.assertEquals("使用BASE64转换非常长的字符串，返回的字符串与原来的字符串不一致",
                SecurityUtils.getMD5Digest(value), "9acede20600d399cfb04ada11885aa36");
    }

}
