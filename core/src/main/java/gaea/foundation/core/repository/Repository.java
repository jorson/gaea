package gaea.foundation.core.repository;

import gaea.foundation.core.repository.query.QuerySupport;
import gaea.foundation.core.repository.support.Pager;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 基础的仓储类，不依赖于具体的ORM实现方案
 *
 * @author wuhy
 */
public interface Repository {
    /**
     * 根据ID和类型从表或者集合中取得单条数据
     *
     * @param id
     * @param name 表或者实体名
     * @return
     */
    Object get(String name, Serializable id);

    /**
     * 根据查询条件取得表或者集合中的单条数据
     *
     * @param name         表或者实体名
     * @param querySupport
     * @return
     */
    Object get(String name, QuerySupport querySupport);

    /**
     * 查询表或集合中所有数据
     *
     * @param name 表或集合名称
     * @return
     */
    public List findAll(String name);

    /**
     * 取得某个表或集合中满足条件的所有数据
     *
     * @param name         表或集合名称
     * @param querySupport 栏位与值列表
     * @return
     */
    public List find(String name, QuerySupport querySupport);

    /**
     * 取得某个表或集合中满足条件的条件
     *
     * @param name         表或集合名称
     * @param querySupport 查询对象
     * @return
     */
    public long count(String name, QuerySupport querySupport);

    /**
     * 根据查询对象分页数据，会返回totalCount
     *
     * @param name         表或集合名称
     * @param querySupport 查询对象
     * @return 含总记录数和当前页数据的Page对象.
     */
    public Pager pagedQuery(String name, QuerySupport querySupport);

    /**
     * 添加单条记录
     *
     * @param insertData 插入数据
     * @param name       表或集合名称
     */
    public void insert(String name, Map<String, Object> insertData);

    /**
     * 批量添加数据
     *
     * @param batchData 批量数据
     * @param name      表或集合名称
     * @return
     */
    public void insert(String name, Collection<Map<String, Object>> batchData);

    /**
     * 批量添加数据
     *
     * @param saveData 保存数据
     * @param name     表或集合名称
     * @return
     */
    public void save(String name, Map<String, Object> saveData);

    /**
     * 更新单条数据
     *                                                   s
     * @param id         ID
     * @param updateData 更新数据
     * @param name       表或集合名称
     * @return
     */
    public void update(String name, Serializable id, Map<String, Object> updateData);

    /**
     * 删除某个ID的记录
     *
     * @param id   栏位与值列表
     * @param name 表或集合名称
     * @return
     */
    public void delete(String name, Serializable id);
}
