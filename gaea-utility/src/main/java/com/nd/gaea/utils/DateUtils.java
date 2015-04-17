package com.nd.gaea.utils;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期工具类
 *
 * @author bifeng.liu
 */
public class DateUtils {
    /**
     * 一个标准秒的毫秒数
     */
    public static final long MILLIS_PER_SECOND = 1000;
    /**
     * 一个标准分钟的毫秒数
     */
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    /**
     * 一个标准时的毫秒数
     */
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    /**
     * 一个标准天的毫秒数
     */
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    static {
        /**
         * 默认的时区，中国、东八区
         */
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
    }

    /**
     * 默认的格式
     */
    public static final String DEFAULT_DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认日期格式
     */
    public static final String DEFAULT_DATE_DATEFORMAT = "yyyy-MM-dd";
    /**
     * 默认时间格式
     */
    public static final String DEFAULT_TIME_DATEFORMAT = "HH:mm:ss";

    /**
     * <p>
     * 根据默认的格式格式化当前时间
     * </p>
     *
     * @return 格式化后的日期字符串
     */
    public static String format() {
        return format(new Date(), DEFAULT_DATEFORMAT);
    }

    /**
     * <p>
     * 根据默认的格式格式化时间
     * </p>
     *
     * @param date 要格式化的日期/时间
     * @return 格式化后的日期字符串
     */
    public static String format(Date date) {
        return format(date, DEFAULT_DATEFORMAT);
    }

    /**
     * <p>
     * 根据格式格式化日期/时间
     * </p>
     *
     * @param date    要格式化的日期/时间
     * @param pattern 要使用的规则
     * @return 格式化后的日期字符串
     */
    public static String format(Date date, String pattern) {
        if (!StringUtils.hasText(pattern)) {
            pattern = DEFAULT_DATEFORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 根据默认格式把文本转换成日期/时间，
     * 转换出错，抛出异常
     *
     * @param text 要转换的文本
     * @return 转换后的日期
     */
    public static Date parse(String text) {
        return parse(text, DEFAULT_DATEFORMAT);
    }

    /**
     * 根据格式把文本转换成日期/时间，
     * 转换出错，抛出异常
     *
     * @param text    要转换的文本
     * @param pattern 要使用的规则
     * @return 转换后的日期
     */
    public static Date parse(String text, String pattern) {
        if (!StringUtils.hasText(pattern)) {
            pattern = DEFAULT_DATEFORMAT;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(text);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Date parse happen error![" + text + "," + pattern + "]");
        }
    }

    /**
     * 根据毫秒数，取得时间间隔
     * <p/>
     * 如果参数milliseconds为小于0的数时，则使用绝对值来计算
     *
     * @param times
     * @param format
     * @return
     */
    public static String calcTimeDiff(long times, String format) {
        if (times < 0) {
            times = Math.abs(times);
        }
        long day = times / MILLIS_PER_DAY;
        long hour = times % MILLIS_PER_DAY / MILLIS_PER_HOUR;
        long minute = times % MILLIS_PER_DAY % MILLIS_PER_HOUR / MILLIS_PER_MINUTE;
        long second = times % MILLIS_PER_DAY % MILLIS_PER_HOUR % MILLIS_PER_MINUTE / MILLIS_PER_SECOND;
        long millisecond = times % MILLIS_PER_SECOND;
        return MessageFormat.format(format, new Object[]{
                NumberUtils.format(day),
                NumberUtils.format(hour),
                NumberUtils.format(minute),
                NumberUtils.format(second),
                NumberUtils.format(millisecond)
        });
    }

    /**
     * 根据毫秒数，取得时间间隔
     * <p/>
     * 如果参数milliseconds为小于0的数时，则使用绝对值来计算，
     * 使用默认的格式输出{0}Day{1}Hour{2}Minute{3}Second
     *
     * @param times
     * @return
     */
    public static String calcTimeDiff(long times) {
        return calcTimeDiff(times, "{0} Day {1} Hour {2} Minute {3} Second");
    }
}
