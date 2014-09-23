package gaea.platform.security.context;

import gaea.foundation.core.utils.DateUtils;
import gaea.foundation.core.utils.StringUtils;
import gaea.platform.security.access.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Locale;

/**
 * 保存登录用户的上下文信息
 *
 * @author wuhy
 */
public abstract class UserContext {
    private static final Log logger = LogFactory.getLog(UserContext.class);

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final Locale DEFAULT_LOCALE = Locale.SIMPLIFIED_CHINESE;

    public static final String DEFAULT_REMOTE_ADDRESS = "0.0.0.1";

    public static final String KEY_CURRENT_USER = "_currentUser";

    private static final ThreadLocal<User> userHolder = new ThreadLocal<User>();

    private static final ThreadLocal<String> remoteAddressHolder = new ThreadLocal<String>();

    private static final ThreadLocal<Locale> localeHolder = new ThreadLocal<Locale>();

    private static final ThreadLocal<ServletRequest> requestHolder = new ThreadLocal<ServletRequest>();

    /**
     * 取得当前用户
     * <p/>
     * 如果在<code>userHolder</code>中没值，则直接从session取得，如果session中没值，则返回<code>null</code>;
     *
     * @return
     */
    public static User getCurrentUser() {
        User user = userHolder.get();
        if (user == null) {
            HttpServletRequest request = (HttpServletRequest) getRequest();
            if (request != null) {
                user = (User) request.getSession().getAttribute(KEY_CURRENT_USER);
                userHolder.set(user);
            }
        }
        return user;
    }

    /**
     * 设置当前登录用户
     *
     * @param user
     */
    public static void setCurrentUser(User user) {
        Assert.notNull(user);
        if (getCurrentUser() != null) {
            logger.info("replace current user : " + getCurrentUser() + " with new user: " + user);
        } else {
            logger.info("set current user : " + user);
        }
        userHolder.set(user);
    }

    /**
     * 取得当前登录用户的ID
     *
     * @return 用户ID
     * @see #getCurrentUser()
     */
    public static String getCurrentUserId() {
        User user = userHolder.get();
        return (user == null) ? null : user.getId();
    }

    /**
     * 取得当前登录用户的登录名称
     *
     * @return 用户登录名称
     * @see #getCurrentUser()
     */
    public static String getCurrentUserName() {
        UserDetails user = userHolder.get();
        return (user == null) ? null : user.getUsername();
    }

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
        logger.info("set remote address : " + remoteAddress);
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
        logger.info("set request locale : " + locale);
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
        logger.info("set request : " + request);
        requestHolder.set(request);
    }

    /**
     * 取得当前使用的时区
     * <p/>
     * 先从登录用户中取得，当用户没有保存时区时，从当前请求中取得，
     *
     * @return
     */
    public static Locale resolveCurrentLocale() {
//        UserDetails user = getCurrentUser();
//        if (user != null && user.getUserPreference() != null && user.getUserPreference().getLocale() != null) {
//            return user.getUserPreference().getLocale();
//        }
        Locale requestLocale = getRequestLocale();
        if (requestLocale != null) {
            return requestLocale;
        }
        return DEFAULT_LOCALE;
    }

    /**
     * 取得当前用户的字符集，现在只支持utf-8
     *
     * @return
     */
    public static String getCurrentUserCharset() {
        return DEFAULT_CHARSET;
    }

    /**
     * 取得用户的最后登录时间
     *
     * @return
     */
    public static String getUserLastLoginTime() {
        User user = getCurrentUser();
        Date loginTime = user.getLastLoginTime();
        return DateUtils.format(loginTime);
    }

    /**
     * 取得当前时间
     *
     * @return
     */
    public static Date getCurrentUserLocalDate() {
        return new Date();
    }
}
