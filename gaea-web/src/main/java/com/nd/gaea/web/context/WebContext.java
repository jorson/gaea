package com.nd.gaea.web.context;

import com.nd.gaea.core.Constant;
import com.nd.gaea.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * 保存请求的上下文信息
 *
 * @author bifeng.liu
 */
public abstract class WebContext {
    private static final Log LOGGER = LogFactory.getLog(WebContext.class);

    /**
     * 默认字符集
     */
    public static final String DEFAULT_CHARSET_NAME = Constant.DEFAULT_CHARSET_NAME;
    /**
     * 默认的区域
     */
    public static final Locale DEFAULT_LOCALE = Locale.SIMPLIFIED_CHINESE;

    public static final String DEFAULT_REMOTE_ADDRESS = "0.0.0.1";

    private static final ThreadLocal<String> remoteAddressHolder = new ThreadLocal<String>();

    private static final ThreadLocal<Locale> localeHolder = new ThreadLocal<Locale>();

    private static final ThreadLocal<ServletRequest> requestHolder = new ThreadLocal<ServletRequest>();

    private static final ThreadLocal<ServletResponse> responseHolder = new ThreadLocal<ServletResponse>();

    /**
     * 取得当前访问的远程地址
     * <p/>
     * 如果在<code>remoteAddressHolder</code>中没值，则直接从request取得，
     * 如果request没值，则返回<code>DEFAULT_REMOTE_ADDRESS</code>;
     *
     * @return 远程地址
     */
    public static String getRemoteAddress() {
        String remoteAddress = remoteAddressHolder.get();
        if (StringUtils.isEmpty(remoteAddress)) {
            HttpServletRequest request = (HttpServletRequest) getRequest();
            if (request != null) {
                remoteAddress = request.getRemoteAddr();
                remoteAddressHolder.set(remoteAddress);
            } else {
                remoteAddress = DEFAULT_REMOTE_ADDRESS;
            }
        }
        return remoteAddress;
    }

    /**
     * 设置当前远程地址
     *
     * @param remoteAddress
     */
    public static void setRemoteAddress(String remoteAddress) {
        Assert.hasText(remoteAddress);
        LOGGER.debug("set remote address : " + remoteAddress);
        remoteAddressHolder.set(remoteAddress);
    }

    /**
     * 取得当前请求的时区
     *
     * @return
     */
    public static Locale getRequestLocale() {
        Locale locale = localeHolder.get();
        if (locale == null) {
            HttpServletRequest request = (HttpServletRequest) getRequest();
            if (request != null) {
                locale = request.getLocale();
                localeHolder.set(locale);
            }
        }
        if (Locale.CHINESE.equals(locale)) {
            return Locale.SIMPLIFIED_CHINESE;
        }
        return locale;
    }

    /**
     * 设置当前请求的时区
     *
     * @return
     */
    public static void setRequestLocale(Locale locale) {
        Assert.notNull(locale);
        LOGGER.debug("set request locale : " + locale);
        localeHolder.set(locale);
    }

    /**
     * 取得当前请求的Request对象
     *
     * @return
     */
    public static ServletRequest getRequest() {
        ServletRequest request = requestHolder.get();
        return request;
    }

    /**
     * 设置当前请求的Request对象
     *
     * @return
     */
    public static void setRequest(ServletRequest request) {
        Assert.notNull(request);
        LOGGER.debug("set request : " + request);
        requestHolder.set(request);
    }

    /**
     * 取得当前请求的Response对象
     *
     * @return 取得Response对象
     */
    public static ServletResponse getResponse() {
        ServletResponse response = responseHolder.get();
        return response;
    }

    /**
     * 设置当前请求的Response对象
     *
     * @param response
     */
    public static void setResponse(ServletResponse response) {
        Assert.notNull(response);
        LOGGER.debug("set response : " + response);
        responseHolder.set(response);
    }

    /**
     * 取得当前使用的时区
     * <p/>
     * 从当前请求中取得，如果没有取得Request，则使用默认时区
     *
     * @return
     */
    public static Locale resolveCurrentLocale() {
        Locale requestLocale = getRequestLocale();
        if (requestLocale != null) {
            return requestLocale;
        }
        return DEFAULT_LOCALE;
    }

    /**
     * 取得当前字符集，现在只支持utf-8
     *
     * @return
     */
    public static Charset getCurrentCharset() {
        return Charset.forName(DEFAULT_CHARSET_NAME);
    }
}
