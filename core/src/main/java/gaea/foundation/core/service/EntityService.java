package gaea.foundation.core.service;

import gaea.foundation.core.bo.EntityObject;
import gaea.foundation.core.repository.query.QuerySupport;

import java.io.Serializable;
import java.util.List;

@DomainService
public interface EntityService<T extends EntityObject> extends EntityPagedQueryable<T> {
    /**
     * 根据ID从表或者集合中取得单条数据
     *
     * @param id
     * @return
     */
    T get(Serializable id);

    /**
     * 根据查询条件取得表或者集合中的单条数据
     *
     * @param querySupport 栏位与值列表
     * @return
     */
    T get(QuerySupport querySupport);

    /**
     * 查询表或者集合中所有数据
     *
     * @return
     */
    List<T> findAll();

    /**
     * 根据查询对象取得表或者集合中满足条件的所有数据
     *
     * @param queryObject
     * @return
     */
    List<T> find(T queryObject);

    /**
     * 根据查询对象取得表或者集合中满足条件的所有数据
     *
     * @param querySupport 查询对象
     * @return
     */
    List<T> find(QuerySupport querySupport);

    /**
     * 根据ID列表取得记录列表
     *
     * @param ids
     * @return
     */
    List<T> find(Serializable[] ids);

    /**
     * 添加单条记录数据
     *
     * @param entity 记录数据
     */
    void add(T entity);

    /**
     * 保存数据
     *
     * @param entity 实体
     * @return
     */
    public void save(T entity);

    /**
     * 修改单条数据
     *
     * @param entity 要删除的对象
     */
    void update(T entity);

    /**
     * 删除对象
     *
     * @param entity
     */
    void remove(T entity);

    /**
     * 根据ID删除对象
     *
     * @param id
     */
    void removeById(Serializable id);
    /**
     * 根据ID殂删除所对应的对象
     *
     * @param ids
     */
    void remove(Serializable[] ids);
    /**
     * 刷新对象
     *
     * @param entity
     */
    public void refresh(T entity);
}
