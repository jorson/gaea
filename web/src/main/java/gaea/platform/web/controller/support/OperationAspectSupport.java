package gaea.platform.web.controller.support;

/**
 * 利用AOP来规范返回的数据格式
 *
 * @author bifeng.liu
 */

import gaea.access.hibernate.support.HibernateUtils;
import gaea.foundation.core.utils.ObjectUtils;
import gaea.foundation.core.utils.StringUtils;
import gaea.foundation.core.utils.Utils;
import gaea.platform.web.controller.ControllerResult;
import gaea.platform.web.controller.ErrorControllerResult;
import gaea.platform.web.controller.OpenApiControllerResult;
import gaea.platform.web.exception.ControllerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

@Component
@Aspect
public class OperationAspectSupport {
    /**
     * logger对象
     */
    private static Log logger = LogFactory.getLog(OperationAspectSupport.class);

    /**
     * 成功的值
     */
    private static final int CODE_SUCCESS = 0;
    /**
     * 未知错误的值
     */
    private static final int CODE_UNKNOWN = 401;
    /**
     * OPEN API JSONP支持的回调参数jsonpcallback（jquery默认）
     */
    private static final String JSON_CALLBACK = "jsonpcallback";

    /**
     * 处理请求调用时，根据方法注释的类型不同，将返回不同的格式。
     *
     * @param joinPoint 切入点
     * @param operation 操作注解
     * @return
     * @throws Throwable
     */
    @Around("@annotation(operation)")
    public Object around(ProceedingJoinPoint joinPoint, Operation operation) throws Throwable {
        if (logger.isDebugEnabled()) {
            logger.debug(operation.value());
        }
        switch (operation.value()) {
            case OPENAPI:
                return handleOpenApi(joinPoint);
            case JSONP:
                return handleJsonp(joinPoint);
            case AJAXAPI:
                return handleAjaxApi(joinPoint);
            default:
                return handlePage(joinPoint);
        }
    }

    /**
     * 两种情况都有的 格式
     */
    private Object handlePage(final ProceedingJoinPoint joinPoint) throws Throwable {
//        Object ret = new ActionRedirector() {
//            public Object doAction(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Throwable {
//                return  HibernateUtils.resolveRealObject(joinPoint.proceed());
//            }
//        }.redirect(this.request, this.response, modelMap, isJson);
        return null;
    }

    /**
     * 两种情况都有的 格式
     */
    private String handleOpenAndJsonp(ProceedingJoinPoint joinPoint) throws Throwable {
        String callback = this.getJsonpCallbackFunc(joinPoint.getArgs());
        if (StringUtils.hasText(callback)) {
            return handleJsonp(joinPoint);
        } else {
            return handleOpenApi(joinPoint);
        }
    }

    /**
     * 处理JSONP格式
     */
    private String handleJsonp(ProceedingJoinPoint joinPoint) throws Throwable {
        String callback = this.getJsonpCallbackFunc(joinPoint.getArgs());
        return callback + "(" + this.handleOpenApi(joinPoint) + ")";
    }

    /**
     * Ajax Api 格式
     */
    private String handleAjaxApi(ProceedingJoinPoint joinPoint) throws Throwable {
        Throwable ex = null;
        try {
            Object obj = HibernateUtils.resolveRealObject(joinPoint.proceed());
            return ObjectUtils.toJson(obj);
        } catch (Exception e) {
            logger.error("处理AJAX请求时出错", ex);
            ErrorControllerResult actionResult = (ErrorControllerResult) wrapException(e, true);
            actionResult.setException(ex);
            return ObjectUtils.toJson(actionResult);
        }
    }

    /**
     * 处理OpenApi
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    private String handleOpenApi(ProceedingJoinPoint joinPoint) throws Throwable {
        Throwable ex = null;
        OpenApiControllerResult result = null;
        try {
            Object obj = HibernateUtils.resolveRealObject(joinPoint.proceed());

            if (obj instanceof OpenApiControllerResult) {
                result = (OpenApiControllerResult) obj;
            } else {
                result = new OpenApiControllerResult();
                result.setData(obj);
            }
        } catch (Exception e) {
            logger.error("处理AJAX请求(Open Api)时出错", e);
            result = (OpenApiControllerResult) wrapException(e, true);
        }
        return ObjectUtils.toJson(result);
    }

    /**
     * 根据异常取得返回值
     *
     * @param ex
     * @param isOpenApi
     * @return
     */
    private ControllerResult wrapException(final Exception ex, boolean isOpenApi) {
        Throwable throwable = null;
        int code = CODE_UNKNOWN;
        if (ex instanceof InvocationTargetException) {
            InvocationTargetException targetException = (InvocationTargetException) ex;
            throwable = targetException.getTargetException() != null ? targetException.getTargetException() : targetException;
        } else if (ex instanceof InvocationTargetException) {
            ControllerException targetException = (ControllerException) ex;
            code = Utils.toInt(targetException.getCode(), CODE_UNKNOWN);
        }

        ControllerResult result = null;
        if (isOpenApi) {
            result = new OpenApiControllerResult();
        } else {
            result = new ErrorControllerResult();
        }
        result.setCode(code);
        result.setMessage(ex.getMessage());
        return result;
    }

    /**
     * 获取从前端传递过来的callback的js回调函数名
     *
     * @param args 参数列表
     */
    private String getJsonpCallbackFunc(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof HttpServletRequest) {
                return ((HttpServletRequest) args[i]).getParameter(JSON_CALLBACK);
            }
        }
        return null;
    }
}

