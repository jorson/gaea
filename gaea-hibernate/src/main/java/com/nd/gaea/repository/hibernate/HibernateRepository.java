package com.nd.gaea.repository.hibernate;

import com.nd.gaea.core.repository.Repository;
import com.nd.gaea.core.repository.query.QuerySupport;
import com.nd.gaea.core.repository.query.criterion.Restrictions;
import com.nd.gaea.core.repository.support.Pager;
import com.nd.gaea.repository.hibernate.support.HibernateDaoSupport;
import com.nd.gaea.repository.hibernate.support.HibernateQueryParser;
import com.nd.gaea.utils.GenericsUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责为单个Entity对象提供CRUD操作的Hibernate Repository基类.
 * <p/>
 * 子类只要在类定义时指定所管理Entity的Class, 即拥有对单个Entity对象的CRUD操作.
 * <p/>
 * <pre>
 * public class UserRepository extends HibernateRepository&lt;User&gt; {
 * }
 * </pre>
 *
 * @author bifeng.liu
 */
public class HibernateRepository<T> extends HibernateDaoSupport implements Repository<T> {

    /**
     * 空分页信息
     */
    public final Pager<T> EMPTY_PAGER = new Pager(new ArrayList<T>(), 0);
    /**
     * DAO所管理的Entity类型
     */
    protected Class<T> entityClass;
    /**
     * Query转换器
     */
    protected HibernateQueryParser parser;

    /**
     * 在构造函数中将泛型T.class赋给entityClass.
     */
    public HibernateRepository() {
        entityClass = GenericsUtils.getSuperClassGenricType(getClass());
        parser = new HibernateQueryParser();
    }

    /**
     * 取得entityClass.JDK1.4不支持泛型的子类可以抛开Class<T> entityClass,重载此函数达到相同效果。
     */
    protected Class<T> getEntityClass() {
        return entityClass;
    }

    //-------------------------------------------------------------------------
    // 查询
    //-------------------------------------------------------------------------

    /**
     * 根据ID获取对象.
     * <p/>
     * 实际调用Hibernate的<code>session.load()</code>方法返回实体或其proxy对象. 如果对象不存在，抛出异常.
     *
     * @param id
     */
    public T load(Serializable id) {
        Assert.notNull(id);
        return getHibernateTemplate().load(getEntityClass(), id);
    }

    /**
     * 根据ID获取对象.
     * <p/>
     * 实际调用Hibernate的<code>session.get()</code>方法返回实体或其proxy对象. 如果对象不存在，返回Null.
     *
     * @param id
     */
    public T get(Serializable id) {
        Assert.notNull(id);
        return getHibernateTemplate().get(getEntityClass(), id);
    }

    /**
     * 根据ID列表取得数据列表
     *
     * @param ids
     * @return
     */
    public List<T> getList(Serializable[] ids) {
        String idName = getIdName(entityClass);
        return find(QuerySupport.createQuery().addCriterion(Restrictions.in(idName, ids)));
    }

    /**
     * 获取全部对象.
     *
     * @return 所有对象列表
     */
    public List<T> findAll() {
        return getHibernateTemplate().loadAll(getEntityClass());
    }

    @Override
    public T findOne(QuerySupport querySupport) {
        Assert.notNull(querySupport);
        Criteria criteria = getHibernateTemplate().createCriteria(getEntityClass());
        parser.parseCriterions(criteria, querySupport.getCriterions());
        parser.parseOrders(criteria, querySupport.getOrders());
        return (T) criteria.uniqueResult();
    }

    /**
     * 根据查询对象查询数据列表
     *
     * @param querySupport 查询对象
     * @return 符合条件的对象列表
     */
    public List<T> find(QuerySupport querySupport) {
        if (querySupport == null) {
            return findAll();
        }
        Criteria criteria = getHibernateTemplate().createCriteria(getEntityClass());
        parser.parse(criteria, querySupport);
        return criteria.list();
    }


    /**
     * 取得某个表或集合中满足条件的条数
     *
     * @param querySupport 查询对象
     * @return
     */
    public int count(QuerySupport querySupport) {
        Criteria criteria = this.getHibernateTemplate().createCriteria(entityClass);
        if (querySupport != null) {
            parser.parseCriterions(criteria, querySupport.getCriterions());
        }
        Long totalCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        return totalCount.intValue();
    }

    /**
     * 根据查询对象分页数据，会返回totalCount
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常.
     *
     * @param querySupport 查询对象
     * @return 含总记录数和当前页数据的Page对象.
     */
    public Pager<T> findPager(QuerySupport querySupport) {
        Assert.notNull(querySupport);
        int totalCount = count(querySupport);
        // 返回分页对象
        if (totalCount < 1) {
            return EMPTY_PAGER;
        }
        List<T> dataList = this.find(querySupport);
        return new Pager<T>(dataList, totalCount);
    }
    //-------------------------------------------------------------------------
    // 数据修改-添加、修改、删除
    //-------------------------------------------------------------------------

    /**
     * 保存对象.
     *
     * @param entity 实体
     */
    public void create(T entity) {
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
     * @param id
     */
    public void delete(Serializable id) {
        T entity = get(id);
        if (entity != null) {
            getHibernateTemplate().delete(entity);
        }
    }


    //-------------------------------------------------------------------------
    // 支持以上方法使用的内部支持
    //-------------------------------------------------------------------------

    /**
     * 取得对象的主键值,辅助函数.
     * 本方法为非事务相关方法.
     *
     * @param entity 实体
     */
    protected Serializable getId(T entity) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        Assert.notNull(entity);
        return (Serializable) PropertyUtils.getProperty(entity, getIdName(getEntityClass()));
    }

    /**
     * 取得对象的主键名,辅助函数.
     * 本方法为非事务相关方法.
     *
     * @param entityClass 实体类型
     */
    protected String getIdName(Class<T> entityClass) {
        Assert.notNull(entityClass);
        ClassMetadata meta = getSessionFactory().getClassMetadata(entityClass);
        Assert.notNull(meta, "Class " + entityClass + " not define in hibernate4 session factory.");
        String idName = meta.getIdentifierPropertyName();
        Assert.hasText(idName, entityClass.getSimpleName() + " has no identifier property define.");
        return idName;
    }

}
