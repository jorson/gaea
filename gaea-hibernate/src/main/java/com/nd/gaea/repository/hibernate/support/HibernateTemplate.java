package com.nd.gaea.repository.hibernate.support;

import com.nd.gaea.repository.hibernate.utils.HibernateSessionFactoryUtils;
import com.nd.gaea.repository.hibernate.utils.HibernateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 定义Hibernate操作的模板类，提供所有的Hibernate数据库操作类使用。
 *
 * @author bifeng.liu
 */
public class HibernateTemplate extends HibernateAccessor {

    private final static Log LOGGER = LogFactory.getLog(HibernateTemplate.class);

    private boolean cacheQueries = false;

    private String queryCacheRegion;

    private int fetchSize = 0;

    private int maxResults = 0;

    /**
     * 创建新的HibernateTemplate实例.
     */
    public HibernateTemplate() {
    }

    /**
     * 创建新的HibernateTemplate实例
     *
     * @param sessionFactory Hibernate SessionFactory工厂类用于创建Session
     */
    public HibernateTemplate(SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
        afterPropertiesSet();
    }


    /**
     * 是否使用缓存查询，如果设置为true，那么该模板下所有的Query和Criteria对象
     * 都会生效，如果想使用自定义的Cache Region，要设置queryCacheRegion
     *
     * @see #setQueryCacheRegion
     * @see org.hibernate.Query#setCacheable
     * @see org.hibernate.Criteria#setCacheable
     */
    public void setCacheQueries(boolean cacheQueries) {
        this.cacheQueries = cacheQueries;
    }

    /**
     * 返回缓存查询
     */
    public boolean isCacheQueries() {
        return this.cacheQueries;
    }

    /**
     * 设置自定义的Cache Region的，当cacheQueries为true时才生效
     *
     * @see #setCacheQueries
     * @see org.hibernate.Query#setCacheRegion
     * @see org.hibernate.Criteria#setCacheRegion
     */
    public void setQueryCacheRegion(String queryCacheRegion) {
        this.queryCacheRegion = queryCacheRegion;
    }

    /**
     * 返回定义的Cache Region
     */
    public String getQueryCacheRegion() {
        return this.queryCacheRegion;
    }

    /**
     * 设置这个Hibernate模板的Fetch Size（每次读取的记录条数）
     * 当数据量比较大时，这个参数非常重要。
     * <p>默认为0，使用JDBC driver的默认处理</p>
     */
    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    /**
     * 返回Fetch Size
     */
    public int getFetchSize() {
        return this.fetchSize;
    }

    /**
     * 设置这个Hibernate模板的MaxResults
     * <p>默认为0，使用JDBC driver的默认处理</p>
     *
     * @param maxResults
     */
    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    /**
     * 返回Max Results
     */
    public int getMaxResults() {
        return this.maxResults;
    }

    /**
     * 取得当前的Session
     *
     * @return the Session to use (never {@code null})
     */
    public Session getSession() {
        try {
            return getSessionFactory().getCurrentSession();
        } catch (HibernateException ex) {
            //throw new DataAccessResourceFailureException("Could not obtain current Hibernate Session", ex);
            LOGGER.info("Could not obtain current Hibernate Session", ex);
            Session session = openSession(getSessionFactory());
            SessionHolder sessionHolder = new SessionHolder(session);
            TransactionSynchronizationManager.bindResource(getSessionFactory(), sessionHolder);
            return session;
        }
    }

