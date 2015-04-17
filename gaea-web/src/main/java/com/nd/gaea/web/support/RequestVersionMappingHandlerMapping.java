package com.nd.gaea.web.support;

import com.nd.gaea.utils.StringUtils;
import com.nd.gaea.web.controller.support.ControllerInfo;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UrlPathHelper;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p>支持Version参数的Mapping</p>
 * 在请求时判断Accept中有带有有效的version参数，则会自动匹配到相应的路径
 * <p/>
 * <code>
 * request:
 * <p>GET:/simple/list</p>
 * <p>HOST: localhost</p>
 * <p>Accept: application/json; version=2</p>
 * </code>
 * 就会自动匹配/v2/simple/list路径
 *
 * @author bifeng.liu
 * @see UrlPathVersionHelper
 * @since 2014/11/19
 */
public class RequestVersionMappingHandlerMapping extends RequestMappingHandlerMapping {
    /**
     * 与Version相关URL路径帮助类
     */
    private UrlPathHelper urlPathHelper = new UrlPathVersionHelper();

    /**
     * 通过方法取得Mapping
     * <p/>
     * 如果方法对应的Controller注释了ControllerInfo且version不为空，则在路由加入前缀/v+version
     *
     * @param method
     * @param handlerType
     * @return
     */
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo info = null;
        RequestMapping methodAnnotation = AnnotationUtils.findAnnotation(method, RequestMapping.class);
        if (methodAnnotation != null) {
            RequestCondition<?> methodCondition = getCustomMethodCondition(method);
            info = createRequestMappingInfo(methodAnnotation, methodCondition);
            RequestMapping typeAnnotation = AnnotationUtils.findAnnotation(handlerType, RequestMapping.class);
            ControllerInfo infoAnnotation = AnnotationUtils.findAnnotation(handlerType, ControllerInfo.class);
            if (typeAnnotation != null) {
                RequestCondition<?> typeCondition = getCustomTypeCondition(handlerType);
                info = createRequestMappingInfo(typeAnnotation, typeCondition).combine(info);
                //如果Controller注释了ControllerInfo且version不为空，则在路由加入前缀/v+version
                if (infoAnnotation != null && StringUtils.isNotEmpty(infoAnnotation.version())) {
                    String version = infoAnnotation.version();
                    Set<String> patterns = info.getPatternsCondition().getPatterns();
                    Set<String> results = new LinkedHashSet<String>(patterns.size());
                    for (String pattern : patterns) {
                        results.add("/v" + version + pattern);
                    }
                }
            }
        }
        return info;
    }

    /**
     * 取得使用URL路径帮助类
     * <p/>
     * 重写父类方法，返回与Version相关的URL路径帮助类
     *
     * @return
     */
    public UrlPathHelper getUrlPathHelper() {
        return urlPathHelper;
    }
}
