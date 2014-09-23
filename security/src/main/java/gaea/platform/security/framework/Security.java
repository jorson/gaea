package gaea.platform.security.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限注解
 * 使用方式
 * 1、若方法有注解，以方法注解为准进行权限校验
 * 2、方法没有注解，以类注解为准进行权限校验
 * 3、若方法与类均没有注解，不进行权限校验
 *
 * @author wuhy
 *         Date: 14-5-30 上午11:44
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Security {

    /**
     * 权限
     *
     * @return
     */
    public String value() default "";
}
