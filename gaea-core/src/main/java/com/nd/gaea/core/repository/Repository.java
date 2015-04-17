package com.nd.gaea.core.repository;

import com.nd.gaea.core.repository.query.QuerySupport;
import com.nd.gaea.core.repository.support.Pager;

import java.io.Serializable;
import java.util.List;

/**
 * 针对单个Entity对象的操作接口定义
 *
 * @param <T>
 * @author bifeng.liu
 */
public interface Repository<T> {

    /**
     * 根据ID从表或者集合中取得单条数据
     *
     * @param id
     * @return
     */
    public T get(Serializable id);


    /**
     * 根据ID列表取得记录列表
     *
     * @param ids
     * @return
     */
   public List<T> getList(Serializable[] ids);

    /**
     * 插入对象
     *
     * @param entity 记录数据
     */
    public void create(T entity);

    /**
     * 修改对象
     *
     * @param entity 要删除的对象
     */
    public void update(T entity);

    /**
     * 删除对象
     *
     * @param
     */
    public void delete(Serializable id);

    /**
     * 根据查询对象取得表或者集合中满足条件的所有数据
     *
     * @param querySupport 查询对象
     * @return
     */
    public  T findOne(QuerySupport querySupport);

    /**
     * 查询所有数据
     *
     * @return
     */
    public List<T> findAll();


    /**
     * 根据查询满足条件的所有数据
     *
     * @param querySupport 查询对象
     * @return
     */
    public  List<T> find(QuerySupport querySupport);

    /**
     * 取得满足条件的条数
     *
     * @param querySupport 查询对象
     * @return
     */
    public int count(QuerySupport querySupport);

    /**
     * 根据查询对象分页数据，会返回totalCount
     *
     * @param querySupport 查询对象
     * @return 含总记录数和当前页数据的Page对象.
     */
    public Pager findPager(QuerySupport querySupport);
}
