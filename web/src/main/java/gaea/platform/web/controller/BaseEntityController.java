package gaea.platform.web.controller;

import gaea.foundation.core.bo.EntityObject;
import gaea.foundation.core.repository.query.QuerySupport;
import gaea.foundation.core.repository.query.criterion.Criterion;
import gaea.foundation.core.repository.support.Pager;
import gaea.foundation.core.repository.support.SqlUtils;
import gaea.foundation.core.service.EntityService;
import gaea.foundation.core.utils.BeanUtils;
import gaea.foundation.core.utils.ClassUtils;
import gaea.foundation.core.utils.GenericsUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 请在这里输入说明
 *
 * @author bifeng.liu
 */
public abstract class BaseEntityController<T extends EntityObject> extends OperationSupportController<T> {
    /**
     * LOGGER对象
     */
    private static Log logger = LogFactory.getLog(BaseController.class);

    /**
     * Controller所管理的Entity类型.
     */
    protected Class<T> entityClass;

    public BaseEntityController() {
        entityClass = GenericsUtils.getSuperClassGenricType(getClass());
    }

    @InitBinder("model")
    public void binderData(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("model.");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    protected Object doAdd(T model) {
        getEntityService().add(model);
        return new SuccessControllerResult();
    }

    protected Object doUpdate(T model) {
        getEntityService().update(model);
        return new SuccessControllerResult();
    }

    @Override
    public EntityObject getModel(Serializable id) {
        return this.getEntityService().get(transformIdentity(id));
    }

    @Override
    protected Object doDelete(Serializable id) {
        getEntityService().removeById(transformIdentity(id));
        return new SuccessControllerResult();
    }

    @Override
    public Pager getEntityList() {
        Pager pager = getPager(this.request);
        QuerySupport querySupport = QuerySupport.createQuery();
        querySupport.setPager(pager);
        List<Criterion> criterions = getSearchList();
        if (criterions != null) {
            querySupport.addRange(criterions);
        }
        querySupport.addOrderRange(SqlUtils.parseOrders(pager.getOrderBy()));
        return getEntityService().pagedQuery(querySupport);
    }


    /**
     * 根据类型转换ID的格式
     * <p/>
     * 根据类型中getId返回的类型来决定类型，如果为默认Object，则返回原生的id,
     * 该类型要存在<code>Constructor(String)</code>这样的构造函数，并且实现Serializable
     *
     * @param id
     * @return
     */
    protected Serializable transformIdentity(Serializable id) {
        try {
            Method method = BeanUtils.getDeclaredMethod(this.entityClass, "getId");
            Type type = method.getGenericReturnType();
            return (Serializable) ClassUtils.createNewInstance((Class) type, new Class[]{String.class}, new Object[]{id});
        } catch (NoSuchMethodException e) {
            logger.error("转换ID出错", e);
            return id;
        }
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
    protected abstract EntityService getEntityService();

}
