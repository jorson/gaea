package com.nd.gaea.web.model;

import com.nd.gaea.core.model.ViewModel;
import com.nd.gaea.web.controller.support.OperationType;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2014-11-26.
 */
@Documented
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CustomOutputModel {

    /**
     * 标记View Model映射的源类型
     * @return 映射的源类型
     */
    Class<? extends ViewModel> outputType() default ViewModel.class;

    /**
     * 标记输出对象转换的类型
     * @return
     */
    OperationType operationType() default OperationType.AJAXAPI;
}
