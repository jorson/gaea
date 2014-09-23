package gaea.platform.security.web;


import gaea.platform.security.UserAuthorizeService;
import gaea.platform.security.support.SecurityConstant;
import gaea.platform.security.utils.AuthUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterInvocation;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 第一个过滤器
 * <p/>
 * Created by jsc on 14-7-12.
 */
public class FirstFilter implements Filter {

    private UserAuthorizeService userAuthorizeService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        invoke(fi);
    }

    /**
     * 执行处理
     *
     * @param fi
     * @throws java.io.IOException
     * @throws ServletException
     */
    private void invoke(FilterInvocation fi) throws IOException, ServletException {
        HttpServletRequest request = fi.getRequest();
        // 遍历request参数名
        Enumeration names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            // 找到匹配的assessToken参数
            if (name.equals(SecurityConstant.ASSESS_TOKEN_PARAMETER_KEY)) {
                String accessToken = request.getParameter(SecurityConstant.ASSESS_TOKEN_PARAMETER_KEY);
                // 认证accessToken
                UserDetails user = userAuthorizeService.validAccessToken(accessToken);
                // 设置session
                AuthUtils.setSessionAuthentication(request, this.createAuthenticationToken(user));
            }
        }
        fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
    }

    /**
     * 创建认证凭证
     *
     * @param user
     * @return
     */
    private Authentication createAuthenticationToken(UserDetails user) {
        // 构造spring的token信息
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        token.setDetails(user);
        return token;
    }

    @Override
    public void destroy() {

    }

    public UserAuthorizeService getUserAuthorizeService() {
        return userAuthorizeService;
    }

    public void setUserAuthorizeService(UserAuthorizeService userAuthorizeService) {
        this.userAuthorizeService = userAuthorizeService;
    }
}
