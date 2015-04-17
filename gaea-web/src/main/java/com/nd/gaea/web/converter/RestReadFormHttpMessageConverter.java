package com.nd.gaea.web.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 关于Form的只读Message Converter
 * <p/>
 * 处理Form的读处理
 *
 * @author bifeng.liu
 * @since 2014/12/8
 */

public class RestReadFormHttpMessageConverter implements HttpMessageConverter<Object> {

    private ObjectMapper objectMapper = new RestObjectMapper();

    private Charset charset = Charset.forName("UTF-8");

    private List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();

    public RestReadFormHttpMessageConverter() {
        this.supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        this.supportedMediaTypes.add(MediaType.MULTIPART_FORM_DATA);
    }

    /**
     * Sets the character set used for writing form data.
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        if (mediaType == null) {
            return true;
        }
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            // we can't read multipart
            if (!supportedMediaType.equals(MediaType.MULTIPART_FORM_DATA) && supportedMediaType.includes(mediaType)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public Object read(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        MediaType contentType = inputMessage.getHeaders().getContentType();
        Charset charset = contentType.getCharSet() != null ? contentType.getCharSet() : this.charset;
        String body = StreamUtils.copyToString(inputMessage.getBody(), charset);
        String[] pairs = StringUtils.tokenizeToStringArray(body, "&");
        Map<String, Object> result = new HashMap<>(pairs.length);
        for (String pair : pairs) {
            int idx = pair.indexOf('=');
            if (idx == -1) {
                result.put(URLDecoder.decode(pair, charset.name()), null);
            } else {
                String name = URLDecoder.decode(pair.substring(0, idx), charset.name());
                String value = URLDecoder.decode(pair.substring(idx + 1), charset.name());
                result.put(name, value);
            }
        }
        String jsonValue = objectMapper.writeValueAsString(result);
        JavaType javaType = this.objectMapper.getTypeFactory().constructType(clazz, (Class<?>) null);
        return readJavaType(javaType, jsonValue);
    }


    private Object readJavaType(JavaType javaType, String str) {
        try {
            return this.objectMapper.readValue(str, javaType);
        } catch (IOException ex) {
            throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }


    /**
     * 只读，不支持写的处理
     */
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    /**
     * 只读，不支持写的处理
     */
    @Override
    public void write(Object map, MediaType contentType, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        throw new UnsupportedOperationException();
    }

    /**
     * 设置支持的{@link MediaType}
     */
    public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
        this.supportedMediaTypes = supportedMediaTypes;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Collections.unmodifiableList(this.supportedMediaTypes);
    }

    /**
     * 设置ObjectMapper
     *
     * @param objectMapper
     */
    public void setObjectMapper(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "ObjectMapper must not be null");
        this.objectMapper = objectMapper;
    }

    /**
     * 返回在Converter中使用的{@code ObjectMapper}
     */
    public ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }


}