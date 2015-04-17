package com.nd.gaea.core.service;

import com.nd.gaea.core.repository.Repository;
import com.nd.gaea.core.repository.query.QuerySupport;
import com.nd.gaea.core.repository.support.Pager;

import java.io.Serializable;
import java.util.List;

/**
 * 抽象服务类
 *
 * @author bifeng.liu
 */
@DomainService
public abstract class AbstractService<T> {
    /**
     * 实体Dao
     *
     * @return
     */
    public abstract Repository<T> getRepository();

    /**
     * 根据ID从表或者集合中取得单条数据
     *
     * @param id
     * @return
     */
    public T get(Serializable id) {
        return this.getRepository().get(id);
    }


    /**
     * 根据ID列表取得记录列表
     *
     * @param ids
     * @return
     */
    public List<T> getList(Serializable[] ids) {
        return this.getRepository().getList(ids);
    }

    /**
     * 添加数据
     *
     * @param entity 记录数据
     */
    public void create(T entity) {
        this.getRepository().create(entity);
    }

    /**
     * 修改对象
     *
     * @param entity 要删除的对象
     */
    public void update(T entity) {
        this.getRepository().update(entity);
    }

    /**
     * 保存对象
     *
     * @param entity 记录数据
     */
    public void save(T entity) {
        // TODOD
    }

    /**
     * 删除对象
     *
     * @param id
     */
    public void delete(Serializable id) {
        this.getRepository().delete(id);
    }

    /**
     * 根据查询对象获取满足条件的记录数量
     * @param querySupport 查询对象
     * @return 满足条件的记录数量
     */
    public int count(QuerySupport querySupport) {
        return this.getRepository().count(querySupport);
    }

    /**
     * 根据查询对象取得表或者集合中满足条件的第一笔数据
     *
     * @param querySupport 查询对象
     * @return 第一个符合查询对象的数据
     */
    public T findOne(QuerySupport querySupport) {
        return this.getRepository().findOne(querySupport);
    }

    /**
     * 根据查询对象取得表或者集合中满足条件的所有数据
     *
     * @param querySupport
     * @return 符合查询对象的所有数据
     */
    public List<T> find(QuerySupport querySupport) {
        return this.getRepository().find(querySupport);
    }

    /**
     * 根据查询对象获取满足条件的分页集合
     *
     * @param querySupport 查询对象
     * @return 符合添加的分页查询对象
     */
    public Pager<T> findPager(QuerySupport querySupport) {
        return this.getRepository().findPager(querySupport);
    }

}
