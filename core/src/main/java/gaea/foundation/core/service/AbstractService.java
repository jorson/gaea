package gaea.foundation.core.service;

import gaea.foundation.core.bo.EntityObject;
import gaea.foundation.core.exception.BusinessExceptionFactory;
import gaea.foundation.core.repository.EntityRepository;
import gaea.foundation.core.repository.query.QuerySupport;
import gaea.foundation.core.repository.query.criterion.Criterion;
import gaea.foundation.core.repository.support.Pager;
import gaea.foundation.core.repository.support.SqlUtils;
import gaea.foundation.core.utils.BeanUtils;

import java.io.Serializable;
import java.util.List;

import static gaea.foundation.core.repository.query.criterion.Restrictions.allEq;

/**
 * 抽象服务类
 *
 * @author wuhy
 */
@DomainService
public abstract class AbstractService<T extends EntityObject> {
    /**
     * 实体Dao
     *
     * @return
     */
    public abstract EntityRepository<T> getEntityDao();

    /**
     * 根据ID取得单条数据
     *
     * @param id
     * @return
     */
    public T get(Serializable id) {
        return getEntityDao().get(id);
    }

    /**
     * 根据查询条件取得表或者集合中的单条数据
     *
     * @param querySupport 查询对象
     * @return
     */
    public T get(QuerySupport querySupport) {
        return this.getEntityDao().get(querySupport);
    }

    /**
     * 查询表或者集合中所有数据
     *
     * @return
     */
    public List<T> findAll() {
        return getEntityDao().findAll();
    }

    /**
     * 根据查询对象取得表或者集合中满足条件的所有数据
     *
     * @param queryObject
     * @return
     */
    public List<T> find(T queryObject) {
        return getEntityDao().find(queryObject);
    }

    /**
     * 根据查询对象取得表或者集合中满足条件的所有数据
     *
     * @param querySupport 查询对象
     * @return
     */
    public List<T> find(QuerySupport querySupport) {
        return this.getEntityDao().find(querySupport);
    }

    /**
     * 根据ID列表取得记录列表
     *
     * @param ids
     * @return
     */
    public List<T> find(Serializable[] ids) {
        return this.getEntityDao().find(ids);
    }

    /**
     * 根据查询对象分页数据，会返回totalCount
     *
     * @param querySupport 查询对象
     * @return 含总记录数和当前页数据的Page对象.
     */
    public Pager pagedQuery(QuerySupport querySupport) {
        return this.getEntityDao().pagedQuery(querySupport);
    }

    /**
     * 根据开始页数、每页记录数、排序、条件取得分页数据，会返回totalCount
     * <p/>
     * 当totalCount传入小于0的数时，才会重新计算totalCount
     *
     * @param pageNo     开始页数，从1开始
     * @param pageSize   每页记录数
     * @param criterions 条件
     * @return 含总记录数和当前页数据的Page对象.
     */
    public Pager pagedQuery(int pageNo, int pageSize, Criterion... criterions) {
        return this.pagedQuery(pageNo, pageSize, -1, null, criterions);
    }

    /**
     * 根据开始页数、每页记录数、条件取得分页数据，会返回totalCount
     * <p/>
     * 当totalCount传入小于0的数时，才会重新计算totalCount
     *
     * @param pageNo     开始页数，从1开始
     * @param pageSize   每页记录数
     * @param totalCount 总页面数
     * @param orderBy    排序
     * @param criterions 条件
     * @return 含总记录数和当前页数据的Page对象.
     */
    public Pager pagedQuery(int pageNo, int pageSize, long totalCount, T queryObject) {
        return pagedQuery(queryObject, new Pager(pageNo, pageSize, totalCount, null));
    }

    /**
     * 根据开始页数、每页记录数、排序、条件取得分页数据，会返回totalCount
     * <p/>
     * 当totalCount传入小于0的数时，才会重新计算totalCount
     *
     * @param pageNo     开始页数，从1开始
     * @param pageSize   每页记录数
     * @param totalCount 总页面数
     * @param orderBy    排序
     * @param criterions 条件
     * @return 含总记录数和当前页数据的Page对象.
     */
    public Pager pagedQuery(int pageNo, int pageSize, long totalCount, String orderBy, Criterion... criterions) {
        QuerySupport querySupport = QuerySupport.createQuery();
        Pager pager = new Pager(pageNo, pageSize, totalCount, null);
        querySupport.setPager(pager);
        querySupport.addRange(criterions);
        querySupport.addOrderRange(SqlUtils.parseOrders(orderBy));
        return this.getEntityDao().pagedQuery(querySupport);
    }

    /**
     * 根据查询对象分页数据，会返回totalCount
     *
     * @param querySupport 查询对象
     * @param queryObject  查询对象
     * @return 含总记录数和当前页数据的Page对象.
     */
    public Pager pagedQuery(T queryObject, Pager pager) {
        try {
            QuerySupport querySupport = QuerySupport.createQuery();
            querySupport.setPager(pager);
            querySupport.addRange(allEq(BeanUtils.toMap(queryObject)));
            querySupport.addOrderRange(SqlUtils.parseOrders(pager.getOrderBy()));
            return this.getEntityDao().pagedQuery(querySupport);
        } catch (Exception ex) {
            throw BusinessExceptionFactory.wrapBusinessException(ex);
        }
    }

    /**
     * 添加单条记录数据
     *
     * @param entity 记录数据
     */
    public void add(T entity) {
        this.getEntityDao().insert(entity);
    }

    /**
     * 保存数据
     *
     * @param entity 实体
     * @return
     */
    public void save(T entity) {
        this.getEntityDao().save(entity);
    }

    /**
     * 修改单条数据
     *
     * @param entity 要删除的对象
     */
    public void update(T entity) {
        this.getEntityDao().update(entity);
    }

    /**
     * 删除对象
     *
     * @param entity
     */
    public void remove(T entity) {
        this.getEntityDao().delete(entity);
    }

    /**
     * 根据ID删除对象
     *
     * @param id
     */
    public void removeById(Serializable id) {
        this.getEntityDao().deleteById(id);
    }

    /**
     * 根据ID殂删除所对应的对象
     *
     * @param ids
     */
    public void remove(Serializable[] ids) {
        this.getEntityDao().deleteByIds(ids);
    }

    /**
     * 刷新对象
     *
     * @param entity
     */
    public void refresh(T entity) {
        this.getEntityDao().refresh(entity);
    }

}
