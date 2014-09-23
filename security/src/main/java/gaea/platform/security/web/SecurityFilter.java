package gaea.platform.security.web;

import gaea.foundation.core.config.SystemConfig;
import gaea.foundation.core.utils.MessageUtils;
import gaea.foundation.core.utils.Utils;
import gaea.platform.security.ResourceDefinitionResolver;
import gaea.platform.security.internal.ResourceDefinitionResolverImpl;
import gaea.platform.security.support.AbstractSecurityInterceptor;
import gaea.platform.security.support.SecurityConstant;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;

import javax.servlet.*;
import java.io.IOException;
import java.util.Collections;

/**
 * 安全过滤类
 */
public class SecurityFilter extends AbstractSecurityInterceptor implements Filter {

    private static final String FILTER_APPLIED = "__aframe_security_SecurityFilter_filterApplied";

    private boolean observeOncePerRequest = true;

    private SecurityMetadataSource securityMetadataSource;

    private ResourceDefinitionResolver resourceDefinitionResolver = new ResourceDefinitionResolverImpl();

    public void destroy() {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        invoke(fi);
    }

    /**
     * @param fi
     * @throws java.io.IOException
     * @throws ServletException
     */
    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        if ((fi.getRequest() != null) && (fi.getRequest().getAttribute(FILTER_APPLIED) != null)
                && observeOncePerRequest) {
            // 如果在本次请求中已经验证通过，则不再次进行验证。
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } else {
            // 第一次请求时处理
            if (fi.getRequest() != null) {
                fi.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
            }
            // 检查请求的路径为系统要验证的资源路径且不在排除路径之内，则验证路径是否可访问
            if (isAuthenticationUrl(fi.getRequestUrl()) && !isExcludeAuthenticationUrl(fi.getRequestUrl())) {
                this.checkAnonymousAuthority(fi);
                this.checkAuthority(fi);
            }
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        }
    }

    public void init(FilterConfig arg0) throws ServletException {

    }

    /**
     * 检查是否可以匿名访问
     * <p/>
     * 如果不可以匿名访问且没有登录用户，则直接抛出异常
     *
     * @param fi
     * @throws java.io.IOException, ServletException
     */
    protected void checkAnonymousAuthority(FilterInvocation fi) throws IOException, ServletException {
        boolean enableFlag = Utils.toBoolean(SystemConfig.Instance.getProperty(SecurityConstant.SECURITY_ENABLE_ANONYMOUSE, "true"));
        // 如果不允许匿名访问，则检查是否有登录用户，如果没有则抛出异常
        if (!enableFlag) {
            Authentication authenticated = SecurityContextHolder.getContext().getAuthentication();
            // 如果为匿名访问，则抛出异常
            if (authenticated == null || authenticated instanceof AnonymousAuthenticationToken) {
                String errorMessage = MessageUtils.format("login_error_unlogin");
                AccessDeniedException accessDeniedException = new AccessDeniedException(errorMessage);
                fi.getHttpRequest().getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", accessDeniedException);
                AuthorizationFailureEvent event = new AuthorizationFailureEvent(fi, Collections.EMPTY_LIST, authenticated, accessDeniedException);
                publishEvent(event);
                throw accessDeniedException;
            }
        }
    }

    /**
     * 检查请求的路径是否为系统不要验证的资源路径
     * <p/>
     * 如果要变更系统不要验证的资源路径，则设置resource.authentication.exclude.matchtype、
     * resource.authentication.exclude.url这两个参数
     *
     * @param requestUrl
     * @return
     */
    protected boolean isExcludeAuthenticationUrl(String requestUrl) {
        String[] extUrls = SecurityConstant.getExcludeAuthenticationUrls();
        String url = resourceDefinitionResolver.preProcessUrl(requestUrl);
        for (String authorityUrl : extUrls) {
            if (resourceDefinitionResolver.isResourceMatch(url, authorityUrl, SecurityConstant.getExcludeAuthenticationMatchtype())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查请求的路径是否为系统要验证的资源路径
     * <p/>
     * 如果要变更系统要验证的资源路径，则设置resource.authentication.matchtype、
     * resource.authentication.url这两个参数
     *
     * @param requestUrl
     * @return
     */
    protected boolean isAuthenticationUrl(String requestUrl) {
        String[] authUrls = SecurityConstant.getAuthenticationUrls();
        String url = resourceDefinitionResolver.preProcessUrl(requestUrl);
        for (String authorityUrl : authUrls) {
            if (resourceDefinitionResolver.isResourceMatch(url, authorityUrl, SecurityConstant.getAuthenticationMatchtype())) {
                return true;
            }
        }
        return false;
    }

    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return securityMetadataSource;
    }

    public void setSecurityMetadataSource(SecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
    }

    public Class<? extends Object> getSecureObjectClass() {
        return FilterInvocation.class;
    }
}
