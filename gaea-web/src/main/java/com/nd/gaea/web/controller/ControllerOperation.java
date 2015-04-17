package com.nd.gaea.web.controller;


import java.io.Serializable;

/**
 * Controller标准操作的接口，目前只支持对EntityObject
 * <p/>
 * 定义Controller标准的操作接口和常量，操作包括添加、删除、修改、列表
 *
 * @author bifeng.liu
 */
public interface ControllerOperation<T> {
    /**
     * 列表返回给Action的KEY
     */
    public static final String LIST = "list";
    /**
     * 编辑返回给Action的KEY
     */
    public static final String EDIT = "edit";

    /**
     * 取得单个记录
     *
     * @return
     */
    public Object get(Serializable id);

    /**
     * 添加记录
     *
     * @param entity 记录
     * @return
     */
    public Object create(T entity);

    /**
     * 更新记录
     *
     * @return
     */
    public Object update(Serializable id, T entity);

    /**
     * 删除单条记录
     *
     * @param id ID
     * @return
     */
    public Object delete(Serializable id);

    /**
     * 查询列表
     *
     * @return
     */
    public Object list();

    /**
     * 查询条数
     *
     * @return
     */
    public int count();

}
