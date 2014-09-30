package gaea.platform.web.aop;

import gaea.access.hibernate.support.HibernateUtils;
import gaea.foundation.core.utils.ObjectUtils;
import gaea.foundation.core.utils.StringUtils;
import gaea.platform.web.exception.OpenApiException;
import gaea.platform.web.openapi.OpenApi;
import gaea.platform.web.openapi.OpenApiCode;
import gaea.platform.web.openapi.OpenApiProtocol;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by way on 14-5-26.
 *
 * 利用aop来规范返回的接口格式
 */
@Component
@Aspect
public class OpenApiAop {

    /**
     * LOGGER对象
     */
    private static final Log LOGGER = LogFactory.getLog(OpenApiAop.class);


    /**
     * Code 参数说明
     */
    private static final int CODE_SUCCESS = 0;//成功

    private static final int CODE_UNKNOWN = 401;//未知错误

    /**
     * OPEN API JSONP支持的回调参数jsonpcallback（jquery默认）
     */
    private static final String JSON_CALLBACK = "jsonpcallback";


    /**
     * 处理api调用时，规范不同api返回格式。
     *
     * @param joinPoint
     * @param openApi
     * @return
     * @throws Throwable
     */
    @Around("@annotation(openApi)")
    public Object around(ProceedingJoinPoint joinPoint, OpenApi openApi) throws Throwable {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(openApi.value());
        }
        String str = null;
        if (openApi.value() == OpenApi.ApiType.openapi) {
            str = this.handleOpen(joinPoint);
        } else if (openApi.value() == OpenApi.ApiType.jsonp) {
            str = this.handleJsonP(joinPoint);
        } else if (openApi.value() == OpenApi.ApiType.openAndJsonp) {
            str = this.handleOpenAndJsonP(joinPoint);
        } else {
            str = this.handleAjax(joinPoint);
        }
        return str;
    }

    /*
      两种情况都有的 格式
     */
    private String handleOpenAndJsonP(ProceedingJoinPoint joinPoint) throws Throwable {
        if (StringUtils.hasText(this.getJsonPCallback(joinPoint.getArgs()))) {
            return handleJsonP(joinPoint);
        } else {
            return handleOpen(joinPoint);
        }
    }

    /*
     * ajaxApi 格式
     */
    private String handleAjax(ProceedingJoinPoint joinPoint) throws Throwable {
        Map map = new HashMap();
        int code;
        String error = null;
        String exception = null;
        try {

            return ObjectUtils.toJson(HibernateUtils.resolveRealObject(joinPoint.proceed()));//成功时数据不进行封装
        } catch (OpenApiException e) {
            LOGGER.info("ajaxApi业务异常", e);
            code = e.getCode();
            error = e.getMessage();
            exception = e.getException();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            code = CODE_UNKNOWN;
            error = e.getMessage();
            exception = e.getLocalizedMessage();
        }
        map.put("code", code);
        map.put("error", error);
        map.put("exception", exception);
        return ObjectUtils.toJson(map);
    }

    /*
     *openApi 格式
     */
    private String handleOpen(ProceedingJoinPoint joinPoint) throws Throwable {
        OpenApiProtocol openApiProtocol = new OpenApiProtocol();
        try {
            openApiProtocol.setCode(OpenApiCode.SUCCESS.getValue());
            openApiProtocol.setData(HibernateUtils.resolveRealObject(joinPoint.proceed()));
        } catch (OpenApiException e) {
            LOGGER.info("openApi业务异常", e);
            openApiProtocol.setCode(e.getCode());
            openApiProtocol.setMessage(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            openApiProtocol.setCode(OpenApiCode.EXCEPTION.getValue());
            openApiProtocol.setMessage(e.getMessage());
        }
        return ObjectUtils.toJson(openApiProtocol);
    }

    /*
     *jsonP 格式
     */
    private String handleJsonP(ProceedingJoinPoint joinPoint) throws Throwable {
        String callback = this.getJsonPCallback(joinPoint.getArgs());
        return callback + "(" + this.handleOpen(joinPoint) + ")";
    }

    /*
     获取callback的js回调函数名
     */
    private String getJsonPCallback(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof HttpServletRequest) {
                return ((HttpServletRequest) args[i]).getParameter(JSON_CALLBACK);
            }
        }
        return null;
    }


}
