package gaea.platform.web.controller.support;

import java.lang.annotation.*;

/**
 * Created by jsc on 14-6-24.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Operation {

    boolean isJson() default false;
    public OperationType value() default OperationType.PAGE;
}
