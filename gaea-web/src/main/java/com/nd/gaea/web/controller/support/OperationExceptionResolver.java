package com.nd.gaea.web.controller.support;

import com.nd.gaea.utils.ObjectUtils;
import com.nd.gaea.utils.StringUtils;
import com.nd.gaea.web.controller.ControllerResult;
import com.nd.gaea.web.controller.ErrorControllerResult;
import com.nd.gaea.web.exception.ControllerException;
import com.nd.gaea.web.model.CustomOutputModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 方法异常解决类
 * <p/>
 *
 * @author bifeng.liu
 */
public class OperationExceptionResolver extends SimpleMappingExceptionResolver {
    /**
     * logger对象
     */
    private final static Log LOGGER = LogFactory.getLog(OperationExceptionResolver.class);
    /**
     * 默认的字符集
     */
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    /**
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return
     */
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (handler != null && handler instanceof HandlerMethod) {
                ModelAndView view = handleHandlerMethodException(ex, request, response, handler);
                if (view != null) {
                    return view;
                }
            }
            if (ex instanceof NoSuchRequestHandlingMethodException) {
                return handleNotFound(ex, request, response, handler);
            } else if (ex instanceof NoHandlerFoundException) {
                return handleNotFound(ex, request, response, handler);
            } else if (ex instanceof HttpRequestMethodNotSupportedException) {
                return handleHttpRequestMethodNotSupported((HttpRequestMethodNotSupportedException) ex, request, response, handler);
            } else if (ex instanceof HttpMediaTypeNotSupportedException) {
                return handleHttpMediaTypeNotSupported((HttpMediaTypeNotSupportedException) ex, request, response, handler);
            } else if (ex instanceof TypeMismatchException) {
                return handleTypeMismatchException((TypeMismatchException) ex, request, response, handler);
            } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
                return handleHttpMediaTypeNotAcceptable((HttpMediaTypeNotAcceptableException) ex, request, response, handler);
            } else if (ex instanceof MissingServletRequestParameterException) {
                return handleBadRequest(ex, request, response, handler);
            } else if (ex instanceof ServletRequestBindingException) {
                return handleBadRequest(ex, request, response, handler);
            } else if (ex instanceof HttpMessageNotReadableException) {
                return handleBadRequest(ex, request, response, handler);
            } else if (ex instanceof MethodArgumentNotValidException) {
                return handleBadRequest(ex, request, response, handler);
            } else if (ex instanceof MissingServletRequestPartException) {
                return handleBadRequest(ex, request, response, handler);
            } else if (ex instanceof BindException) {
                return handleBadRequest(ex, request, response, handler);
            } else if (ex instanceof HttpMessageNotWritableException) {
                return handleServerError(ex, request, response, handler);
            } else if (ex instanceof ConversionNotSupportedException) {
                return handleServerError(ex, request, response, handler);
            } else if (ex instanceof ControllerException) {
                return handleServerError(ex, request, response, handler);
            }
        } catch (Exception handlerException) {
            logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
            //return handleServerError(ex, request, response, handler);
        }
        return super.doResolveException(request, response, handler, ex);
    }


    private ModelAndView handleHandlerMethodException(Exception ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        Method method = ((HandlerMethod) handler).getMethod();
        if (method.isAnnotationPresent(CustomOutputModel.class)) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            CustomOutputModel model = method.getAnnotation(CustomOutputModel.class);
            OperationType operationType = model.operationType();
            switch (operationType) {
                case OPENAPI:
                    return output(response, handler, ControllerUtils.wrapException(ex, true), null);
                case AJAXAPI:
                    return output(response, handler, ControllerUtils.wrapException(ex, false), null);
                case JSONP:
                    String callback = getJsonpCallbackFunc(request);
                    if (StringUtils.isEmpty(callback)) {
                        return output(response, handler, ControllerUtils.wrapException(new ControllerException("参数出错，请输入回调函数(" + ControllerConstants.JSON_CALLBACK + ")"), true), null);
                    } else {
                        return output(response, handler, ControllerUtils.wrapException(ex, true), callback);
                    }
                case OPENJSONP:
                    callback = this.getJsonpCallbackFunc(request);
                    return output(response, handler, ControllerUtils.wrapException(ex, true), callback);
            }
        } else if (method.isAnnotationPresent(ResponseBody.class)) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return output(response, handler, ControllerUtils.wrapException(ex, false), null);
        }
        return null;
    }

    /**
     * @param ex
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws IOException
     */
    protected ModelAndView handleNotFound(Exception ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        LOGGER.warn(ex.getMessage());
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return new ModelAndView();
    }

    /**
     * @param ex
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws IOException
     */
    protected ModelAndView handleBadRequest(Exception ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        LOGGER.warn(ex.getMessage());
        StringBuilder messages = new StringBuilder();
        messages.append("请求错误[");
        messages.append(ex.getMessage());
        messages.append("]");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return output(response, handler, generateErrorCode(HttpServletResponse.SC_BAD_REQUEST, 0), messages.toString(), null);
    }

    protected ModelAndView handleTypeMismatchException(TypeMismatchException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        LOGGER.warn(ex.getMessage());
        StringBuilder messages = new StringBuilder();
        messages.append("转换类型出错，值[" + ex.getValue() + "]，要求类型[");
        messages.append(ex.getRequiredType());
        messages.append("]");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return output(response, handler, generateErrorCode(HttpServletResponse.SC_BAD_REQUEST, 1), messages.toString(), null);
    }

    protected ModelAndView handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                                            HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        LOGGER.warn(ex.getMessage());
        StringBuilder messages = new StringBuilder();
        messages.append("类型不支持出错");
        response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        return output(response, handler, generateErrorCode(HttpServletResponse.SC_NOT_ACCEPTABLE, 0), messages.toString(), null);
    }

    protected ModelAndView handleServerError(Exception ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        LOGGER.warn(ex.getMessage());
        StringBuilder messages = new StringBuilder();
        // messages.append("服务器执行出错[");
        messages.append(ex.getMessage());
        //   messages.append("]");
        //request.setAttribute("javax.servlet.error.exception", ex);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return output(response, handler, generateErrorCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 0), messages.toString(), null);
    }

    protected ModelAndView handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                               HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        StringBuilder messages = new StringBuilder();
        messages.append("该请求不支持Method[");
        messages.append(ex.getMethod());
        messages.append("]");
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return output(response, handler, generateErrorCode(HttpServletResponse.SC_METHOD_NOT_ALLOWED, 0), messages.toString(), null);
    }

    protected ModelAndView handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                           HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        StringBuilder messages = new StringBuilder();
        messages.append("该请求只支持Media[");
        List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
        if (!CollectionUtils.isEmpty(mediaTypes)) {
            messages.append(MediaType.toString(mediaTypes));
        }
        messages.append("]");
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return output(response, handler, generateErrorCode(HttpServletResponse.SC_METHOD_NOT_ALLOWED, 0), messages.toString(), null);
    }

    /**
     * 输出对象
     * <p/>
     * 把相关对象转化成JSON，并输出到Writer中，如果有callback方法，则使用callback调用出错的方法。
     *
     * @param response
     * @param handler
     * @param callback
     * @return
     * @throws java.io.IOException
     */
    public ModelAndView output(HttpServletResponse response, Object handler, int code, String message, String callback) throws IOException {
        ControllerResult result = new ErrorControllerResult();
        result.setCode(code);
        result.setMessage(message);
        return output(response, handler, result, callback);
    }

    /**
     * 输出对象
     * <p/>
     * 把相关对象转化成JSON，并输出到Writer中，如果有callback方法，则使用callback调用出错的方法。
     *
     * @param response
     * @param obj
     * @param callback
     * @return
     * @throws java.io.IOException
     */
    public ModelAndView output(HttpServletResponse response, Object handler, Object obj, String callback) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(ObjectUtils.toJson(obj));
        if (!StringUtils.isEmpty(callback)) {
            sb.insert(0, callback + "(");
            sb.append(")");
        }
        response.setContentType("application/json; charset=" + DEFAULT_CHARSET.name());
        Writer writer = response.getWriter();
        writer.write(sb.toString());
        return new ModelAndView();
    }

    /**
     * 获取从前端传递过来的callback的js回调函数名
     *
     * @param request
     */
    private String getJsonpCallbackFunc(HttpServletRequest request) {
        return request.getParameter(ControllerConstants.JSON_CALLBACK);
    }

    /**
     * 根据Handler转换成OperationType
     * <p/>
     * 如果未取得，返回null
     *
     * @param handler
     * @return
     */
    private OperationType getOperationType(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (method.isAnnotationPresent(Operation.class)) {
                Operation operation = method.getAnnotation(Operation.class);
                return operation.value();
            }
        }
        return null;
    }

    /**
     * 生成错误代码
     * <p/>
     * 为6位的整数，高3位与http status code一致，低3位为自定义错误号，默认为 000
     *
     * @param statusCode
     * @param index
     * @return
     */
    private int generateErrorCode(int statusCode, int index) {
        return statusCode * 1000 + index;
    }
}
