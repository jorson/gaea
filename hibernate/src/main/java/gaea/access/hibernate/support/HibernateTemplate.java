package gaea.access.hibernate.support;


import gaea.foundation.core.repository.shard.ShardParameter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;

/**
 * 定义Hibernate操作的模板类，提供所有的Hibernate数据库操作类使用。
 *
 * @author wuhy
 */
public class HibernateTemplate extends HibernateAccessor {

    private static Log logger = LogFactory.getLog(HibernateTemplate.class);

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
     * 设置这个Hibernate模板的Fetch Size
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
            throw new DataAccessResourceFailureException("Could not obtain current Hibernate Session", ex);
            //logger.info("Could not obtain current Hibernate Session", ex);
            //return getSessionFactory().openSession();
        }
    }

    //-------------------------------------------------------------------------
    // Convenience methods for loading individual objects
    //-------------------------------------------------------------------------

    public <T> T get(Class<T> entityClass, Serializable id) throws DataAccessException {
        return (T) getSession().get(entityClass, id);
    }


    public Object get(String entityName, Serializable id) throws DataAccessException {
        return getSession().get(entityName, id);
    }

    public <T> T load(Class<T> entityClass, Serializable id, ShardParameter shardParameter) throws DataAccessException {
        return (T) getSession().load(entityClass, id);
    }

    public <T> T load(Class<T> entityClass, Serializable id) throws DataAccessException {
        return (T) getSession().load(entityClass, id);
    }

    public Object load(String entityName, Serializable id) throws DataAccessException {
        return getSession().load(entityName, id);
    }

    public <T> List<T> loadAll(final Class<T> entityClass) throws DataAccessException {
        Criteria criteria = getSession().createCriteria(entityClass);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        prepareCriteria(criteria);
        return criteria.list();
    }

    public void load(final Object entity, final Serializable id) throws DataAccessException {
        getSession().load(entity, id);
    }

    public void refresh(final Object entity) throws DataAccessException {
        getSession().refresh(entity);
    }

    public boolean contains(final Object entity) throws DataAccessException {
        return getSession().contains(entity);
    }

    public void evict(final Object entity) throws DataAccessException {
        getSession().evict(entity);
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

    public void lock(final String entityName, final Object entity, final LockOptions lockOptions)
            throws DataAccessException {
        getSession().buildLockRequest(lockOptions).lock(entityName, entity);
    }

    public Serializable save(final Object entity) throws DataAccessException {
        checkWriteOperationAllowed(getSession());
        return getSession().save(entity);

    }

    public Serializable save(final String entityName, final Object entity) throws DataAccessException {
        checkWriteOperationAllowed(getSession());
        return getSession().save(entityName, entity);
    }

    public void update(Object entity) throws DataAccessException {
        update(entity, null);
    }

    public void update(final Object entity, final LockOptions lockOptions) throws DataAccessException {
        checkWriteOperationAllowed(getSession());
        getSession().update(entity);
        if (lockOptions != null) {
            getSession().buildLockRequest(lockOptions).lock(entity);
        }
    }

    public void update(String entityName, Object entity) throws DataAccessException {
        update(entityName, entity, null);
    }

    public void update(final String entityName, final Object entity, final LockOptions lockOptions)
            throws DataAccessException {
        checkWriteOperationAllowed(getSession());
        getSession().update(entityName, entity);
        if (lockOptions != null) {
            getSession().buildLockRequest(lockOptions).lock(entity);
        }
    }

    public void saveOrUpdate(final Object entity) throws DataAccessException {
        checkWriteOperationAllowed(getSession());
        getSession().saveOrUpdate(entity);
    }

    public void saveOrUpdate(final String entityName, final Object entity) throws DataAccessException {
        checkWriteOperationAllowed(getSession());
        getSession().saveOrUpdate(entityName, entity);
    }

    public void saveOrUpdateAll(final Collection entities) throws DataAccessException {
        checkWriteOperationAllowed(getSession());
        for (Object entity : entities) {
            getSession().saveOrUpdate(entity);
        }
    }

    public void replicate(final Object entity, final ReplicationMode replicationMode)
            throws DataAccessException {
        checkWriteOperationAllowed(getSession());
        getSession().replicate(entity, replicationMode);
    }

    public void replicate(final String entityName, final Object entity, final ReplicationMode replicationMode)
            throws DataAccessException {
        checkWriteOperationAllowed(getSession());
        getSession().replicate(entityName, entity, replicationMode);
    }

    public void persist(final Object entity) throws DataAccessException {
        checkWriteOperationAllowed(getSession());
        getSession().persist(entity);
    }

    public void persist(final String entityName, final Object entity) throws DataAccessException {
        checkWriteOperationAllowed(getSession());
        getSession().persist(entityName, entity);
    }

    public <T> T merge(final T entity) throws DataAccessException {
        checkWriteOperationAllowed(getSession());
        return (T) getSession().merge(entity);
    }

    public <T> T merge(final String entityName, final T entity) throws DataAccessException {
        checkWriteOperationAllowed(getSession());
        return (T) getSession().merge(entityName, entity);
    }

    public void delete(Object entity) throws DataAccessException {
        delete(entity, null);
    }

    public void delete(final Object entity, final LockOptions lockOptions) throws DataAccessException {
        checkWriteOperationAllowed(getSession());
        if (lockOptions != null) {
            getSession().buildLockRequest(lockOptions).lock(entity);
        }
        getSession().delete(entity);
    }

    public void delete(String entityName, Object entity) throws DataAccessException {
        delete(entityName, entity, null);
    }

    public void delete(final String entityName, final Object entity, final LockOptions lockOptions)
            throws DataAccessException {
        checkWriteOperationAllowed(getSession());
        if (lockOptions != null) {
            getSession().buildLockRequest(lockOptions).lock(entityName, entity);
        }
        getSession().delete(entityName, entity);
    }

    public void deleteAll(final Collection entities) throws DataAccessException {
        checkWriteOperationAllowed(getSession());
        for (Object entity : entities) {
            getSession().delete(entity);
        }
    }

    public void flush() throws DataAccessException {
        getSession().flush();
    }

    public void clear() throws DataAccessException {
        getSession().clear();
    }


    //-------------------------------------------------------------------------
    // Convenience finder methods for HQL strings
    //-------------------------------------------------------------------------

    public Query createQuery(String hql) {
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
    public Query createQuery(String hql, Object... values) {
        Assert.hasText(hql);
        Query query = getSession().createQuery(hql);
        HibernateUtils.applyParameters(query, values);
        return query;
    }

    /**
     * 获取HQL查询对象.
     */
    public Query createQuery(String hql, Map<String, Object> parameters) {
        Assert.hasText(hql);
        Query query = getSession().createQuery(hql);
        HibernateUtils.applyParameters(query, parameters);
        return query;
    }

    public List find(String queryString) throws DataAccessException {
        return find(queryString, (Object[]) null);
    }

    public List find(String queryString, Object value) throws DataAccessException {
        return find(queryString, new Object[]{value});
    }

    public List find(final String queryString, final Object... values) throws DataAccessException {
        Query queryObject = getSession().createQuery(queryString);
        prepareQuery(queryObject);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                queryObject.setParameter(i, values[i]);
            }
        }
        return queryObject.list();
    }

    public List find(final String queryString, Class transformerClazz, final Object... values) throws DataAccessException {
        Query queryObject = getSession().createQuery(queryString);
        if (transformerClazz != null) {
            queryObject.setResultTransformer(Transformers.aliasToBean(transformerClazz));
        } else {
            queryObject.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        }
        prepareQuery(queryObject);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                queryObject.setParameter(i, values[i]);
            }
        }
        return queryObject.list();
    }

    public List findByNamedParam(String queryString, String paramName, Object value) throws DataAccessException {
        return findByNamedParam(queryString, new String[]{paramName}, new Object[]{value});
    }

    public List findByNamedParam(final String queryString, final String[] paramNames, final Object[] values, Class transformerClazz)
            throws DataAccessException {
        if (paramNames.length != values.length) {
            throw new IllegalArgumentException("Length of paramNames array must match length of values array");
        }
        Query queryObject = getSession().createQuery(queryString);
        if (transformerClazz != null) {
            queryObject.setResultTransformer(Transformers.aliasToBean(transformerClazz));
        } else {
            queryObject.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        }
        prepareQuery(queryObject);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                HibernateUtils.applyParameter(queryObject, paramNames[i], values[i]);
            }
        }
        return queryObject.list();
    }


    public List findByNamedParam(final String queryString, final String[] paramNames, final Object[] values)
            throws DataAccessException {
        if (paramNames.length != values.length) {
            throw new IllegalArgumentException("Length of paramNames array must match length of values array");
        }
        Query queryObject = getSession().createQuery(queryString);
        prepareQuery(queryObject);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                HibernateUtils.applyParameter(queryObject, paramNames[i], values[i]);
            }
        }
        return queryObject.list();
    }

    public List findByValueBean(final String queryString, final Object valueBean) throws DataAccessException {
        Query queryObject = getSession().createQuery(queryString);
        prepareQuery(queryObject);
        queryObject.setProperties(valueBean);
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
    public SQLQuery createSQLQuery(String sql, Object... values) {
        Assert.hasText(sql);
        return createSQLQuery(sql, null, values);
    }

    /**
     * 创建Native查询对象.
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常
     */
    public SQLQuery createSQLQuery(String sql, Map<String, Type> scalarTypes, Object... values) {
        Assert.hasText(sql);
        return createSQLQuery(getSession(), sql, scalarTypes, values);
    }

    /**
     * 创建Native查询对象.
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常
     */
    private SQLQuery createSQLQuery(Session session, String sql, Map<String, Type> scalarTypes, Object... values) {
        Assert.notNull(session);
        Assert.hasText(sql);
        SQLQuery query = session.createSQLQuery(sql);
        HibernateUtils.applyScalars(query, scalarTypes);
        HibernateUtils.applyParameters(query, values);
        return query;
    }

    /**
     * 创建Native查询对象.
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常
     */
    public SQLQuery createSQLQuery(String sql, Class transformerClazz) {
        Assert.hasText(sql);
        return createSQLQuery(sql, transformerClazz, null);
    }

    /**
     * 创建Native查询对象.
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常
     */
    public SQLQuery createSQLQuery(String sql, Map<String, Object> parameters) {
        Assert.hasText(sql);
        return createSQLQuery(sql, null, parameters);
    }

    /**
     * 创建Native查询对象.
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常
     */
    public SQLQuery createSQLQuery(String sql, Class transformerClazz, Map<String, Object> parameters) {
        Assert.hasText(sql);
        return createSQLQuery(sql, parameters, transformerClazz, null);
    }

    /**
     * 创建Native查询对象.
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常
     */
    public SQLQuery createSQLQuery(String sql, Map<String, Object> parameters, Class transformerClazz, Map<String, Type> scalarTypes) {
        Assert.hasText(sql);
        return createSQLQuery(getSession(), sql, parameters, transformerClazz, scalarTypes);
    }

    /**
     * 创建Native查询对象.
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常
     */
    private SQLQuery createSQLQuery(Session session, String sql, Map<String, Object> parameters, Class transformerClazz, Map<String, Type> scalarTypes) {
        Assert.notNull(session);
        Assert.hasText(sql);
        SQLQuery query = session.createSQLQuery(sql);
        HibernateUtils.applyParameters(query, parameters);
        HibernateUtils.applyScalars(query, scalarTypes);
        if (transformerClazz != null) {
            query.setResultTransformer(Transformers.aliasToBean(transformerClazz));
        }
        return query;
    }
    //-------------------------------------------------------------------------
    // Convenience finder methods for named queries
    //-------------------------------------------------------------------------

    /**
     * 获取命名查询对象.
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常
     */
    public Query getNamedQuery(String queryName, Object... values) {
        Assert.hasText(queryName);
        Query query = getSession().getNamedQuery(queryName);
        HibernateUtils.applyParameters(query, values);
        return query;
    }

    /**
     * 获取命名查询对象.
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常
     */
    public Query getNamedQuery(String queryName, Map<String, Object> parameters) {
        Assert.hasText(queryName);
        Query query = getSession().getNamedQuery(queryName);
        HibernateUtils.applyParameters(query, parameters);
        return query;
    }

    /**
     * 获取命名查询对象.
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常
     */
    public Query getNamedQuery(String queryName, String key, Object parameter) {
        Assert.hasText(queryName);
        Assert.hasText(key);
        Assert.notNull(parameter);
        Map<String, Object> parameters = new HashMap<String, Object>();
        return getNamedQuery(queryName, parameters);
    }

    public List findByNamedQuery(String queryName) throws DataAccessException {
        return findByNamedQuery(queryName, (Object[]) null);
    }

    public List findByNamedQuery(String queryName, Object value) throws DataAccessException {
        return findByNamedQuery(queryName, new Object[]{value});
    }

    public List findByNamedQuery(final String queryName, final Object... values) throws DataAccessException {
        Query queryObject = getSession().getNamedQuery(queryName);
        prepareQuery(queryObject);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                queryObject.setParameter(i, values[i]);
            }
        }
        return queryObject.list();
    }

    public List findByNamedQueryAndNamedParam(String queryName, String paramName, Object value)
            throws DataAccessException {
        return findByNamedQueryAndNamedParam(queryName, new String[]{paramName}, new Object[]{value});
    }

    public List findByNamedQueryAndNamedParam(
            final String queryName, final String[] paramNames, final Object[] values)
            throws DataAccessException {
        if (paramNames != null && values != null && paramNames.length != values.length) {
            throw new IllegalArgumentException("Length of paramNames array must match length of values array");
        }
        Query queryObject = getSession().getNamedQuery(queryName);
        prepareQuery(queryObject);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                HibernateUtils.applyParameter(queryObject, paramNames[i], values[i]);
            }
        }
        return queryObject.list();
    }

    public List findByNamedQueryAndValueBean(final String queryName, final Object valueBean) throws DataAccessException {
        Query queryObject = getSession().getNamedQuery(queryName);
        prepareQuery(queryObject);
        queryObject.setProperties(valueBean);
        return queryObject.list();

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

    public List findByExample(Object exampleEntity) throws DataAccessException {
        return findByExample(null, exampleEntity, -1, -1);
    }

    public List findByExample(String entityName, Object exampleEntity) throws DataAccessException {
        return findByExample(entityName, exampleEntity, -1, -1);
    }

    public List findByExample(Object exampleEntity, int firstResult, int maxResults) throws DataAccessException {
        return findByExample(null, exampleEntity, firstResult, maxResults);
    }

    public List findByExample(final String entityName, final Object exampleEntity, final int firstResult, final int maxResults)
            throws DataAccessException {

        Assert.notNull(exampleEntity, "Example entity must not be null");
        Criteria executableCriteria = (entityName != null ?
                getSession().createCriteria(entityName) : getSession().createCriteria(exampleEntity.getClass()));
        executableCriteria.add(Example.create(exampleEntity));
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
    // Convenience query methods for create and iteration and bulk updates/deletes
    //-------------------------------------------------------------------------

    public Iterator iterate(String queryString) throws DataAccessException {
        return iterate(queryString, (Object[]) null);
    }

    public Iterator iterate(String queryString, Object value) throws DataAccessException {
        return iterate(queryString, new Object[]{value});
    }

    public Iterator iterate(final String queryString, final Object... values) throws DataAccessException {
        Query queryObject = getSession().createQuery(queryString);
        prepareQuery(queryObject);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                queryObject.setParameter(i, values[i]);
            }
        }
        return queryObject.iterate();

    }

    public void closeIterator(Iterator it) throws DataAccessException {
        try {
            Hibernate.close(it);
        } catch (HibernateException ex) {
            throw SessionFactoryUtils.convertHibernateAccessException(ex);
        }
    }

    /**
     * 批量保存对象.
     *
     * @param entities 对象列表
     */
    public void bulkSave(final Collection entities) {
        checkWriteOperationAllowed(getSession());
        if (!CollectionUtils.isEmpty(entities)) {
            for (Object entity : entities) {
                getSession().save(entity);
            }
        }
    }


    public int bulkUpdate(String queryString) throws DataAccessException {
        return bulkUpdate(queryString, (Object[]) null);
    }

    public int bulkUpdate(String queryString, Object value) throws DataAccessException {
        return bulkUpdate(queryString, new Object[]{value});
    }

    public int bulkUpdate(final String queryString, final Object... values) throws DataAccessException {
        Query queryObject = getSession().createQuery(queryString);
        prepareQuery(queryObject);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                queryObject.setParameter(i, values[i]);
            }
        }
        return queryObject.executeUpdate();
    }


    //-------------------------------------------------------------------------
    // Helper methods used by the operations above
    //-------------------------------------------------------------------------

    /**
     * Check whether write operations are allowed on the given Session.
     * <p>Default implementation throws an InvalidDataAccessApiUsageException in
     * case of {@code FlushMode.MANUAL}. Can be overridden in subclasses.
     *
     * @param session current Hibernate Session
     * @throws org.springframework.dao.InvalidDataAccessApiUsageException if write operations are not allowed
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
     * Prepare the given Query object, applying cache settings and/or
     * a transaction timeout.
     *
     * @param queryObject the Query object to prepare
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
     * Prepare the given Criteria object, applying cache settings and/or
     * a transaction timeout.
     *
     * @param criteria the Criteria object to prepare
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
