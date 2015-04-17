package com.nd.gaea.repository.hibernate.object;

import com.nd.gaea.core.repository.Repository;
import com.nd.gaea.core.repository.query.QuerySupport;
import com.nd.gaea.core.repository.support.Pager;
import com.nd.gaea.core.service.DomainService;

import java.io.Serializable;
import java.util.List;

/**
 * Entity Dao的接口
 *
 * @author bifeng.liu
 */
@DomainService
public interface EntityRepository extends Repository<SimpleEntity> {
    /**
     * 根据ID从表或者集合中取得单条数据
     *
     * @param id
     * @return
     */
    public SimpleEntity get(Serializable id);


    /**
     * 根据ID列表取得记录列表
     *
     * @param ids
     * @return
     */
    public List<SimpleEntity> getList(Serializable[] ids);

    /**
     * 插入对象
     *
     * @param entity 记录数据
     */
    public void create(SimpleEntity entity);

    /**
     * 修改对象
     *
     * @param entity 要删除的对象
     */
    public void update(SimpleEntity entity);

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
    public SimpleEntity findOne(QuerySupport querySupport);

    /**
     * 查询所有数据
     *
     * @return
     */
    public List<SimpleEntity> findAll();


    /**
     * 根据查询满足条件的所有数据
     *
     * @param querySupport 查询对象
     * @return
     */
    public List<SimpleEntity> find(QuerySupport querySupport);

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

    /**
     * 删除表所有数据,用于测试
     */
    public void deleteAll();

    /**
     * 判断对象某些属性的值在数据库中是否唯一.
     *
     * @param uniquePropertyNames 在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
     */
    public boolean isUnique(SimpleEntity entity, String uniquePropertyNames);

    /**
     * 测试抛出错误，事务是否回滚
     */
    public void testThrowException() throws Exception;

}
