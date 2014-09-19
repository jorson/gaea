package gaea.access.hibernate;

import gaea.foundation.core.bo.EntityObject;
import gaea.foundation.core.repository.Repository;
import gaea.access.hibernate.support.HibernateDaoSupport;
import gaea.access.hibernate.support.HibernateQueryParser;
import gaea.foundation.core.repository.query.QuerySupport;
import gaea.foundation.core.repository.shard.ShardDataRegister;
import gaea.foundation.core.repository.shard.ShardInfo;
import gaea.foundation.core.repository.shard.ShardParameter;
import gaea.foundation.core.repository.support.Pager;
import gaea.foundation.core.repository.support.SqlUtils;
import gaea.foundation.core.utils.GenericsUtils;
import org.hibernate.Criteria;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;

/**
 * 负责为单个Entity对象提供CRUD操作的Hibernate DAO基类.
 * <p/>
 * 子类只要在类定义时指定所管理Entity的Class, 即拥有对单个Entity对象的CRUD操作.
 * <p/>
 * <pre>
 * public class UserManager extends HibernateShardDao&lt;User&gt; {
 * }
 * </pre>
 *
 * @author wuhy
 * @see HibernateGenericDao
 */
public class HibernateShardDao<T extends EntityObject> extends HibernateEntityExtendDao<T> {

    protected ShardInfo shardInfo;


    /**
     * 在构造函数中将泛型T.class赋给entityClass.
     * 初始化entity信息.
     */
    public HibernateShardDao() {
        shardInfo = new ShardInfo(entityClass);
    }

    //-------------------------------------------------------------------------
    // 查询
    //-------------------------------------------------------------------------

    /**
     * 根据ID获取对象.
     * <p/>
     * 实际调用Hibernate的<code>session.load()</code>方法返回实体或其proxy对象. 如果对象不存在，抛出异常.
     * <p>本方法是事务安全的.</p>
     *
     * @param id
     */
    public T get(Serializable id, ShardParameter shardParameter) {
        Assert.notNull(id);
        register(shardParameter);
        return get(id);
    }

    /**
     * 根据查询对象查询唯一对象.
     *
     * @param querySupport 查询对象
     * @return 符合条件的唯一对象 or null if not found.
     */
    public T get(QuerySupport querySupport) {
        if (querySupport != null) {
            parser.parsePartitionInfos(querySupport.getPartitionInfos());
        }
        return get(querySupport);
    }

    /**
     * 根据查询对象获取全部对象，查询对象的分页信息将不会起作用。
     *
     * @param querySupport 查询对象
     * @return 符合条件的对象列表
     */
    public List<T> findAll(QuerySupport querySupport) {
        if (querySupport != null) {
            parser.parsePartitionInfos(querySupport.getPartitionInfos());
        }
        return findAll(getEntityClass(), querySupport);
    }

    /**
     * 根据查询对象取得表或者集合中满足条件的所有数据
     *
     * @param queryObject
     * @return
     */
    public List<T> find(T queryObject) {
        Assert.notNull(queryObject);
        register(shardInfo.getShardParamter(queryObject));
        return getHibernateTemplate().findByExample(queryObject);
    }

    /**
     * 根据属性名和属性值查询对象.
     *
     * @param entityClass
     * @param querySupport 查询对象
     * @return 符合条件的对象列表
     */
    public List<T> find(QuerySupport querySupport) {
        if (querySupport != null) {
            parser.parsePartitionInfos(querySupport.getPartitionInfos());
        }
        return find(getEntityClass(), querySupport);
    }

    /**
     * 根据ID列表取得数据列表
     *
     * @param ids
     * @return
     */
    public List<T> find(Serializable[] ids, ShardParameter shardParameter) {
        register(shardParameter);
        return find(getEntityClass(), ids);
    }

    /**
     * 根据查询对象分页数据，会返回totalCount
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常.
     *
     * @param querySupport 查询对象
     * @return 含总记录数和当前页数据的Page对象.
     */
    public Pager pagedQuery(QuerySupport querySupport) {
        if (querySupport != null) {
            parser.parsePartitionInfos(querySupport.getPartitionInfos());
        }
        return this.pagedQuery(this.getEntityClass(), querySupport);
    }
    //-------------------------------------------------------------------------
    // 数据修改-添加、修改、删除
    //-------------------------------------------------------------------------

    /**
     * 保存对象.
     *
     * @param entity 实体
     */
    public void insert(T entity) {
        register(shardInfo.getShardParamter(entity));
        getHibernateTemplate().save(entity);
    }

    /**
     * 保存或者更新对象，如果对象已经存在则更新，该方法不适合手动生成ID的实体
     *
     * @param entity 实体
     */
    public void save(T entity) {
        register(shardInfo.getShardParamter(entity));
        super.save(entity);
    }

    /**
     * 更新对象
     *
     * @param entity 实体
     */
    public void update(T entity) {
        register(shardInfo.getShardParamter(entity));
        getHibernateTemplate().update(entity);
    }

    /**
     * 删除对象.
     *
     * @param entity 对象
     */
    public void delete(T entity) {
        register(shardInfo.getShardParamter(entity));
        getHibernateTemplate().delete(entity);
    }

    /**
     * 根据ID删除对象.
     *
     * @param id 主键值
     */
    public void deleteById(Serializable id, ShardParameter shardParameter) {
        register(shardParameter);
        T entity = get(getEntityClass(), id);
        if (entity == null) {
            register(shardParameter);
            delete(entity);
        }
    }

    /**
     * 根据ID数组删除对象.
     * <p/>
     * 该方法是对象逐个删除，会产生多条SQL语句，如果删除量比较大时会有性能问题
     *
     * @param ids 实体id数组
     */
    public void deleteByIds(final Serializable[] ids, ShardParameter shardParameter) {
        register(shardParameter);
        this.deleteByIds(getEntityClass(), ids);
    }

    //-------------------------------------------------------------------------
    // 业务功能
    //-------------------------------------------------------------------------

    protected void register(ShardParameter shardParameter) {
        if (shardParameter != null) {
            ShardDataRegister.register(shardInfo.getStrategy().getPartitionInfo(shardParameter));
        }
    }

}
