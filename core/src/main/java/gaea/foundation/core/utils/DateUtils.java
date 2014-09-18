package gaea.foundation.core.utils;

import gaea.foundation.core.Constant;
import org.apache.commons.lang.time.DateFormatUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期工具类
 *
 * @author wuhy
 * @see org.apache.commons.lang.time.DateUtils
 */
public final class DateUtils extends org.apache.commons.lang.time.DateUtils {

    /**
     * 私有化构造函数，不允许实例化该类
     */
    private DateUtils() {
    }

    static {
        TimeZone.setDefault(Constant.DEFAULT_TIMEZONE);
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
        return DateFormatUtils.format(date, pattern);
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
     * TODO 还有待优化
     *
     * @param milliseconds
     * @return
     */
    public static String distance(long milliseconds) {
        if (milliseconds <= 0) {
            return "0";
        }

        long second = milliseconds / 1000;
        long millisecond = milliseconds % 1000;
        long minute = second / 60;
        second = second % 60;

        long hour = minute / 60;
        minute = minute % 60;

        long day = hour / 60;
        hour = hour % 60;
        return new StringBuilder().append(day).append(" Day ").append(hour).append(" Hour ").append(minute)
                .append(" Minute ").append(second).append(" Second ").append(millisecond).append(" Millisecond ").toString();

    }
}
