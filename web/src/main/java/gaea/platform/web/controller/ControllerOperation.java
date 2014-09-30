package gaea.platform.web.controller;

import gaea.foundation.core.bo.EntityObject;

/**
 * Controller标准操作的接口，目前只支持对EntityObject
 * <p/>
 * 定义Controller标准的操作接口和常量，操作包括添加、删除、修改、列表
 *
 * @author bifeng.liu
 */
public interface ControllerOperation<T extends Object> {
    /**
     * 列表返回给Action的KEY
     */
    public static final String LIST = "list";
    /**
     * 编辑返回给Action的KEY
     */
    public static final String EDIT = "edit";
    /**
     * 查看返回给Action的KEY
     */
    public static final String VIEW = "show";


    /**
     * 添加记录
     *
     * @param model 记录
     * @param <T>
     * @return
     */
    public Object add(T model);

//    /**
//     * 更新记录
//     *
//     * @return
//     */
//    public <T> Object update(T model);
//
//
//    /**
//     * 删除单条记录
//     *
//     * @param id ID
//     * @return
//     */
//    public String delete(Serializable id);
//
//
//
//    /**
//     * 查询列表
//     *
//     * @return
//     */
//    public Object list();
//
//    /**
//     * 查看记录
//     *
//     * @return
//     */
//    public String show(Serializable id);
}
