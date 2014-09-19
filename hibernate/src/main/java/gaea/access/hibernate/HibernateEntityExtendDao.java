package gaea.access.hibernate;


import gaea.foundation.core.bo.EntityObject;
import gaea.foundation.core.context.WorkContext;
import gaea.foundation.core.repository.query.QuerySupport;
import gaea.foundation.core.exception.BaseRuntimeException;
import gaea.foundation.core.repository.extend.UndeletableEntityOperation;
import gaea.foundation.core.repository.extend.support.EntityInfo;
import gaea.foundation.core.repository.shard.Shard;
import gaea.foundation.core.repository.shard.ShardDataRegister;
import gaea.foundation.core.repository.shard.ShardInfo;
import gaea.foundation.core.repository.shard.ShardParameter;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 加强版的entity dao.
 * <p>自动处理Undeletable EntityField.<br>
 * Undeletable EntityField 在删除时只把状态设为无效,不会真正执行删除.<br>
 * Undeletable EntityField 可以通过annotation或接口两种形式来声明.<br>
 * 其中annotation模式不限制状态列的属性名必须为"status",可以用注释来确定任意属性为状态属性.<br>
 * </p>
 *
 * @author wuhy
 * @see gaea.access.hibernate.HibernateEntityDao
 * @see gaea.foundation.core.repository.extend.support.EntityInfo
 * @see gaea.foundation.core.repository.extend.UndeletableEntityOperation
 * @see gaea.foundation.core.repository.extend.Undeletable
 */
@SuppressWarnings("unchecked")
public class HibernateEntityExtendDao<T extends EntityObject> extends HibernateEntityDao<T> implements UndeletableEntityOperation<T> {
    /**
     * 保存所管理的Entity的信息.
     */
    protected EntityInfo entityInfo;

    /**
     * 构造函数，初始化entity信息.
     */
    public HibernateEntityExtendDao() {
        entityInfo = new EntityInfo(entityClass);
    }

    /**
     * 取得所有状态为有效的对象.
     */
    public List<T> findAllValid() {
        Criteria criteria = this.getHibernateTemplate().createCriteria(getEntityClass());
        if (entityInfo.isUndeletable()) {
            criteria.add(getUnDeletableCriterion());
        }
        return criteria.list();
    }

    /**
     * 根据查询条件取得表或者集合中满足条件的所有标志有效的数据
     *
     * @param queryData 栏位与值列表
     * @return
     */
    public List<T> findValid(QuerySupport querySupport) {
        Criteria criteria = this.getHibernateTemplate().createCriteria(getEntityClass());
        parser.parse(criteria, querySupport);
        if (entityInfo.isUndeletable()) {
            criteria.add(getUnDeletableCriterion());
        }
        return criteria.list();
    }

    /**
     * 根据查询对象取得表或者集合中满足条件的所有标志有效的数据
     *
     * @param example
     * @return
     */
    @Override
    public List<T> findValid(T queryObject) {
        if (entityInfo.isUndeletable()) {
            try {
                PropertyUtils.setProperty(queryObject, entityInfo.getStatusProperty(), entityInfo.getUnvalidValue());
            } catch (Exception e) {
                ReflectionUtils.handleReflectionException(e);
            }
        }
        return find(queryObject);
    }

    public List<T> find(ShardParameter shardParameter, QuerySupport querySupport) {
        return null;
    }

    /**
     * 获取过滤已删除对象的hql条件语句.
     *
     * @see com.huayu.foundation.core.repository.extend.UndeletableEntityOperation#getUnDeletableHQL()
     */
    public String getUnDeletableHQL() {
        return entityInfo.getStatusProperty() + "<>" + entityInfo.getUnvalidValue();
    }

    /**
     * 获取过滤已删除对象的Criterion条件语句.
     *
     * @see com.huayu.foundation.core.repository.extend.UndeletableEntityOperation#
     */
    public Criterion getUnDeletableCriterion() {
        return Restrictions.not(Restrictions.eq(entityInfo.getStatusProperty(), entityInfo.getUnvalidValue()));
    }

    /**
     * 重载保存函数,在保存前先调用onValid(T),进行数据库相关的校验.
     *
     * @see com.huayu.foundation.core.repository.hibernate.HibernateEntityDao#save(Object)
     */
    public void save(T entity) {
        Assert.isInstanceOf(getEntityClass(), entity);
        onValid(entity);
        super.save(entity);
    }

    /**
     * 重载修改函数,在保存前先调用onValid(T),进行数据库相关的校验.
     *
     * @see com.huayu.foundation.core.dao.hibernate.HibernateEntityDao#save(Object)
     */
    @Override
    public void update(T entity) {
        save(entity);
    }

    @Override
    public void delete(Object removeObject) {
        delete((T) removeObject);
    }

    /**
     * 删除对象，如果是Undeleteable的entity,设置对象的状态而不是直接删除.
     *
     * @see com.huayu.foundation.core.dao.hibernate.HibernateEntityDao#remove(Object)
     */
    public void delete(T entity) {
        if (entityInfo.isUndeletable()) {
            if (entityInfo.isUpdatable()) {
                try {
                    PropertyUtils.setProperty(entity, entityInfo.getStatusProperty(), entityInfo.getUnvalidValue());
                    save(entity);
                } catch (Exception e) {
                    ReflectionUtils.handleReflectionException(e);
                }
            } else {
                throw new BaseRuntimeException("the data is not allowed to be removed");
            }
        } else {
            super.delete(entity);
        }
    }

    /**
     * 与数据库相关的校验,比如判断名字在数据库里有没有重复, 在保存时被调用,在子类重载.
     *
     * @see #save(Object)
     */
    protected void onValid(T entity) {
    }

//    public Pager pagedQueryValid(T example, int pageNo, int pageSize, long totalCount) {
//        Assert.notNull(example);
//        Criteria criteria = this.getHibernateTemplate().createCriteria(entityClass, Example.create(example));
//        criteria.add(getUnDeletableCriterion());
//        return pagedQuery(criteria, pageNo, pageSize, totalCount);
//    }
//
//    /**
//     * 分页查询函数，根据entityClass和查询条件参数创建默认的<code>Criteria</code>.
//     *
//     * @param pageNo 页号,从1开始.
//     * @return 含总记录数和当前页数据的Page对象.
//     */
//    public Pager pagedQueryValid(int pageNo, int pageSize, long totalCount, Criterion... criterions) {
//        Criteria criteria = createCriteria(criterions);
//        criteria.add(getUnDeletableCriterion());
//        return pagedQuery(criteria, pageNo, pageSize, totalCount);
//    }
//
//    /**
//     * 分页查询函数，根据entityClass和查询条件参数,排序参数创建默认的<code>Criteria</code>.
//     *
//     * @param pageNo 页号,从1开始.
//     * @return 含总记录数和当前页数据的Page对象.
//     */
//    public Pager pagedQueryValid(int pageNo, int pageSize, long totalCount, String orderBy, Criterion... criterions) {
//        Assert.hasText(orderBy);
//        Criteria criteria = createCriteria(orderBy, criterions);
//        criteria.add(getUnDeletableCriterion());
//        return pagedQuery(criteria, pageNo, pageSize, totalCount);
//    }
}
