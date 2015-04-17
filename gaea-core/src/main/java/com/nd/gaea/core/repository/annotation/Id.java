package com.nd.gaea.core.repository.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Id注解
 * <p/>
 * 用于标识实体的主键
 *
 * @author bifeng.liu
 * @since 2014/11/28
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Id {
}