    protected Session openSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
        try {
            Session session = sessionFactory.openSession();
            //       session.setFlushMode(FlushMode.MANUAL);
            return session;
        } catch (HibernateException ex) {
            throw new DataAccessResourceFailureException("Could not open Hibernate Session", ex);
        }
    }

    //-------------------------------------------------------------------------
    // Convenience methods for loading individual objects
    //-------------------------------------------------------------------------

    public <T> T get(final Class<T> entityClass, final Serializable id) throws DataAccessException {
        return (T) getSession().get(entityClass, id);
    }

    public <T> T load(final Class<T> entityClass, final Serializable id) throws DataAccessException {
        return (T) getSession().load(entityClass, id);
    }

    public <T> List<T> loadAll(final Class<T> entityClass) throws DataAccessException {
        Criteria criteria = getSession().createCriteria(entityClass);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        prepareCriteria(criteria);
        return criteria.list();
    }

    public void initialize(Object proxy) throws DataAccessException {
        try {
            Hibernate.initialize(proxy);
        } catch (HibernateException ex) {
            throw SessionFactoryUtils.convertHibernateAccessException(ex);
        }
    }


    //-------------------------------------------------------------------------
    // Convenience methods for storing individual objects
    //-------------------------------------------------------------------------

    public void lock(final Object entity, final LockOptions lockOptions) throws DataAccessException {
        getSession().buildLockRequest(lockOptions).lock(entity);
    }

    public Serializable save(final Object entity) throws DataAccessException {
        checkWriteOperationAllowed(getSession());
        return getSession().save(entity);
    }

    public void update(final Object entity) throws DataAccessException {
        update(entity, null);
    }

    public void update(final Object entity, final LockOptions lockOptions) throws DataAccessException {
        checkWriteOperationAllowed(getSession());
        getSession().update(entity);
        if (lockOptions != null) {
            getSession().buildLockRequest(lockOptions).lock(entity);
        }
    }

    public void saveOrUpdate(final Object entity) throws DataAccessException {
        checkWriteOperationAllowed(getSession());
        getSession().saveOrUpdate(entity);
    }

    public void delete(final Object entity) throws DataAccessException {
        delete(entity, null);
    }

    public void delete(final Object entity, final LockOptions lockOptions) throws DataAccessException {
        checkWriteOperationAllowed(getSession());
        if (lockOptions != null) {
            getSession().buildLockRequest(lockOptions).lock(entity);
        }
        getSession().delete(entity);
    }

    //-------------------------------------------------------------------------
    // Convenience finder methods for HQL strings
    //-------------------------------------------------------------------------

    public Query createQuery(final String hql) {
        Assert.hasText(hql);
        Query query = getSession().createQuery(hql);
        return query;
    }

    /**
     * 创建Query对象. 对于需要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设置.
     * 留意可以连续设置,如下：
     * <pre>
     * dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
     * </pre>
     * 调用方式如下：
     * <pre>
     *        dao.createQuery(hql)
     *        dao.createQuery(hql,arg0);
     *        dao.createQuery(hql,arg0,arg1);
     *        dao.createQuery(hql,new Object[arg0,arg1,arg2])
     * </pre>
     * <p/>
     *
     * @param values 可变参数.
     */
    public Query createQuery(final String hql, Object... values) {
        Assert.hasText(hql);
        Query query = getSession().createQuery(hql);
        HibernateUtils.applyParameters(query, values);
        return query;
    }

    /**
     * 获取HQL查询对象.
     */
    public Query createQuery(final String hql, final Map<String, Object> parameters) {
        Assert.hasText(hql);
        Query query = getSession().createQuery(hql);
        HibernateUtils.applyParameters(query, parameters);
        return query;
    }

    public List find(final String queryString) throws DataAccessException {
        return find(queryString, null, (Object[]) null);
    }

    public List find(final String queryString, final Class transformerClazz, final Object... values) throws DataAccessException {
        Query queryObject = getSession().createQuery(queryString);
        if (transformerClazz != null) {
            // 如果传入的类继承自Map，则使用Transformers.ALIAS_TO_ENTITY_MAP来转换
            if (transformerClazz.isAssignableFrom(Map.class)) {
                queryObject.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            } else {
                queryObject.setResultTransformer(Transformers.aliasToBean(transformerClazz));
            }
        }
        prepareQuery(queryObject);
        HibernateUtils.applyParameters(queryObject, values);
        return queryObject.list();
    }

    public List findByNamedParam(final String queryString, final Class transformerClazz, final Map<String, Object> values) throws DataAccessException {
        Query queryObject = getSession().createQuery(queryString);
        if (transformerClazz != null) {
            // 如果传入的类继承自Map，则使用Transformers.ALIAS_TO_ENTITY_MAP来转换
            if (transformerClazz.isAssignableFrom(Map.class)) {
                queryObject.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            } else {
                queryObject.setResultTransformer(Transformers.aliasToBean(transformerClazz));
            }
        }
        prepareQuery(queryObject);
        HibernateUtils.applyParameters(queryObject, values);
        return queryObject.list();
    }


    //-------------------------------------------------------------------------
    //Convenience finder methods for sql queries
    //-------------------------------------------------------------------------

    /**
     * 创建Native查询对象.
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常
     */
    public SQLQuery createSQLQuery(final String sql, final Class transformerClazz, final Map<String, Object> parameters, final Map<String, Type> scalarTypes) {
        Assert.hasText(sql);
        SQLQuery query = getSession().createSQLQuery(sql);
        HibernateUtils.applyParameters(query, parameters);
        HibernateUtils.applyScalars(query, scalarTypes);
        if (transformerClazz != null) {
            query.setResultTransformer(Transformers.aliasToBean(transformerClazz));
        }
        return query;
    }

    //-------------------------------------------------------------------------
    // Convenience finder methods for detached criteria
    //-------------------------------------------------------------------------

    /**
     * 创建Criteria对象.
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常.
     *
     * @param criterions 可变的Restrictions条件列表,见{@link #createQuery(String, Object...)}
     */
    public <T> Criteria createCriteria(Class<T> entityClass, Criterion... criterions) {
        Criteria criteria = getSession().createCriteria(entityClass);
        for (Criterion c : criterions) {
            criteria.add(c);
        }
        return criteria;
    }

    /**
     * 创建Criteria对象，带排序字段与升降序字段.
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常.
     *
     * @see #createCriteria(Class, org.hibernate.criterion.Criterion...)
     */
    public <T> Criteria createCriteria(Class<T> entityClass, String orderBy, Criterion... criterions) {
        Criteria criteria = createCriteria(entityClass, criterions);
        HibernateUtils.applyOrders(criteria, orderBy);
        return criteria;
    }

    public List findByCriteria(DetachedCriteria criteria) throws DataAccessException {
        return findByCriteria(criteria, -1, -1);
    }

    public List findByCriteria(final DetachedCriteria criteria, final int firstResult, final int maxResults)
            throws DataAccessException {
        Assert.notNull(criteria, "DetachedCriteria must not be null");
        Criteria executableCriteria = criteria.getExecutableCriteria(getSession());
        prepareCriteria(executableCriteria);
        if (firstResult >= 0) {
            executableCriteria.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            executableCriteria.setMaxResults(maxResults);
        }
        return executableCriteria.list();

    }

    //-------------------------------------------------------------------------
    // 通用的方法，用于上述操作
    //-------------------------------------------------------------------------

    /**
     * 检查方法是否能够在Session中更新数据
     * <p>如果是{@code FlushMode.MANUAL}则会直接抛出InvalidDataAccessApiUsageException异常
     *
     * @param session 要检查的Hibernate Session
     * @throws org.springframework.dao.InvalidDataAccessApiUsageException 如果不允许更新数据，则抛出异常
     * @see org.hibernate.Session#getFlushMode()
     * @see org.hibernate.FlushMode#MANUAL
     */
    protected void checkWriteOperationAllowed(Session session) throws InvalidDataAccessApiUsageException {
        if (getSession().getFlushMode().lessThan(FlushMode.COMMIT)) {
            throw new InvalidDataAccessApiUsageException(
                    "Write operations are not allowed in read-only mode (FlushMode.MANUAL): " +
                            "Turn your Session into FlushMode.COMMIT/AUTO or remove 'readOnly' marker from transaction definition.");
        }
    }

    /**
     * 准备Query的参数，设置默认是否需要缓存、每次记录取得最大数以及事务过期时间
     *
     * @param queryObject 要设置Query
     * @see #setCacheQueries
     * @see #setQueryCacheRegion
     * @see HibernateSessionFactoryUtils#applyTransactionTimeout
     */
    protected void prepareQuery(Query queryObject) {
        if (isCacheQueries()) {
            queryObject.setCacheable(true);
            if (getQueryCacheRegion() != null) {
                queryObject.setCacheRegion(getQueryCacheRegion());
            }
        }
        if (getFetchSize() > 0) {
            queryObject.setFetchSize(getFetchSize());
        }
        if (getMaxResults() > 0) {
            queryObject.setMaxResults(getMaxResults());
        }
        HibernateSessionFactoryUtils.applyTransactionTimeout(queryObject, getSessionFactory());
    }

    /**
     * 准备Criteria的参数，设置默认是否需要缓存、每次记录取得最大数以及事务过期时间t.
     *
     * @param criteria 要设置Criteria
     * @see #setCacheQueries
     * @see #setQueryCacheRegion
     * @see HibernateSessionFactoryUtils#applyTransactionTimeout
     */
    protected void prepareCriteria(Criteria criteria) {
        if (isCacheQueries()) {
            criteria.setCacheable(true);
            if (getQueryCacheRegion() != null) {
                criteria.setCacheRegion(getQueryCacheRegion());
            }
        }
        if (getFetchSize() > 0) {
            criteria.setFetchSize(getFetchSize());
        }
        if (getMaxResults() > 0) {
            criteria.setMaxResults(getMaxResults());
        }
        HibernateSessionFactoryUtils.applyTransactionTimeout(criteria, getSessionFactory());
    }
}
