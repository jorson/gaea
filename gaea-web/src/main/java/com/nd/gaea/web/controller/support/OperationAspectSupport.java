package com.nd.gaea.web.controller.support;


import com.nd.gaea.utils.BeanUtils;
import com.nd.gaea.utils.ObjectUtils;
import com.nd.gaea.utils.StringUtils;
import com.nd.gaea.web.controller.ErrorControllerResult;
import com.nd.gaea.web.controller.OpenApiControllerResult;
import com.nd.gaea.web.exception.ControllerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.ServletRequest;

/**
 * 利用AOP来规范返回的数据格式
 *
 * @author bifeng.liu
 */
//@Component
//@Aspect
public class OperationAspectSupport {
    /**
     * logger对象
     */
    private static Log logger = LogFactory.getLog(OperationAspectSupport.class);


    /**
     * 处理请求调用时，根据方法注释的类型不同，将返回不同的格式。
     *
     * @param joinPoint 切入点
     * @param operation 操作注解
     * @return
     * @throws Throwable
     */
    //@Around("@annotation(operation)")
    public Object around(ProceedingJoinPoint joinPoint, Operation operation) throws Throwable {
        if (logger.isDebugEnabled()) {
            logger.debug("---- " + joinPoint.toLongString() + " Start ;Operation:" + operation.value() + " ----");
        }
        Object result = null;
        switch (operation.value()) {
            case OPENAPI:
                result = handleOpenApi(joinPoint);
                break;
            case JSONP:
                result = handleJsonp(joinPoint);
                break;
            case OPENJSONP:
                result = handleOpenJsonp(joinPoint);
                break;
            case AJAXAPI:
                result = handleAjaxApi(joinPoint);
                break;
            default:
                result = handlePage(joinPoint);
                break;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("---- " + joinPoint.toLongString() + " End;----");
        }
        return result;
    }

    /**
     * 处理默认的请求
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    private Object handlePage(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Throwable ex) {
            logger.error("处理默认请求时出错", ex);
            throw ex;
        }
    }

    /**
     * 两种情况都有的 格式
     */
    /**
     * 处理OPENAPI和JSONP格式
     * <p/>
     * 当有传入回调函数 OperationAspectSupport@JSON_CALLBACK时，使用JSONP格式返回，
     * 没有传入时使用OpenApi格式返回
     *
     * @param joinPoint
     * @return
     */
    private String handleOpenJsonp(ProceedingJoinPoint joinPoint) {
        String callback = this.getJsonpCallbackFunc(joinPoint);
        if (StringUtils.hasText(callback)) {
            return handleJsonp(joinPoint);
        } else {
            return handleOpenApi(joinPoint);
        }
    }

    /**
     * 处理JSONP格式
     * <p/>
     * 该种格式必须传入回调函数 OperationAspectSupport@JSON_CALLBACK，否则报错
     *
     * @param joinPoint
     * @return
     */
    private String handleJsonp(ProceedingJoinPoint joinPoint) {
        String callback = this.getJsonpCallbackFunc(joinPoint);
        if (StringUtils.isEmpty(callback)) {
            return ObjectUtils.toJson(ControllerUtils.wrapException(new ControllerException("参数出错，请输入回调函数(" + ControllerConstants.JSON_CALLBACK + ")"), true));
        }
        return callback + "(" + this.handleOpenApi(joinPoint) + ")";
    }

    /**
     * 处理Ajax Api 格式
     * <p/>
     * 直接使用JSON返回相关对象
     *
     * @param joinPoint
     * @return 返回到前端的JSON
     */
    private String handleAjaxApi(ProceedingJoinPoint joinPoint) {
        try {
            Object obj = joinPoint.proceed();
            return ObjectUtils.toJson(obj);
        } catch (Throwable ex) {
            logger.error("处理AJAX请求时出错", ex);
            ErrorControllerResult actionResult = (ErrorControllerResult) ControllerUtils.wrapException(ex, false);
            // actionResult.setException(ex);  //TODO 先去掉，无法解决死循环的问题
            return ObjectUtils.toJson(actionResult);
        }
    }

    /**
     * 处理OpenApi
     *
     * @param joinPoint
     * @return
     */
    private String handleOpenApi(ProceedingJoinPoint joinPoint) {
        OpenApiControllerResult result = null;
        try {
            Object obj = joinPoint.proceed();
            if (obj != null && obj instanceof OpenApiControllerResult) {
                result = (OpenApiControllerResult) obj;
            } else {
                result = new OpenApiControllerResult();
                result.setData(obj);
            }
        } catch (Throwable throwable) {
            logger.error("处理AJAX请求(Open Api)时出错", throwable);
            result = (OpenApiControllerResult) ControllerUtils.wrapException(throwable, true);
        }
        return ObjectUtils.toJson(result);
    }


    /**
     * 获取从前端传递过来的callback的js回调函数名
     *
     * @param joinPoint
     */
    private String getJsonpCallbackFunc(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        // 取得处理方法的参数
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest) {
                return ((ServletRequest) args[i]).getParameter(ControllerConstants.JSON_CALLBACK);
            }
        }
        //
        Object target = joinPoint.getTarget();
        try {
            Object ret = BeanUtils.forceGetProperty(target, "request");
            if (ret instanceof ServletRequest) {
                return ((ServletRequest) ret).getParameter(ControllerConstants.JSON_CALLBACK);
            }
        } catch (Exception ex) {
            logger.warn("get jsonp callback function happen error!", ex);
        }
        return null;
    }
}

