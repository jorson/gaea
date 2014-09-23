package gaea.platform.security.web;

import gaea.foundation.core.config.SystemConfig;
import gaea.platform.security.LoginAspect;
import gaea.platform.security.exception.AuthenticationException;
import gaea.platform.security.support.SecurityConstant;
import gaea.platform.security.web.captcha.CaptchaInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录认证处理器
 */
public class AuthenticationProcessingFilter extends UsernamePasswordAuthenticationFilter {

    private LoginAspect loginAspect = null;
    private Authentication authentication = null;
    /**
     * 验证码拦截类
     */
    private CaptchaInterceptor captchaInterceptor = new CaptchaInterceptor();
    protected final static int DEFAULT_SUCCESS_RESULT = 1;
    protected final static int DEFAULT_FAIL_RESULT = -1;
    public static final String SECURITY_LAST_USERNAME_KEY = "SECURITY_LAST_USERNAME";

    /**
     * 登录成功，处理操作
     *
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        if (loginAspect != null) {
            loginAspect.afterLogin(DEFAULT_SUCCESS_RESULT, obtainAuthentication(request), request, response);
        }
        super.successfulAuthentication(request, response, chain, authResult);
    }

    /**
     * 登录不成功，处理操作
     *
     * @param request
     * @param response
     * @param failed
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        if (loginAspect != null) {
            loginAspect.afterLogin(DEFAULT_FAIL_RESULT, obtainAuthentication(request), request, response);
        }
        super.unsuccessfulAuthentication(request, response, failed);
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        preLogin(request, response);
        // 验证验证码
        this.getCaptchaInterceptor().intercept(request, response);
        boolean useCustomLogin = SystemConfig.Instance.getBooleanProperty(SecurityConstant.LOGIN_USE_CUSTOM);
        // 如果开启，则处理自定义登录
        if (useCustomLogin) {
            try {
                if (loginAspect != null) {
                    loginAspect.beforeLogin(obtainAuthentication(request), request, response);
                }
            } catch (Exception bex) {
                logger.error("登录异常！", bex);
                try {
                    unsuccessfulAuthentication(request, response, new AuthenticationException(bex.getMessage()));
                } catch (Exception ex) {
                    logger.error("登录不成功，处理操作异常！", ex);
                }
            }
        }
        return super.attemptAuthentication(request, response);
    }

    public void preLogin(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = ((HttpServletRequest) request).getSession();
        session.setAttribute(SECURITY_LAST_USERNAME_KEY, request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY));
    }

    public LoginAspect getLoginAspect() {
        return loginAspect;
    }

    public void setLoginAspect(LoginAspect loginAspect) {
        this.loginAspect = loginAspect;
    }

    public CaptchaInterceptor getCaptchaInterceptor() {
        return captchaInterceptor;
    }

    public void setCaptchaInterceptor(CaptchaInterceptor captchaInterceptor) {
        this.captchaInterceptor = captchaInterceptor;
    }

    /**
     * 根据Request中传入参数，取得Authentication
     *
     * @param request
     * @return
     */
    protected Authentication obtainAuthentication(HttpServletRequest request) {
        if (authentication == null) {
            String username = obtainUsername(request);
            String password = obtainPassword(request);
            if (username == null) {
                username = "";
            }
            if (password == null) {
                password = "";
            }
            username = username.trim();
            authentication = new UsernamePasswordAuthenticationToken(username, password);
        }
        return authentication;
    }
}
