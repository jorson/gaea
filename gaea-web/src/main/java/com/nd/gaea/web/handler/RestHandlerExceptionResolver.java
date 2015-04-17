package com.nd.gaea.web.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ND on 2015/1/26.
 */
public class RestHandlerExceptionResolver extends AbstractHandlerExceptionResolver {
    /**
     * LOGGER对象
     */
    private static final Log LOGGER = LogFactory.getLog(RestHandlerExceptionResolver.class);

    @Override
    protected ModelAndView doResolveException(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        return null;
    }
}
