package gaea.platform.web.controller.support;

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
}
