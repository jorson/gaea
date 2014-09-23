package gaea.platform.security.utils;

import gaea.foundation.core.utils.HttpClientUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jsc on 14-5-27.
 */
public class HttpUtil {

    private HttpUtil() {

    }

    /**
     * logger
     */
    private static Logger logger = Logger.getLogger(HttpUtil.class);

    /**
     * 将url参数转换成map
     *
     * @param param aa=11&bb=22&cc=33
     * @return
     */
    public static Map<String, String> getUrlParams(String param) {
        Map<String, String> map = new HashMap<String, String>(0);
        if (StringUtils.isBlank(param)) {
            return map;
        }
        String[] params = param.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    /**
     * 将map转换成url
     *
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }

    /**
     * httpGet请求
     *
     * @param url
     * @return
     * @throws java.io.IOException
     */
    public static JSONObject httpGet(String url) {
        JSONObject json = null;
        try {
            String body = HttpClientUtils.httpGet(url);
            json = new JSONObject(body);
        } catch (Exception e) {
            logger.error("httpGet 请求失败！", e);
        }
        return json;
    }

    /**
     * httpGet请求
     *
     * @param url
     * @param params
     * @return
     * @throws java.io.IOException
     */
    public static JSONObject httpGet(String url, Map<String, String> params) {
        JSONObject json = null;
        try {
            String body = HttpClientUtils.httpGet(url, params);
            json = new JSONObject(body);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("httpGet 请求失败！", e);
        }
        return json;
    }

    /**
     * httpPost请求
     *
     * @param url
     * @param params
     * @return
     * @throws java.io.IOException
     */
    public static JSONObject httpPost(String url, Map<String, String> params) {
        JSONObject json = null;
        try {
            String body = HttpClientUtils.httpPost(url, params);
            json = new JSONObject(body);
        } catch (Exception e) {
            logger.error("httpPost 请求失败！", e);
        }
        return json;
    }
}
