package gaea.foundation.core.repository.extend;

import gaea.foundation.core.repository.extend.support.UndeletableConstant;

import java.lang.annotation.*;

/**
 * 标识留痕对象不能被删除,只能被设为无效的Annoation.
 * <p/>
 * 可以定义任意属性代表status,而默认为status属性.
 *
 * @author wuhy
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Undeletable {
    /**
     * 是否能够更新，有的对象不能在系统中更新删除状态，该状态要设置成false
     *
     * @return
     */
    boolean updatable() default true;

    /**
     * 栏位名称，默认为status
     *
     * @return
     */
    String property() default UndeletableConstant.DEFAULT_COLUMN_NAME;

    /**
     * 无效值，默认值为1
     *
     * @return
     */
    String unvalid() default UndeletableConstant.DEFAULT_UNVALID_VALUE;

    /**
     * 有效值，默认值为0
     *
     * @return
     */
    String valid() default UndeletableConstant.DEFAULT_VALID_VALUE;

    /**
     * 类型，默认类型为int
     *
     * @return
     */
    String type() default UndeletableConstant.DEFAULT_COLUMN_TYPE;
}
