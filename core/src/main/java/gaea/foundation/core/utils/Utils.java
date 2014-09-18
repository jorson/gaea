package gaea.foundation.core.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * 工具类，提供一些较为通用的方法
 *
 * @author wuhy
 */
public class Utils {

    private static Log logger = LogFactory.getLog(Utils.class);

    /**
     * 私有化构造函数，不允许实例化该类
     */
    private Utils() {
    }

    /**
     * 根据Key取得Map中的值，如果不存在或者为null，则返回默认值
     *
     * @param data
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getMapStringValue(Map data, String key, String defaultValue) {
        Object obj = getMapValue(data, key);
        return obj == null ? defaultValue : obj.toString();
    }

    /**
     * 根据Key取得Map中的Int值，如果不存在或者转换出错，则返回默认值
     *
     * @param data
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getMapIntValue(Map data, String key, int defaultValue) {
        Object obj = getMapValue(data, key);
        int result = defaultValue;
        if (obj != null) {
            if (obj instanceof Integer) {
                result = (Integer) obj;
            } else if (obj instanceof String) {
                try {
                    result = Integer.parseInt((String) obj);
                } catch (Exception ex) {
                    logger.info("parse integer happen error!");
                }
            }
        }
        return result;
    }

    /**
     * 根据Key取得Map中的Long值，如果不存在或者转换出错，则返回默认值
     *
     * @param data
     * @param key
     * @param defaultValue
     * @return
     */
    public static long getMapLongValue(Map data, String key, long defaultValue) {
        Object obj = getMapValue(data, key);
        long result = defaultValue;
        if (obj != null) {
            if (obj instanceof Long) {
                result = (Long) obj;
            } else if (obj instanceof String) {
                try {
                    result = Long.parseLong((String) obj);
                } catch (Exception ex) {
                    logger.info("parse long happen error!");
                }
            }
        }
        return result;
    }

    /**
     * 根据Key取得Map中的值，如果不存在则返回null
     *
     * @param data
     * @param key
     * @return
     */
    public static Object getMapValue(Map data, Object key) {
        if (data != null) {
            return data.get(key);
        }
        return null;
    }

    /**
     * 根据Key取得Map中的值，如果不存在或者为null，则返回默认值
     *
     * @param data
     * @param key
     * @param defaultValue
     * @return
     */
    public static Object getMapValue(Map data, Object key, Object defaultValue) {
        Object ret = defaultValue;
        if (data != null) {
            ret = data.get(key);
            ret = ret == null ? defaultValue : ret;
        }
        return ret;
    }


    /**
     * 把字符转换成Boolean，
     * <p/>
     * 当为true/yes/ok/1字符串时，返回true,否则返回false，不区分大小写
     *
     * @param s
     * @return
     */
    public static boolean toBoolean(String s) {
        if (s == null || s.length() == 0) return false;
        return s.compareToIgnoreCase("TRUE") == 0 || s.compareToIgnoreCase("YES") == 0
                || s.compareToIgnoreCase("OK") == 0 || s.compareToIgnoreCase("1") == 0;
    }

    /**
     * 把字符串转换成Integer类型，如果转换出错，则抛出异常
     * <p/>
     * 字符串为数字开头，会直接取得从0索引开始的数字进行转换，
     * 如100a，则返回100，100.9返回100
     *
     * @param s
     * @return
     */
    public static int toInt(String s) throws ParseException {
        DecimalFormat df = new DecimalFormat();
        return df.parse(s).intValue();
    }

    /**
     * 把字符串转换成Integer类型，如果转换出错，则返回0
     * <p/>
     * 字符串为数字开头，会直接取得从0索引开始的数字进行转换，
     * 如100a，则返回100，100.9返回100
     *
     * @param s
     * @return
     */
    public static int toInt(String s, int defaultValue) {
        if (s == null || s.length() == 0) {
            return defaultValue;
        }
        try {
            return toInt(s);
        } catch (ParseException e) {
            return defaultValue;
        }
    }

    /**
     * 把字符串转换成Long类型，如果转换出错，则抛出异常
     * <p/>
     * 字符串为数字开头，会直接取得从0索引开始的数字进行转换，
     * 如100a，则返回100，100.9返回100
     *
     * @param s
     * @return
     */
    public static long toLong(String s) throws ParseException {
        DecimalFormat df = new DecimalFormat();
        return df.parse(s).longValue();
    }

    /**
     * 把字符串转换成Long类型，如果转换出错，则返回0
     * <p/>
     * 字符串为数字开头，会直接取得从0索引开始的数字进行转换，
     * 如100a，则返回100，100.9返回100
     *
     * @param s
     * @return
     */
    public static long toLong(String s, long defaultValue) {
        if (s == null || s.length() == 0) {
            return defaultValue;
        }
        try {
            return toLong(s);
        } catch (ParseException e) {
            return defaultValue;
        }
    }

    /**
     * 根据对象生成Long类型
     * <p/>
     *
     * @param obj 对象
     * @return
     */
    public static long generateLong(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        } else if (obj instanceof Long) {
            return (Long) obj;
        } else if (obj instanceof Date) {
            return ((Date) obj).getTime();
        } else {
            return obj.toString().hashCode();
        }
    }

    /**
     * 把字符串转换成Float型，如果转换出错，则抛出异常
     * <p/>
     * 该Float的精度大概在0.00001左右，转换时请慎重使用
     *
     * @param s
     * @return
     * @deprecated Use #toDouble
     */
    public static float toFloat(String s) {
        BigDecimal dec = new BigDecimal(s);
        return dec.floatValue();
    }

    /**
     * 把字符串转换成Float型，如果转换出错，则返回0
     * <p/>
     * 该Float的精度大概在0.00001左右，转换时请慎重使用
     *
     * @param s
     * @return
     * @deprecated Use #toDouble
     */
    public static float toFloat(String s, float defaultValue) {
        if (s == null || s.length() == 0) {
            return defaultValue;
        }
        try {
            return toFloat(s);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 把字符串转换成Double类型，如果转换出错，则抛出异常
     * <p/>
     * 字符串为数字开头，会直接取得从0索引开始的数字进行转换，
     * 如100a，则返回100，100.9返回100
     *
     * @param s
     * @return
     */
    public static double toDouble(String s) {
        BigDecimal dec = new BigDecimal(s);
        return dec.doubleValue();
    }

    /**
     * 把字符串转换成Double类型，如果转换出错，则返回0
     * <p/>
     * 字符串为数字开头，会直接取得从0索引开始的数字进行转换，
     * 如100a，则返回100，100.9返回100
     *
     * @param s
     * @return
     */
    public static double toDouble(String s, double defaultValue) {
        if (s == null || s.length() == 0) {
            return defaultValue;
        }
        try {
            return toDouble(s);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}

