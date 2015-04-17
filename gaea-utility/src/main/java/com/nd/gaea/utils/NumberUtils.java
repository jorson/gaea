package com.nd.gaea.utils;

import java.text.DecimalFormat;

/**
 * 数字工具类
 *
 * @author bifeng.liu
 */
public class NumberUtils  {
    /**
     * 默认的整型格式
     */
    public static final String DEFAULT_INTEGER_FORMAT = "#,##0";
    /**
     * 默认的浮点型格式
     */
    public static final String DEFAULT_FLOAT_FORMAT = "#,##0.00";

    /**
     * 根据默认的格式格式化数值
     * <p/>
     *
     * @param value 要格式化的数值
     * @return 格式化后的数值字符串
     */
    public static String format(long value) {
        return format(value, DEFAULT_INTEGER_FORMAT);
    }

    /**
     * 根据默认的格式格式化数值
     * <p/>
     *
     * @param value   要格式化的数值
     * @param pattern 要使用的规则
     * @return 格式化后的数值字符串
     */
    public static String format(long value, String pattern) {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern(pattern);
        return df.format(value);
    }

    /**
     * 根据默认的格式格式化数值
     * <p/>
     *
     * @param value 要格式化的数值
     * @return 格式化后的数值字符串
     */
    public static String format(double value) {
        return format(value, DEFAULT_FLOAT_FORMAT);
    }

    /**
     * 根据默认的格式格式化数值
     * <p/>
     *
     * @param value   要格式化的数值
     * @param pattern 要使用的规则
     * @return 格式化后的数值字符串
     */
    public static String format(double value, String pattern) {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern(pattern);
        return df.format(value);
    }
}