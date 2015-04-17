package com.nd.gaea.utils;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 字符串工具类
 * <p/>
 *
 * @author bifeng.liu
 */
public class StringUtils {

    /**
     * The empty String <code>""</code>.
     */
    public static final String EMPTY = "";
    /**
     * 默认单词分割字符<code>"_"</code>
     */
    public static final char DEFAULT_WORD_SEPARATOR_CHAR = '_';

    /**
     * 检查指定字符串长度，表示是否有值
     * <p><pre>
     * StringUtils.hasLength(null) = false
     * StringUtils.hasLength("") = false
     * StringUtils.hasLength(" ") = true
     * StringUtils.hasLength(" \t\n") = true
     * StringUtils.hasLength("Hello") = true
     * </pre>
     *
     * @param str 要检查的字符串
     * @return <code>true</code> 如果不为null或者空，则返回真
     * @see #hasText(CharSequence)
     * @see #isEmpty(String)
     */
    public static boolean hasLength(CharSequence str) {
        return str != null && str.length() > 0;
    }

    /**
     * 检查指定字符串是否有内容，如果为null或者全为空白字符，则返回false
     * NOTE:空字符包括空格、换行符、Tab符等
     * <p><pre>
     * StringUtils.hasText(null) = false
     * StringUtils.hasText("") = false
     * StringUtils.hasText(" ") = false
     * StringUtils.hasText(" \n\t") = false
     * StringUtils.hasText("12345") = true
     * StringUtils.hasText(" 12345 ") = true
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果不为null且不全为空字符，则返回<code>true</code> e
     * @see Character#isWhitespace
     */
    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查指定字符串长度是否为空字符串
     * <p><pre>
     * StringUtils.isEmpty(null) = true
     * StringUtils.isEmpty("") = true
     * StringUtils.isEmpty(" ") = false
     * StringUtils.isEmpty("Hello") = false
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果为null或者空，则返回<code>true</code>
     * @see #hasLength(CharSequence)
     */
    public static boolean isEmpty(String str) {
        return !hasLength(str);
    }

    /**
     * 检查指定字符串长度是否为非空字符串
     * <p><pre>
     * StringUtils.isNotEmpty(null) = false
     * StringUtils.isNotEmpty("") = false
     * StringUtils.isNotEmpty(" ") = true
     * StringUtils.isNotEmpty("Hello") = true
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果为null或者空，则返回<code>false</code>
     * @see #hasLength(CharSequence)
     */
    public static boolean isNotEmpty(String str) {
        return hasLength(str);
    }

