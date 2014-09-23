package gaea.platform.security.utils;

import gaea.platform.security.access.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录认证工具类
 */
public class AuthUtils {

    /**
     * 获取登陆信息
     *
     * @return Authentication
     */
    public static Authentication getSessionAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 设置登陆信息
     *
     * @param request        HttpServletRequest
     * @param authentication Authentication
     */
    public static void setSessionAuthentication(HttpServletRequest request, Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }

    /**
     * 是否已登陆
     *
     * @param authentication Authentication
     * @return boolean
     */
    public static boolean isLogin(Authentication authentication) {
        return authentication != null && !"anonymousUser".equals(authentication.getName()) && authentication.isAuthenticated();
    }

    /**
     * 是否已登陆
     *
     * @return boolean
     */
    public static boolean isLogin() {
        return isLogin(getSessionAuthentication());
    }


    /**
     * 获取用户对象
     *
     * @return User (not exists is null)
     */
    public static User getLoginUser() {
        Authentication a = getSessionAuthentication();
        return (a != null && a.isAuthenticated() && a.getPrincipal() instanceof User) ? (User) a.getPrincipal() : null;
    }

    /**
     * 获取用户ID
     *
     * @return User (not exists is null)
     */
    public static Long getLoginUserId() {
        User user = getLoginUser();
        if (user != null && user.getId() != null) {
            return Long.valueOf(user.getId());
        }
        return null;
    }
}

