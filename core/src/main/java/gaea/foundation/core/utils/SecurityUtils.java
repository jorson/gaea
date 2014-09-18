package gaea.foundation.core.utils;

import gaea.foundation.core.Constant;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * 安全方面的工具类，提供一些较为通用的加密解密算法
 *
 * @author wuhy
 */
public class SecurityUtils {
    /**
     * 默认的字符集
     */
    public static final Charset DEFAULT_CHARSET = Constant.DEFAULT_CHARSET;

    /**
     * 私有化构造函数，不允许实例化该类
     */
    private SecurityUtils() {
    }

    /**
     * 使用BASE64加密字符串
     *
     * @param plainText
     * @return 加密后的字符串
     */
    public static String encode4Base64(String plainText) {
        return encode4Base64(plainText, true);
    }

    /**
     * 使用BASE64加密字符串
     *
     * @param plainText
     * @param isChunked 是否分组
     * @return 加密后的字符串
     */
    public static String encode4Base64(String plainText, boolean isChunked) {
        if (plainText == null) {
            return null;
        }
        return new String(Base64.encodeBase64(plainText.getBytes(DEFAULT_CHARSET), isChunked));
    }

    /**
     * 使用BASE64解密加密的字符串
     *
     * @param value
     * @return 解密后的字符串
     */
    public static String decode4Base64(String value) {
        if (value == null) {
            return null;
        }
        return new String(Base64.decodeBase64(value.getBytes()));
    }

    /**
     * 使用MD5加密字符串，默认编码(UTF-8)
     *
     * @param plainText
     * @return 加密后的字符串
     */
    public static String getMD5Digest(String plainText) {
        return getMD5Digest(plainText, DEFAULT_CHARSET);
    }

    /**
     * 使用MD5加密字符串。自定义编码
     *
     * @param plainText
     * @param charset   编码
     * @return 加密后的字符串
     */
    public static String getMD5Digest(String plainText, String charset) {
        return getMD5Digest(plainText, Charset.forName(charset));
    }

    /**
     * 使用MD5加密字符串。自定义编码
     *
     * @param plainText
     * @param charset   编码
     * @return 加密后的字符串
     */
    public static String getMD5Digest(String plainText, Charset charset) {
        if (plainText == null) return null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(plainText.getBytes(charset));
            byte[] val = md5.digest();
            return toPlusHex(val);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 使用MD5加密字符串。自定义编码
     *
     * @param plainText
     * @param charset   编码
     * @return 加密后的字符串
     */
    public static String get16MD5Digest(String plainText, Charset charset) {
        if (plainText == null) {
            return null;
        }
        String ret = getMD5Digest(plainText, charset);
        return ret.substring(8, 24);
    }

    /**
     * 转换成16进制
     *
     * @param data
     * @return
     */
    public static String toPlusHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        int by = 0;
        for (int offset = 0; offset < data.length; offset++) {
            by = data[offset];
            if (by < 0) {
                by += 256;
            }
            if (by < 16) {
                buf.append("0");
            }
            buf.append(Integer.toHexString(by));
        }
        return buf.toString();
    }
}
