package gaea.platform.web.openapi;

import java.lang.annotation.*;

/**
 * Created by way on 14-5-22.
 *
 *
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenApi {

    /**
     * 返回的类型是openApi,还是ajaxApi等
     */
    public enum ApiType {openapi, ajaxapi, jsonp,openAndJsonp};


    public ApiType value() default ApiType.openapi;
}
