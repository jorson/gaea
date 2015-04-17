package com.nd.gaea.web.controller;

import com.nd.gaea.core.exception.ObjectNotFoundException;
import com.nd.gaea.odata.ODataHandler;
import com.nd.gaea.core.repository.query.criterion.Criterion;
import com.nd.gaea.core.service.AbstractService;
import com.nd.gaea.utils.BeanUtils;
import com.nd.gaea.web.exception.ControllerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 基础类型的控制器
 *
 * @author bifeng.liu
 */
public abstract class BaseEntityController<T> extends OperationSupportController<T> {
    /**
     * LOGGER对象
     */
    private final static Log LOGGER = LogFactory.getLog(BaseController.class);


    @InitBinder("model")
    public void binderData(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("model.");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    protected Object doGet(Serializable id) {
        return getEntityService().get(changeIdentifyType(id));
    }

    protected T doCreate(T model) {
        getEntityService().create(model);
        return model;
    }

    /**
     * 根据ID更新对象中的值
     *
     * @param id
     * @param model
     * @return
     */
    protected T doUpdate(Serializable id, T model) {
        //根据ID取得记录
        T result = getEntityService().get(changeIdentifyType(id));
        //如果记录为null，则直接抛出异常
        if (result == null) {
            throw new ObjectNotFoundException("对象未找到");
        }
        try {
            Map<String, Object> retMap = BeanUtils.toMap(model);
            BeanUtils.populate(result, retMap);
        } catch (Exception e) {
            throw new ControllerException("转换对象出错", e);
        }
        getEntityService().update(result);
        return result;
    }


    @Override
    protected Object doDelete(Serializable id) {
        getEntityService().delete(changeIdentifyType(id));
        return null;
    }


    @Override
    protected int doCount() {
        Object result = ODataHandler.process(request, getEntityService(), entityClass);
        if(result instanceof Integer) {
            return (Integer)result;
        }
        throw new ControllerException("error return value type, please check odata expression");
    }

    @Override
    public List<T> getEntityList() {
        Object result = ODataHandler.process(request, getEntityService(), entityClass);
        if(result instanceof List) {
            return (List<T>)result;
        }
        throw new ControllerException("error return value type, please check odata expression");
    }


    protected Serializable changeIdentifyType(Serializable id) {
        if (id != null) {
            if (id instanceof String) {
                return new Long((String) id);
            }
        }
        return id;
    }

    //-------------------------------------------------------------------------
    // 抽象方法或者子类按需要继承实现的
    //-------------------------------------------------------------------------

    /**
     * 取得查询条件的列表
     *
     * @return
     */
    protected List<Criterion> getSearchList() {
        return null;
    }

    /**
     * 取得实体的Service
     *
     * @return
     */
    protected abstract AbstractService<T> getEntityService();

}
