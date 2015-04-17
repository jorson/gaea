package com.nd.gaea.web.controller;

import com.nd.gaea.core.exception.ObjectNotFoundException;
import com.nd.gaea.utils.GenericsUtils;
import com.nd.gaea.web.controller.support.Operation;
import com.nd.gaea.web.controller.support.OperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * 操作支持的控制器
 *
 * @author bifeng.liu
 */
public abstract class OperationSupportController<T> extends BaseController implements ControllerOperation<T> {

    /**
     * JSON Content Type的名称
     */
    protected final static String APPLICATION_JSON = "application/json";

    @Autowired(required = false)
    protected HttpServletRequest request;

    @Autowired(required = false)
    protected HttpServletResponse response;

    /**
     * DAO所管理的Entity类型
     */
    protected Class<T> entityClass;

    /**
     * 在构造函数中将泛型T.class赋给entityClass.
     */
    public OperationSupportController() {
        entityClass = GenericsUtils.getSuperClassGenricType(getClass());
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @Operation(OperationType.OPENAPI)
    public Object list() {
        return getEntityList();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @Operation(OperationType.OPENAPI)
    public Object get(@PathVariable("id") Serializable id) {
        Object ret = doGet(id);
        if (ret == null) {
            throw new ObjectNotFoundException("对象未找到");
        }
        return ret;
    }

    protected abstract Object doGet(Serializable id);

    /**
     * 添加数据
     * <p/>
     * 执行<code>doCreate</code>方法
     *
     * @param entity 记录
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    @Operation(OperationType.OPENAPI)
    public Object create(@RequestBody T entity) {
        return doCreate(entity);
    }

    protected abstract T doCreate(T model);

    /**
     * 更新数据
     * <p/>
     * 执行<code>doUpdate</code>方法
     *
     * @param entity 记录
     * @return
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    @ResponseBody
    @Operation(OperationType.OPENAPI)
    public Object update(@PathVariable("id") Serializable id, @RequestBody T entity) {
        return doUpdate(id, entity);
    }

    protected abstract T doUpdate(Serializable id, T model);


    /**
     * 删除数据
     * <p/>
     * 执行<code>doDelete</code>方法
     *
     * @param id 记录
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Operation(OperationType.OPENAPI)
    public Object delete(@PathVariable("id") Serializable id) {
        Object model = doGet(id);
        if (model == null) {
            throw new ObjectNotFoundException("对象未找到");
        }
        Object ret = doDelete(id);
        return ret == null ? model : ret;
    }

    protected abstract Object doDelete(Serializable id);

    /**
     * 查询条数
     *
     * @return
     */
    @RequestMapping(value = "/$count", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public int count() {
        return doCount();
    }

    protected abstract int doCount();

    //-------------------------------------------------------------------------
    // 内部方法
    //-------------------------------------------------------------------------

    /**
     * 取得entityClass.JDK1.4不支持泛型的子类可以抛开Class<T> entityClass,重载此函数达到相同效果。
     */
    protected Class<T> getEntityClass() {
        return entityClass;
    }

    //-------------------------------------------------------------------------
    // 抽象方法
    //-------------------------------------------------------------------------

    /**
     * 取得Entity列表
     *
     * @return
     */
    public abstract List<T> getEntityList();


}
