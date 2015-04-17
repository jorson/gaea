package com.nd.gaea.core.model;

import com.nd.gaea.core.model.converter.AbstractModelConverter;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2014-11-25.
 */
@Documented
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ViewModelMapping {

    /**
     * 标记View Model映射的源类型
     * @return 映射的源类型
     */
    Class<?> sourceType();

    /**
     * 从源类型到目标类型的自定义转换类
     * @return 自定义转换类
     */
    Class<? extends AbstractModelConverter> converter() default AbstractModelConverter.class;
}
