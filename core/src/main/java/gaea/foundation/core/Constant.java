package gaea.foundation.core;

import java.nio.charset.Charset;
import java.util.TimeZone;

/**
 * 系统中使用到的常量
 *
 * @author wuhy
 */
public class Constant {
    /**
     * 私有化构造函数，不允许实例化该类
     */
    private Constant() {
    }

    /**
     * 默认的时区，中国、东八区
     */
    public static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("GMT+8");

    /**
     * 默认的字符集名称
     */
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";
    /**
     * 默认的字符集
     */
    public static final Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_NAME);

    /**
     * url映射controller的后缀
     */
    public static final String URL_SUFFIX = ".do";
}
