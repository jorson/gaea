package gaea.platform.security;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义登录接口
 */
public interface LoginAspect {

    /**
     * 登录之前处理该事务
     *
     * @param authentication
     * @param request
     * @param response
     */
    public abstract void beforeLogin(Authentication authentication, HttpServletRequest request,
                                     HttpServletResponse response);

    /**
     * 登录之后处理该事务
     *
     * @param result         登录返回结果
     * @param authentication
     * @param request
     * @param response
     */
    public abstract void afterLogin(int result, Authentication authentication, HttpServletRequest request,
                                    HttpServletResponse response);
}
