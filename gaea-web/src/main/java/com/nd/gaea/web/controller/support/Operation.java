package com.nd.gaea.web.controller.support;

import java.lang.annotation.*;

/**
 * Controller方法操作
 * <p/>
 * 通用的方法注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Operation {

    /**
     * 操作类型
     *
     * @return
     */
    public OperationType value() default OperationType.PAGE;
}