    /**
     * 去掉指定字符串前后的空字符串
     * NOTE:空字符包括空格、换行符、Tab符等
     *
     * @param str 要处理的字符串
     * @return 处理后的字符串
     * @see Character#isWhitespace
     */
    public static String trimWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
            sb.deleteCharAt(0);
        }
        while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * <p>连接字符串数组</p>
     * <p/>
     * <p>使用空字符串连接
     * 如果数组中的字符串为空或者NULL，则会被忽略</p>
     * <p/>
     * <pre>
     * ArrayUtils.join(null)            = null
     * ArrayUtils.join([])              = ""
     * ArrayUtils.join([null])          = ""
     * ArrayUtils.join(["a", "b", "c"]) = "abc"
     * ArrayUtils.join([null, "", "a"]) = "a"
     * </pre>
     *
     * @param array 要连接字符串数组
     * @return 连接后的字符串, 如果为null返回<code>null</code>
     */
    public static String join(Object[] array) {
        return join(array, null);
    }

    /**
     * <p>使用特定的字符串，连接字符串数组</p>
     * <p/>
     * <p>使用特定的字符串连接
     * 如果数组中的字符串为空或者NULL，设置为空字符串</p>
     * <p/>
     * <pre>
     * ArrayUtils.join(null, *)                = null
     * ArrayUtils.join([], *)                  = ""
     * ArrayUtils.join([null], *)              = ""
     * ArrayUtils.join(["a", "b", "c"], "--")  = "a--b--c"
     * ArrayUtils.join(["a", "b", "c"], null)  = "abc"
     * ArrayUtils.join(["a", "b", "c"], "")    = "abc"
     * ArrayUtils.join([null, "", "a"], ',')   = ",,a"
     * </pre>
     *
     * @param array     要连接字符串数组
     * @param separator 分隔符
     * @return 连接后的字符串, 如果为null返回<code>null</code>
     */
    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    /**
     * <p>使用特定的字符串，连接字符串数组</p>
     * <p/>
     * <p>使用特定的字符串从开始到结束索引连接
     * 如果数组中的字符串为空或者NULL，设置为空字符串</p>
     * <p/>
     * <pre>
     * ArrayUtils.join(null, *)                = null
     * ArrayUtils.join([], *)                  = ""
     * ArrayUtils.join([null], *)              = ""
     * ArrayUtils.join(["a", "b", "c"], "--")  = "a--b--c"
     * ArrayUtils.join(["a", "b", "c"], null)  = "abc"
     * ArrayUtils.join(["a", "b", "c"], "")    = "abc"
     * ArrayUtils.join([null, "", "a"], ',')   = ",,a"
     * </pre>
     *
     * @param array      要连接字符串数组
     * @param separator  分隔符
     * @param startIndex 开始索引
     * @param length     长度
     * @return 连接后的字符串, 如果为null返回<code>null</code>
     */
    public static String join(Object[] array, String separator, int startIndex, int length) {
        if (array == null) {
            return null;
        }
        startIndex = startIndex < 0 ? 0 : startIndex;
        if (startIndex >= array.length || length <= 0) {
            return EMPTY;
        }
        if (separator == null) {
            separator = EMPTY;
        }

        int endIndex = startIndex + length;
        endIndex = endIndex > array.length ? array.length : endIndex;
        StringBuilder buf = new StringBuilder();

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    /**
     * 判断两个字符串是否相等
     *
     * @param srcStr  源字符串
     * @param destStr 目标字符串
     * @return 是否相同
     */
    public static boolean equals(String srcStr, String destStr) {
        return srcStr != null ? srcStr.equals(destStr) : destStr == null;
    }

    /**
     * 判断两个字符串是否相等，忽略大小写
     *
     * @param srcStr  源字符串
     * @param destStr 目标字符串
     * @return 是否相同
     */
    public static boolean equalsIgnoreCase(String srcStr, String destStr) {
        return srcStr != null ? srcStr.equalsIgnoreCase(destStr) : destStr == null;
    }

    /**
     * 从首位置开始，根据限制长度截取字符串
     * <p/>
     * 其中截取长度的计算：全角字符算两个字符，全角字符包括汉字等
     *
     * @param str         要截取的字符串
     * @param limitLength 截取的长度
     * @return 截取后的字符串
     * @see #subString(String, int, int)
     */
    public static String subString(String str, int limitLength) {
        return subString(str, 0, limitLength);
    }

    /**
     * 从传入的位置开始，根据限制长度截取字符串
     * <p/>
     * 其中截取长度的计算：全角字符算两个字符，全角字符包括汉字等
     *
     * @param str         要截取的字符串
     * @param startIndex  开始位置
     * @param limitLength 截取的长度
     * @return 截取后的字符串
     */
    public static String subString(String str, int startIndex, int limitLength) {
        if (!hasLength(str) || limitLength <= 0 || startIndex >= str.length()) {
            return "";
        }
        startIndex = startIndex < 0 ? 0 : startIndex;
        String doStr = str.substring(startIndex);
        int byteLen = 0; // 将汉字转换成两个字符后的字符串长度
        int strPos = 0;  // 对原始字符串截取的长度
        byte[] strBytes = null;
        try {
            strBytes = doStr.getBytes("gbk");// 将字符串转换成字符数组
        } catch (Exception ex) {
            strBytes = doStr.getBytes();
        }
        for (int i = 0; i < strBytes.length; i++) {
            if (strBytes[i] >= 0) {
                byteLen = byteLen + 1;
            } else {
                byteLen = byteLen + 2;// 一个汉字等于两个字符
                i++;
            }
            strPos++;

            if (byteLen >= limitLength) {
                if (strBytes[byteLen - 1] < 0) {
                    strPos--;
                }
                return doStr.substring(0, strPos);
            }
        }
        return doStr;
    }


    /**
     * 把字符串的首字母大写
     *
     * @param str 要转换的字符串
     * @return A new string that is <code>str</code> capitalized.
     * Returns <code>null</code> if str is null.
     */
    public static String toUpperFirstLetter(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        } else {
            return (new StringBuilder(strLen)).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).toString();
        }
    }

    /**
     * 把字符串的首字母小写
     *
     * @param str 要转换的字符串
     * @return A new string that is <code>str</code> capitalized.
     * Returns <code>null</code> if str is null.
     */
    public static String toLowerFirstLetter(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        } else {
            return (new StringBuilder(strLen)).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();
        }
    }

    /**
     * 生成UUID
     * <p/>
     * 当hasSymbol为false时，返回字符串格式为32位16进制数字
     * 当hasSymbol为true时，返回字符串格式为：xxxxxxxx-xxxx-xxxx-xxxxxx-xxxxxxxxxx (8-4-4-4-12)
     *
     * @return
     */
    public static String generateUUID(boolean hasSymbol) {
        UUID uuid = UUID.randomUUID();
        String result = uuid.toString();
        return hasSymbol ? result : result.replace("-", "");
    }

    /**
     * 把字符串转换成JSON格式
     *
     * @param str
     * @return
     */
    public static String toJsonQuote(String str) {
        if ((str == null) || (str.length() == 0)) {
            return "\"\"";
        }
        if ("true".equalsIgnoreCase(str)) {
            return "true";
        }
        if ("false".equalsIgnoreCase(str)) {
            return "false";
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(len + 4);

        sb.append('"');
        for (int i = 0; i < len; ++i) {
            char c = str.charAt(i);
            switch (c) {
                case '"':
                case '/':
                case '\\':
                    sb.append('\\');
                    sb.append(c);
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                default:
                    if ((c < ' ') || (c >= '')) {
                        String t = "000" + Integer.toHexString(c);
                        sb.append("\\u");
                        sb.append(t.substring(t.length() - 4));
                    } else {
                        sb.append(c);
                    }
                    // no break
            }
        }
        sb.append('"');
        return sb.toString();
    }

    /**
     * 使用默认的分隔符，转换成驼峰的显示方式
     *
     * @param str
     * @return
     */
    public static String toCamelCase(String str) {
        return toCamelCase(str, DEFAULT_WORD_SEPARATOR_CHAR);
    }

    /**
     * 使用字符串分隔符转换成驼峰显示方式
     *
     * @param str
     * @param separatorChar 分隔符
     * @return
     */
    public static String toCamelCase(String str, char separatorChar) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(str.length());
        boolean upperCase = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == separatorChar) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 把驼峰的显示方式转换成用某字符分隔的字符串
     *
     * @param str
     * @param separatorChar 分隔符
     * @return
     */
    public static String revertCamelCase(String str, char separatorChar) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            boolean nextUpperCase = true;
            if (i < (str.length() - 1)) {
                nextUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }
            if ((i >= 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    if (i > 0) {
                        sb.append(separatorChar);
                    }
                }
                upperCase = true;
            } else {
                upperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }
}
