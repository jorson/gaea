package com.nd.gaea.web.converter;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 * Rest支持的ObjectMapper，处理一些特定的配置
 *
 * @author bifeng.liu
 */

public class RestObjectMapper extends ObjectMapper {

    public RestObjectMapper() {
        initConfigure();
    }

    /**
     * 重新设置与Rest的配置项
     */
    private void initConfigure() {
        //设置将驼峰命名法转换成下划线的方式输入输出
        this.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        // this.setPropertyNamingStrategy(new CamelPropertyNamingStrategy());
        //设置时间为 ISO-8601 日期
        this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //如果输入不存在的字段时不会报错
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //使用默认的Jsckson注解
        AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        this.setAnnotationIntrospector(introspector);
        /*
         * 设定是否使用Enum的toString函数来读取Enum,
         * 为False时使用Enum的name()函数来读取Enum, 默认为False.
         */
        this.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        this.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        this.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        //是否缩放排列输出，默认false，有些场合为了便于排版阅读则需要对输出做缩放排列
        //this.configure(SerializationFeature.INDENT_OUTPUT, true);
        //是否环绕根元素，默认false，如果为true，则默认以类名作为根元素
        //this.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        //序列化BigDecimal时之间输出原始数字还是科学计数，默认false，即是否以toPlainString()科学计数方式来输出
        this.configure(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN, false);
    }
}
