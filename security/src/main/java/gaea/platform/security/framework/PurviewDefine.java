package gaea.platform.security.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于权限定义的注解
 * 如果系统包含的权限在DB中已定义，那么无需使用改注解；
 * 如果系统包含的权限在DB中未定义，那么常量上需要声明注解，供系统进行扫描，扫描结果保存在PurviewManger中
 * @see com.huayu.foundation.security.framework.PurviewManger
 * Created by wuhy on 14-6-6.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PurviewDefine {

    /**
     * 名称，供前台展示
     *
     * @return
     */
    public String value();

    /**
     * 父级,可以不定义
     * @return
     */
    public String parent() default "";

}
