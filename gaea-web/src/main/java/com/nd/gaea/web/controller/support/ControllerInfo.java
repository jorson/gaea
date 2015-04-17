package com.nd.gaea.web.controller.support;

import java.lang.annotation.*;

/**
 * 标识Controller对象信息.
 *
 * @author bifeng.liu
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ControllerInfo {
    /**
     * Controller模块名称
     *
     * @return
     */
    String namespace() default "";

    /**
     * Controller模块View所在根目录
     *
     * @return
     */
    String rootPath() default "";

    /**
     * Controller的版本，如果有version，则会在url中加入v + version
     * 如：请求URL为/simple/get，如在请求中accept的version版本为3，那么会自动转到v3/simple/get的请求
     *
     * @return
     */
    String version() default "";
}
