package com.nd.gaea.web.model;

import com.nd.gaea.core.model.ViewModel;
import com.nd.gaea.web.controller.OpenApiControllerResult;
import com.nd.gaea.web.controller.support.OperationType;
import com.nd.gaea.web.model.mapper.AutoModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 自定义返回值的Handler, 支持DTO to VO的转换
 * @author Jorson.WHY
 */
public class ViewModelReturnValueHandler implements HandlerMethodReturnValueHandler, InitializingBean {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ViewModelReturnValueHandler.class);

    protected List<HttpMessageConverter<?>> messageConverters;
    protected List<HttpMessageConverter<?>> customMessageConverters;

    private static final MediaType MEDIA_TYPE_APPLICATION = new MediaType("application");
    private final ContentNegotiationManager contentNegotiationManager;
    protected List<MediaType> allSupportedMediaTypes;

    /**
     * OPEN API JSONP支持的回调参数jsonpcallback（jquery默认）
     */
    public static final String JSON_CALLBACK = "jsonpcallback";

    public ViewModelReturnValueHandler() {
        contentNegotiationManager = new ContentNegotiationManager();
    }

    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        return methodParameter.getMethodAnnotation(CustomOutputModel.class) != null;
    }

    @Override
    public void handleReturnValue(Object result, MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest) throws Exception {
        modelAndViewContainer.setRequestHandled(true);
        Object realValue = null;
        CustomOutputModel outputModel = methodParameter.getMethodAnnotation(CustomOutputModel.class);
        //如果包含 CustomOutputModel 特性
        if(outputModel != null) {
            Class<? extends ViewModel> targetClz = outputModel.outputType();
            //如果输出对象是一个接口, 说明就不要转换
            if(!targetClz.isInterface()) {
                try {
                    realValue = AutoModelMapper.getInstance().process(result, targetClz);
                    //如果返回的RealValue为空
                    if(realValue != null) {
                        //对返回结果进行包装
                        realValue = wrapResult(realValue, outputModel.operationType());
                        writeWithMessageConverters(realValue, methodParameter, nativeWebRequest);
                    } else {
                        //如果返回对象为空, 则将原始的对象写出去
                        writeWithMessageConverters(result, methodParameter, nativeWebRequest);
                    }
                } catch (Exception e) {
                    if(LOGGER.isErrorEnabled()) {
                        LOGGER.error("mapping to " + targetClz.getName() + " error!", e);
                    }
                }
            } else {
                writeWithMessageConverters(result, methodParameter, nativeWebRequest);
            }
        } else {
            throw new IllegalArgumentException("action not contain ViewModel annotation!");
        }
    }

    /**
     * 对映射后的结果进行包装
     * @param result 结果对象
     * @param type 包装类型
     * @return 包装后的对象
     */
    protected Object wrapResult(Object result, OperationType type) {
        //如果是Ajax_API的注解,直接返回
        if(type == OperationType.AJAXAPI) {
            return result;
        } else if(type == OperationType.OPENAPI) {
            OpenApiControllerResult openResult = new OpenApiControllerResult();
            openResult.setData(result);
            return openResult;
        } else {
            //无法处理除此之外的其他操作类型,这里直接返回原始对象; 由后续过程进行处理
            return result;
        }
    }

    private List<HttpMessageConverter<?>> getDefaultMessageConverter() {
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
        converters.add(new AllEncompassingFormHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter());

        if(this.getCustomMessageConverters() != null) {
            converters.addAll(getCustomMessageConverters());
        }

        return converters;
    }

    protected <T> void writeWithMessageConverters(T returnValue,
                                                  MethodParameter returnType,
                                                  NativeWebRequest webRequest)
            throws IOException, HttpMediaTypeNotAcceptableException {
        ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
        ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
        writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
    }

    @SuppressWarnings("unchecked")
    protected <T> void writeWithMessageConverters(T returnValue,
                                                  MethodParameter returnType,
                                                  ServletServerHttpRequest inputMessage,
                                                  ServletServerHttpResponse outputMessage)
            throws IOException, HttpMediaTypeNotAcceptableException {

        Class<?> returnValueClass = returnValue.getClass();

        HttpServletRequest servletRequest = inputMessage.getServletRequest();
        List<MediaType> requestedMediaTypes = getAcceptableMediaTypes(servletRequest);
        List<MediaType> producibleMediaTypes = getProducibleMediaTypes(servletRequest, returnValueClass);

        Set<MediaType> compatibleMediaTypes = new LinkedHashSet<MediaType>();
        for (MediaType r : requestedMediaTypes) {
            for (MediaType p : producibleMediaTypes) {
                if (r.isCompatibleWith(p)) {
                    compatibleMediaTypes.add(getMostSpecificMediaType(r, p));
                }
            }
        }
        if (compatibleMediaTypes.isEmpty()) {
            throw new HttpMediaTypeNotAcceptableException(producibleMediaTypes);
        }

        List<MediaType> mediaTypes = new ArrayList<MediaType>(compatibleMediaTypes);
        MediaType.sortBySpecificityAndQuality(mediaTypes);

        MediaType selectedMediaType = null;
        for (MediaType mediaType : mediaTypes) {
            if (mediaType.isConcrete()) {
                selectedMediaType = mediaType;
                break;
            }
            else if (mediaType.equals(MediaType.ALL) || mediaType.equals(MEDIA_TYPE_APPLICATION)) {
                selectedMediaType = MediaType.APPLICATION_OCTET_STREAM;
                break;
            }
        }

        if (selectedMediaType != null) {
            selectedMediaType = selectedMediaType.removeQualityValue();
            for (HttpMessageConverter<?> messageConverter : messageConverters) {
                if (messageConverter.canWrite(returnValueClass, selectedMediaType)) {
                    ((HttpMessageConverter<T>) messageConverter).write(returnValue, selectedMediaType, outputMessage);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Written [" + returnValue + "] as \"" + selectedMediaType + "\" using [" +
                                messageConverter + "]");
                    }
                    return;
                }
            }
        }
        throw new HttpMediaTypeNotAcceptableException(allSupportedMediaTypes);
    }

    @SuppressWarnings("unchecked")
    protected List<MediaType> getProducibleMediaTypes(HttpServletRequest request, Class<?> returnValueClass) {
        Set<MediaType> mediaTypes = (Set<MediaType>) request.getAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
        if (!CollectionUtils.isEmpty(mediaTypes)) {
            return new ArrayList<MediaType>(mediaTypes);
        }
        else if (!allSupportedMediaTypes.isEmpty()) {
            List<MediaType> result = new ArrayList<MediaType>();
            for (HttpMessageConverter<?> converter : messageConverters) {
                if (converter.canWrite(returnValueClass, null)) {
                    result.addAll(converter.getSupportedMediaTypes());
                }
            }
            return result;
        }
        else {
            return Collections.singletonList(MediaType.ALL);
        }
    }

    /**
     * 实现 InitializingBean 接口, 在属性完成设置后进行的操作
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if(this.messageConverters == null) {
            List<HttpMessageConverter<?>> converters = getDefaultMessageConverter();
            this.messageConverters = converters;
            allSupportedMediaTypes = getAllSupportedMediaTypes(this.messageConverters);
        }
    }

    protected ServletServerHttpRequest createInputMessage(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        return new ServletServerHttpRequest(servletRequest);
    }

    protected ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest) {
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        return new ServletServerHttpResponse(response);
    }

    private List<MediaType> getAcceptableMediaTypes(HttpServletRequest request) throws HttpMediaTypeNotAcceptableException {
        List<MediaType> mediaTypes = this.contentNegotiationManager.resolveMediaTypes(new ServletWebRequest(request));
        return mediaTypes.isEmpty() ? Collections.singletonList(MediaType.ALL) : mediaTypes;
    }

    private MediaType getMostSpecificMediaType(MediaType acceptType, MediaType produceType) {
        produceType = produceType.copyQualityValue(acceptType);
        return MediaType.SPECIFICITY_COMPARATOR.compare(acceptType, produceType) <= 0 ? acceptType : produceType;
    }

    private static List<MediaType> getAllSupportedMediaTypes(List<HttpMessageConverter<?>> messageConverters) {
        Set<MediaType> allSupportedMediaTypes = new LinkedHashSet<MediaType>();
        for (HttpMessageConverter<?> messageConverter : messageConverters) {
            allSupportedMediaTypes.addAll(messageConverter.getSupportedMediaTypes());
        }
        List<MediaType> result = new ArrayList<MediaType>(allSupportedMediaTypes);
        MediaType.sortBySpecificity(result);
        return Collections.unmodifiableList(result);
    }

    /**
     * 获取自定义的MessageConverter
     * @return
     */
    public List<HttpMessageConverter<?>> getCustomMessageConverters() {
        return customMessageConverters;
    }

    /**
     * 设置自定义的MessageConverter
     * @param customMessageConverters
     */
    public void setCustomMessageConverters(List<HttpMessageConverter<?>> customMessageConverters) {
        this.customMessageConverters = customMessageConverters;
    }
}
