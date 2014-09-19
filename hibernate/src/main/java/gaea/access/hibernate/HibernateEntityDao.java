package gaea.access.hibernate;

import gaea.foundation.core.bo.EntityObject;
import gaea.foundation.core.repository.EntityRepository;
import gaea.foundation.core.repository.query.QuerySupport;
import gaea.foundation.core.repository.support.Pager;
import gaea.foundation.core.utils.GenericsUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;

/**
 * 负责为单个Entity对象提供CRUD操作的Hibernate DAO基类.
 * <p/>
 * 子类只要在类定义时指定所管理Entity的Class, 即拥有对单个Entity对象的CRUD操作.
 * <p/>
 * <pre>
 * public class UserManager extends HibernateEntityDao&lt;User&gt; {
 * }
 * </pre>
 *
 * @author wuhy
 * @see gaea.access.hibernate.HibernateGenericDao
 */
public class HibernateEntityDao<T extends EntityObject> extends HibernateGenericDao implements EntityRepository<T> {

    protected Class<T> entityClass;// DAO所管理的Entity类型.

    /**
     * 在构造函数中将泛型T.class赋给entityClass.
     */
    public HibernateEntityDao() {
        entityClass = GenericsUtils.getSuperClassGenricType(getClass());
    }

    /**
     * 取得entityClass.JDK1.4不支持泛型的子类可以抛开Class<T> entityClass,重载此函数达到相同效果。
     */
    protected Class<T> getEntityClass() {
        return entityClass;
    }

    //-------------------------------------------------------------------------
    // 功能
    //-------------------------------------------------------------------------
    public void refresh(T entity) {
        getHibernateTemplate().refresh(entity);
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
    public T load(Serializable id) {
        Assert.notNull(id);
        return load(getEntityClass(), id);
    }

    /**
     * 根据查询对象查询唯一对象.
     *
     * @param querySupport 查询对象
     * @return 符合条件的唯一对象 or null if not found.
     */
    public T get(QuerySupport querySupport) {
        return findUniqueBy(getEntityClass(), querySupport);
    }

    /**
     * 根据ID获取对象.
     * <p/>
     * 实际调用Hibernate的<code>session.get()</code>方法返回实体或其proxy对象. 如果对象不存在，返回Null.
     * <p>本方法是事务安全的.</p>
     *
     * @param entityClass
     * @param id
     */
    public T get(Serializable id) {
        Assert.notNull(id);
        return get(getEntityClass(), id);
    }

    /**
     * 根据属性名和属性值查询单个对象.
     *
     * @return 符合条件的唯一对象 or null
     */
    public T findUniqueBy(String propertyName, Object value) {
        return findUniqueBy(getEntityClass(), propertyName, value);
    }

    /**
     * 获取全部对象.
     *
     * @return 所有对象列表
     */
    public List<T> findAll() {
        return findAll(getEntityClass());
    }

    /**
     * 根据查询对象获取全部对象，查询对象的分页信息将不会起作用。
     *
     * @param querySupport 查询对象
     * @return 符合条件的对象列表
     */
    public List<T> findAll(QuerySupport querySupport) {
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
        return find(getEntityClass(), querySupport);
    }


    /**
     * 根据ID列表取得数据列表
     *
     * @param ids
     * @return
     */
    public List<T> find(Serializable[] ids) {
        return find(getEntityClass(), ids);
    }

    /**
     * 取得某个表或集合中满足条件的条数
     *
     * @param querySupport 查询对象
     * @return
     */
    public long count(QuerySupport querySupport) {
        return this.count(getEntityClass(), querySupport);
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
        getHibernateTemplate().save(entity);
    }

    /**
     * 保存或者更新对象，如果对象已经存在则更新，该方法不适合手动生成ID的实体
     *
     * @param entity 实体
     */
    public void save(T entity) {
        getHibernateTemplate().saveOrUpdate(entity);
    }

    /**
     * 更新对象
     *
     * @param entity 实体
     */
    public void update(T entity) {
        getHibernateTemplate().update(entity);
    }

    /**
     * 删除对象.
     *
     * @param entity 对象
     */
    public void delete(T entity) {
        getHibernateTemplate().delete(entity);
    }

    /**
     * 根据ID删除对象.
     *
     * @param id 主键值
     */
    public void deleteById(Serializable id) {
        T entity = get(getEntityClass(), id);
        if (entity != null) {
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
    public void deleteByIds(final Serializable[] ids) {
        this.deleteByIds(getEntityClass(), ids);
    }

    //-------------------------------------------------------------------------
    // 业务功能
    //-------------------------------------------------------------------------


    /**
     * 判断对象某些属性的值在数据库中唯一.
     *
     * @param uniquePropertyNames 在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
     */
    public boolean isUnique(Object entity, String uniquePropertyNames) {
        return isUnique(getEntityClass(), entity, uniquePropertyNames);
    }


}
