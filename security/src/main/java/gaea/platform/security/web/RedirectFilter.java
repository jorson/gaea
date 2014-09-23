package gaea.platform.security.web;


import gaea.platform.security.support.SecurityConstant;
import org.apache.log4j.Logger;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RedirectFilter implements Filter {

    private static final Logger logger = Logger.getLogger(RedirectFilter.class);

    private PathMatcher pathMatcher = new AntPathMatcher();

    private String PATH_PATTERN = "/**/*.do";

    private String INNER_PAGE_REQUEST_KEY = "innerPage";

    public void init(FilterConfig arg0) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        HttpServletRequest httpServletRequest = fi.getRequest();
        HttpSession session = httpServletRequest.getSession();
        String uri = httpServletRequest.getRequestURI();
        // 非ajax请求，非嵌入页面，匹配*.do、/
        if (!isAjaxRequest(httpServletRequest) && httpServletRequest.getParameter(INNER_PAGE_REQUEST_KEY) == null
                && (pathMatcher.match(PATH_PATTERN, uri) || "/".equals(uri))) {
            session.setAttribute(SecurityConstant.USER_LASTACTION_URL, fi.getRequestUrl());
            if (logger.isDebugEnabled()) {
                logger.debug("add session attribute USER_LASTACTION_URL:" + fi.getRequestUrl());
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * 判断是否为Ajax请求
     *
     * @param request HttpServletRequest
     * @return 是true, 否false
     */
    public boolean isAjaxRequest(HttpServletRequest request) {
        String requestType = request.getHeader("X-Requested-With");
        if (requestType != null && requestType.equals("XMLHttpRequest")) {
            return true;
        } else {
            return false;
        }
    }
}
