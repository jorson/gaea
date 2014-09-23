package gaea.platform.security.support;

import gaea.foundation.core.config.SystemConfig;

/**
 * Security常量，用于定义配置时的KEY
 * 与login.properties键相对应
 *
 * @author wuhy
 */
public class SecurityConstant {

    private SecurityConstant() {

    }

    /**
     * 是否开启自定义的登录处理
     */
    public static final String LOGIN_USE_CUSTOM = "login.use.custom";
    /**
     * 自定义的登录类
     */
    public static final String LOGIN_ASCPECT_CLASS = "login.ascpect.class";
    /**
     * 自定义的登出类
     */
    public static final String LOGOUT_HANDLER_CLASS = "logout.handler.class";
    /**
     * 自定义的安全检查类
     */
    public static final String SECURITY_CHECK_CLASS = "security.check.class";
    /**
     * 保存用户上次登录时间的KEY
     */
    public static final String USER_LASTLOGINTIME_KEY = "USER_LASTLOGINTIME_KEY";
    /**
     * 保存用户是否已经检查过密码周期的KEY
     */
    public static final String USER_PASSWORDCYCLE_CHECKED_KEY = "USER_PASSWORDCYCLE_CHECKED_KEY";
    /**
     * 用户上次访问的地址
     */
    public static final String USER_LASTACTION_URL = "USER_LASTACTION_URL";

    /**
     * 最后登录用户的KEY
     */
    public static final String LASTEST_LOGIN_USERNAME_KEY = "lastest.login.username";

    /**
     * 是否允许匿名
     */
    public static final String SECURITY_ENABLE_ANONYMOUSE = "security.enable.anonymouse";

    /**
     * 是否允许访问所有资源
     */
    public static final String SECURITY_ALLOWALLRESOURCES = "security.allowallresources";


    /**
     * 验证码
     */
    public static final String CAPTCHA_CHARS = "captcha.chars";
    public static final String CAPTCHA_WIDTH = "captcha.width";
    public static final String CAPTCHA_HEIGHT = "captcha.height";
    public static final String CAPTCHA_WORDS_MIN = "captcha.words_min";
    public static final String CAPTCHA_WORDS_MAX = "captcha.words_max";
    public static final String CAPTCHA_FONTSIZE_MIN = "captcha.fontsize_min";
    public static final String CAPTCHA_FONTSIZE_MAX = "captcha.fontsize_max";
    public static final String CAPTCHA_SESSION_KEYNAME = "captcha.session.keyname";
    public static final String CAPTCHA_ENABLED = "captcha.enabled";
    public static final String CAPTCHA_FIELD_NAME = "captcha.field.name";
    public static final String CAPTCHA_IGNORE_CASE = "captcha.ignore.case";


    /**
     * 资源匹配是否忽略大小写
     */
    public static final String RESOURCE_COMPARISON_IGNORE_CASE = "resource.comparison.ignore.case";

    /**
     * OAuth配置参数
     */
    public static final String OAUTH_SERVICEPATH = "oauth.servicepath";
    public static final String OAUTH_SERVICEHOST = "oauth.servicehost";
    public static final String OAUTH_PROVIDER = "oauth.provider";
    public static final String OAUTH_CREDENTIALSCLIENT_CLIENTID = "oauth.credentialsclient.clientid";
    public static final String OAUTH_CREDENTIALSCLIENT_CLIENTSECRET = "oauth.credentialsclient.clientsecret";
    public static final String OAUTH_CREDENTIALSCLIENT_CLIENTCODE = "oauth.credentialsclient.clientcode";
    public static final String OAUTH_CREDENTIALSCLIENT_PLATCODE = "oauth.credentialsclient.platcode";

    /**
     * url认证assessToken参数名称
     */
    public static final String ASSESS_TOKEN_PARAMETER_KEY = "accessToken";

    /**
     * 系统要验证的资源路径
     */
    public static final String AUTHENTICATION_URLS = "authentication.urls";

    /**
     * 系统要验证的资源类型
     */
    public static final String AUTHENTICATION_MATCHTYPE = "authentication.matchtype";

    /**
     * 系统不要验证的资源路径
     */
    public static final String EXCLUDE_AUTHENTICATION_URLS = "exclude.authentication.urls";

    /**
     * 系统不要验证的资源类型
     */
    public static final String EXCLUDE_AUTHENTICATION_MATCHTYPE = "exclude.authentication.matchtype";

    /**
     * 系统要验证的资源路径
     */
    private static String[] authenticationUrls;

    /**
     * 系统要验证的资源类型
     */
    private static ResourceMatchType authenticationMatchtype;

    /**
     * 系统不要验证的资源路径
     */
    private static String[] excludeAuthenticationUrls;

    /**
     * 系统不要验证的资源类型
     */
    private static ResourceMatchType excludeAuthenticationMatchtype;

    /**
     * 分割标记
     */
    private static final String URL_SPLIT_KEY = ";";

    public static String[] getAuthenticationUrls() {
        if (authenticationUrls == null) {
            authenticationUrls = SystemConfig.Instance.getProperty(AUTHENTICATION_URLS, "").split(URL_SPLIT_KEY);
        }
        return authenticationUrls;
    }

    public static String[] getExcludeAuthenticationUrls() {
        if (excludeAuthenticationUrls == null) {
            excludeAuthenticationUrls = SystemConfig.Instance.getProperty(EXCLUDE_AUTHENTICATION_URLS, "").split(URL_SPLIT_KEY);
        }
        return excludeAuthenticationUrls;
    }

    public static String getLoginUseCustom() {
        return LOGIN_USE_CUSTOM;
    }

    public static ResourceMatchType getAuthenticationMatchtype() {
        if (authenticationMatchtype == null) {
            authenticationMatchtype = ResourceMatchType.getEnum(SystemConfig.Instance.getProperty(AUTHENTICATION_MATCHTYPE, ResourceMatchType.URL.value()));
        }
        return authenticationMatchtype;
    }

    public static ResourceMatchType getExcludeAuthenticationMatchtype() {
        if (excludeAuthenticationMatchtype == null) {
            excludeAuthenticationMatchtype = ResourceMatchType.getEnum(SystemConfig.Instance.getProperty(EXCLUDE_AUTHENTICATION_MATCHTYPE, ResourceMatchType.URL.value()));
        }
        return excludeAuthenticationMatchtype;
    }
}
