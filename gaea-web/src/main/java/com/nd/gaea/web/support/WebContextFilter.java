package com.nd.gaea.web.support;

import com.nd.gaea.web.context.WebContext;

import javax.servlet.*;
import java.io.IOException;

/**
 * 设置WebContext中的相关信息
 *
 * @author bifeng.liu
 */
public class WebContextFilter implements Filter {

    private static final String FILTER_APPLIED = "_gaea_WebContextFilter_filterApplied";


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
            WebContext.setRequest(request);
            WebContext.setResponse(response);
            WebContext.setRemoteAddress(request.getRemoteAddr());
            WebContext.setRequestLocale(request.getLocale());
            chain.doFilter(request, response);
        }
    }
}
