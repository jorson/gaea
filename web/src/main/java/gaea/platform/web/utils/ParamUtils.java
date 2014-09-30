package gaea.platform.web.utils;

import gaea.foundation.core.utils.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * 参数处理工具类
 * <p/>
 *
 * @author bifeng.liu
 */
public class ParamUtils {

    private static Log logger = LogFactory.getLog(ParamUtils.class);

    /**
     * 私有化构造函数，不允许实例化该类
     */
    private ParamUtils() {
    }

    public static String getString(HttpServletRequest request, String paramName) {
        return request.getParameter(paramName);
    }

    public static String getString(HttpServletRequest request, String paramName, String defaultValue) {
        String temp = request.getParameter(paramName);
        return temp == null ? defaultValue : temp;
    }

    public static String getDecodeString(HttpServletRequest request, String paramName, String defaultString) {
        return getDecodeString(request, paramName, defaultString, "GBK");
    }

    public static String getDecodeString(HttpServletRequest request, String paramName, String defaultString, String charset) {
        String temp = request.getParameter(paramName);
        temp = temp == null ? defaultString : temp;

        try {
            return java.net.URLDecoder.decode(temp, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return temp;
        }
    }

    public static int getInt(HttpServletRequest request, String paramName) throws NumberFormatException {
        return Integer.parseInt(getString(request, paramName));
    }

    public static int getInt(HttpServletRequest request, String paramName, int defaultValue) {
        String temp = getString(request, paramName);
        return Utils.toInt(temp, defaultValue);
    }

    public static long getLong(HttpServletRequest request, String paramName, long defaultValue) {
        String temp = getString(request, paramName);
        return Utils.toLong(temp, defaultValue);
    }


}
