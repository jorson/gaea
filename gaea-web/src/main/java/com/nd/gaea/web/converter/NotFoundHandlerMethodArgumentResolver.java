package com.nd.gaea.web.converter;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 *
 */
public class NotFoundHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //     return UriComponentsBuilder.class.isAssignableFrom(parameter.getParameterType());
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        throw new NoHandlerFoundException("", "", null);
        //return "{code:404}";
//        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
//        return ServletUriComponentsBuilder.fromServletMapping(request);
    }

}
