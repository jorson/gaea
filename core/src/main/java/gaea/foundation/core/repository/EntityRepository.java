package gaea.foundation.core.repository;

import gaea.foundation.core.bo.EntityObject;
import gaea.foundation.core.repository.query.QuerySupport;
import gaea.foundation.core.repository.support.Pager;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 针对单个Entity对象的操作定义
 *
 * @param <T>
 * @author wuhy
 */
public interface EntityRepository<T extends EntityObject> {

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
     * 取得某个表或集合中满足条件的条数
     *
     * @param querySupport 查询对象
     * @return
     */
    public long count(QuerySupport querySupport);

    /**
     * 删除对象
     *
     * @param removeObject
     */
    void delete(T removeObject);

    /**
     * 根据查询对象分页数据，会返回totalCount
     *
     * @param querySupport 查询对象
     * @return 含总记录数和当前页数据的Page对象.
     */
    public Pager pagedQuery(QuerySupport querySupport);

    /**
     * 添加单条记录数据
     *
     * @param addData 记录数据
     */
    void insert(T addData);

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
     * @param updateObject 要删除的对象
     */
    void update(T updateObject);

    /**
     * 根据ID删除对象
     *
     * @param id
     */
    void deleteById(Serializable id);

    /**
     * 根据ID数组删除对象.
     * <p/>
     * 该方法是对象逐个删除，会产生多条SQL语句，如果删除量比较大时会有性能问题
     *
     * @param ids 实体id数组
     */
    public void deleteByIds(final Serializable[] ids);

    /**
     * 刷新对象
     *
     * @param entity
     */
    public void refresh(T entity);
}
