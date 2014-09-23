package gaea.platform.security.web;


import gaea.platform.security.access.User;
import gaea.platform.security.context.UserContext;
import gaea.platform.security.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 设置AppUserContext中的相关信息
 *
 * @author wuhy
 */
public class UserContextFilter implements Filter {

    private static final String FILTER_APPLIED = "__aframe_security_AppUserContextFilter_filterApplied";

    private UserService userService;

    public void init(FilterConfig arg0) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        // 在每次请求只会处理一次。
        if (request.getAttribute(FILTER_APPLIED) != null) {
            chain.doFilter(request, response);
        } else {
            // 第一次时，设置值为TRUE。
            request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                UserContext.setCurrentUser((User) principal);
                // 把用户放入request
                request.setAttribute(UserContext.KEY_CURRENT_USER, principal);
            } else {
                // 未登录，设置匿名用户
                User anonymousUser = userService.getAnonymousUser();
                UserContext.setCurrentUser(anonymousUser);
                ((HttpServletRequest) request).getSession().setAttribute(UserContext.KEY_CURRENT_USER, anonymousUser);
            }
            UserContext.setRequest(request);
            UserContext.setRemoteAddress(request.getRemoteAddr());
            UserContext.setRequestLocale(request.getLocale());
            chain.doFilter(request, response);
        }
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
