package gaea.access.hibernate;

import gaea.access.hibernate.support.HibernateDaoSupport;
import gaea.access.hibernate.support.HibernateQueryParser;
import gaea.access.hibernate.support.HibernateUtils;
import gaea.foundation.core.repository.query.QuerySupport;
import gaea.foundation.core.repository.support.Pager;
import gaea.foundation.core.utils.BeanUtils;
import gaea.foundation.core.utils.ClassUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hibernate Dao的泛型基类.
 * <p/>
 * 继承于<code>HibernateDaoSupport</code>,提供若干便捷查询方法，并对返回值作了泛型类型转换.
 *
 * @author wuhy
 * @see gaea.access.hibernate.support.HibernateDaoSupport
 */
public class HibernateGenericDao extends HibernateDaoSupport {
    /**
     * Logger对象
     */
    private static Log logger = LogFactory.getLog(HibernateGenericDao.class);

    protected static final Map<String, Object> EMP_PARAMETERS = new HashMap<String, Object>();
    protected static final Map<String, Type> EMP_TYPES = new HashMap<String, Type>();

    /**
     * Query转换器
     */
    protected HibernateQueryParser parser;

    public HibernateGenericDao() {
        parser = new HibernateQueryParser();
    }
    //-------------------------------------------------------------------------
    // 功能
    //-------------------------------------------------------------------------

    /**
     * 写锁(仅在事务环境上有效)
     * <p>本方法是事务安全的.</p>
     *
     * @param entity 对象
     */
    public void lock(Object entity) {
        getHibernateTemplate().lock(entity, LockOptions.UPGRADE);
    }

    /**
     * 读锁(仅在事务环境上有效),数据直接从数据库读，不使用缓存数据
     * <p>本方法是事务安全的.</p>
     *
     * @param entity 对象
     */
    public void lockForRead(Object entity) {
        getHibernateTemplate().lock(entity, LockOptions.READ);
    }

    /**
     * 消除与 Hibernate Session 的关联
     *
     * @param entity
     */
    public void evit(Object entity) {
        Assert.notNull(entity);
        getHibernateTemplate().evict(entity);
    }

    /**
     * 初始化Hibernate代理对象
     * <p>本方法是事务安全的.</p>
     *
     * @param proxy
     */
    public void initializeProxy(Object proxy) {
        getHibernateTemplate().initialize(proxy);
    }

    /**
     * 同步对象（获取最新数据库状态）.
     * <p>本方法是事务安全的.</p>
     *
     * @param entity 对象
     */
    public void refresh(Object entity) {
        getHibernateTemplate().refresh(entity);
    }

    /**
     * 刷新Hibernate会话.
     * <p>本方法是事务安全的.</p>
     */
    public void flush() {
        getHibernateTemplate().flush();
    }

    /**
     * 刷新Hibernate会话状态.
     * <p>本方法是事务安全的.</p>
     */
    public void clear() {
        getHibernateTemplate().clear();
    }

    /**
     * 获取无状态会话.
     * 本方法是非事务安全的，需要程序显示关闭，不推荐使用.
     */
    public StatelessSession openStatelessSession() {
        return this.getSessionFactory().openStatelessSession();
    }

    /**
     * 取得当前的Session
     *
     * @return
     */
    public Session getDoSession() {
        return getSession();
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
     * @param entityClass
     * @param id
     */
    public <T> T load(Class<T> entityClass, Serializable id) {
        return getHibernateTemplate().load(entityClass, id);
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
    public <T> T get(Class<T> entityClass, Serializable id) {
        return getHibernateTemplate().get(entityClass, id);
    }

    /**
     * 根据ID获取对象.
     *
     * @param entityName 实体名称
     * @param id         ID
     * @return
     */
    public Object get(String entityName, Serializable id) {
        return this.getHibernateTemplate().get(entityName, id);
    }

    /**
     * 根据查询对象查询唯一对象.
     *
     * @param entityName
     * @param querySupport 查询对象
     * @return 符合条件的唯一对象 or null if not found.
     */
    public Object get(String entityName, QuerySupport querySupport) {
        return this.findUniqueBy(loadEntityClass(entityName), querySupport);
    }

    /**
     * 根据属性名和属性值查询唯一对象.
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常.
     *
     * @return 符合条件的唯一对象 or null if not found.
     */
    public <T> T findUniqueBy(Class<T> entityClass, String propertyName, Object value) {
        Assert.hasText(propertyName);
        return (T) this.getHibernateTemplate().createCriteria(entityClass, Restrictions.eq(propertyName, value)).uniqueResult();
    }

    /**
     * 根据查询对象查询唯一对象.
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常.
     *
     * @param entityClass
     * @param querySupport 查询对象
     * @return 符合条件的唯一对象 or null if not found.
     */
    public <T> T findUniqueBy(Class<T> entityClass, QuerySupport querySupport) {
        Assert.notNull(entityClass);
        Criteria criteria = getHibernateTemplate().createCriteria(entityClass);
        if (querySupport != null) {
            parser.parseCriterions(criteria, querySupport.getCriterions());
            parser.parseOrders(criteria, querySupport.getOrders());
        }
        return (T) criteria.uniqueResult();
    }

    /**
     * 获取全部对象.
     * <p>本方法是事务安全的.</p>
     *
     * @param entityClass
     * @return 所有对象列表
     */
    public <T> List<T> findAll(Class<T> entityClass) {
        Assert.notNull(entityClass);
        return getHibernateTemplate().loadAll(entityClass);
    }

    /**
     * 获取全部对象.
     * <p>本方法是事务安全的.</p>
     *
     * @param entityName 实体名称
     * @return 所有对象列表
     */
    public List findAll(String entityName) {
        return this.findAll(loadEntityClass(entityName));
    }


    /**
     * 根据查询对象获取全部对象，查询对象的分页信息将不会起作用。
     * <p>本方法是事务安全的.</p>
     *
     * @param entityClass
     * @param querySupport 查询对象
     * @return 符合条件的对象列表
     */
    public <T> List<T> findAll(Class<T> entityClass, QuerySupport querySupport) {
        Assert.notNull(entityClass);
        if (querySupport == null) {
            return findAll(entityClass);
        }
        Criteria criteria = getHibernateTemplate().createCriteria(entityClass);
        parser.parseCriterions(criteria, querySupport.getCriterions());
        parser.parseOrders(criteria, querySupport.getOrders());
        return criteria.list();
    }

    /**
     * 根据ID列表取得数据列表
     *
     * @param entityClass
     * @param ids
     * @param <T>
     * @return
     */
    public <T> List<T> find(Class<T> entityClass, Serializable[] ids) {
        String idName = getIdName(entityClass);
        return find(entityClass, QuerySupport.createQuery()
                .add(gaea.foundation.core.repository.query.criterion.Restrictions.in(idName, ids)));
    }

    /**
     * 根据查询对象查询数据列表
     *
     * @param entityClass
     * @param querySupport 查询对象
     * @return 符合条件的对象列表
     */
    public <T> List<T> find(Class<T> entityClass, QuerySupport querySupport) {
        Assert.notNull(entityClass);
        if (querySupport == null) {
            return findAll(entityClass);
        }
        Criteria criteria = getHibernateTemplate().createCriteria(entityClass);
        parser.parse(criteria, querySupport);
        return criteria.list();
    }

    /**
     * 根据查询对象查询数据列表.
     *
     * @param entityName
     * @param querySupport 查询对象
     * @return 符合条件的对象列表
     */
    public List find(String entityName, QuerySupport querySupport) {
        return this.find(loadEntityClass(entityName), querySupport);
    }

    /**
     * 根据hql查询,直接使用HibernateTemplate的find函数.
     * 本方法是事务安全的.
     *
     * @param values 可变参数,见{@link gaea.access.hibernate.support.HibernateTemplate#find(String, Object...)}
     */
    public List findBy(String hql, Object... values) {
        Assert.hasText(hql);
        return getHibernateTemplate().find(hql, values);
    }

    /**
     * 根据hql查询,直接使用HibernateTemplate的find函数.
     * 本方法是事务安全的.
     *
     * @param values 可变参数,见{@link gaea.access.hibernate.support.HibernateTemplate#find(String, Object...)}
     */
    public List findBy(String hql, Class transformerClazz, Object... values) {
        Assert.hasText(hql);
        return getHibernateTemplate().find(hql, transformerClazz, values);
    }

    /**
     * 根据hql查询,直接使用HibernateTemplate的find函数.
     * 本方法是事务安全的.
     */
    public List findBy(String hql, Map<String, Object> params) {
        return getHibernateTemplate().findByNamedParam(hql, params.keySet().toArray(new String[]{}), params.values().toArray());
    }

    /**
     * 根据hql查询,直接使用HibernateTemplate的find函数.
     * 本方法是事务安全的.
     */
    public List findBy(String hql, Class transformerClazz, Map<String, Object> params) {
        Assert.hasText(hql);
        return getHibernateTemplate().findByNamedParam(hql, params.keySet().toArray(new String[]{}), params.values().toArray(), transformerClazz);
    }

    /**
     * 通过Native SQL查询.
     * 本方法是事务安全的.
     */
    public List findBySQL(String sql, Object... values) {
        return findBySQL(sql, null, values);
    }

    /**
     * 通过Native SQL查询.
     * 本方法是事务安全的.
     */
    public List findBySQL(final String sql, final Map<String, Type> scalarTypes, final Object... values) {
        Query queryObject = this.getHibernateTemplate().createSQLQuery(sql, scalarTypes, values);
        return queryObject.list();
    }

    /**
     * 通过Native SQL查询.
     * 本方法是事务安全的.
     */
    public List findBySQL(final String sql, final Map<String, Type> scalarTypes, final Class transformerClazz, final Object... values) {
        Query queryObject = this.getHibernateTemplate().createSQLQuery(sql, scalarTypes, values);
        // 如果没有class，则直接使用Map
        if (transformerClazz == null) {
            queryObject.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        }
        return queryObject.list();
    }

    /**
     * 通过Native SQL查询.
     * 本方法是事务安全的.
     */
    public List findBySQL(String sql, Map<String, Object> parameters) {
        return findBySQL(sql, parameters, null);
    }

    /**
     * 通过Native SQL查询.
     * 本方法是事务安全的.
     */
    public List findBySQL(String sql, Map<String, Object> parameters, Map<String, Type> scalarTypes) {
        return findBySQL(sql, parameters, null, scalarTypes);
    }

    /**
     * 通过Native SQL查询.
     * 本方法是事务安全的.
     */
    public List findBySQL(final String sql, final Map<String, Object> parameters, final Class transformerClazz, final Map<String, Type> scalarTypes) {
        Query queryObject = this.getHibernateTemplate().createSQLQuery(sql, parameters, transformerClazz, scalarTypes);
        // 如果没有class，则直接使用Map
        if (transformerClazz == null) {
            queryObject.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        }
        return queryObject.list();
    }

    /**
     * 根据查询对象分页数据，会返回totalCount
     *
     * @param entityName   实体名称
     * @param querySupport 查询对象
     * @return 含总记录数和当前页数据的Page对象.
     */
    public Pager pagedQuery(String entityName, QuerySupport querySupport) {
        return pagedQuery(loadEntityClass(entityName), querySupport);
    }

    /**
     * 根据查询对象分页数据，会返回totalCount
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常.
     *
     * @param entityClass  实体
     * @param querySupport 查询对象
     * @return 含总记录数和当前页数据的Page对象.
     */
    public <T> Pager pagedQuery(Class<T> entityClass, QuerySupport querySupport) {
        Assert.notNull(querySupport);
        Pager pager = querySupport.getPager() == null ? Pager.EMPTY_PAGER : querySupport.getPager();
        long totalCount = count(entityClass, querySupport);
        // 返回分页对象
        if (totalCount < 1) {
            return new Pager();
        }
        List dataList = this.find(entityClass, querySupport);
        pager.setData(dataList);
        pager.setTotalRecordCount(totalCount);
        return pager;
    }

    /**
     * 分页查询函数，使用hql.
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常.
     *
     * @param pageNo 页号,从1开始.
     */
    public Pager pagedQuery(String hql, int pageNo, int pageSize, long totalCount, Object... values) {
        Assert.hasText(hql);
        Assert.isTrue(pageNo >= 0, "pageNo should start from 0");
        if (totalCount < 0) {
            // Count查询
            String countQueryString = " select count(*) " + removeSelect(removeOrders(hql));
            List countlist = this.getHibernateTemplate().find(countQueryString, values);
            totalCount = (Long) countlist.get(0);
        }
        if (totalCount < 1) {
            return new Pager();
        }
        // 实际查询返回分页对象
        int startIndex = Pager.getStartOfPage(pageNo, pageSize);
        Query query = this.getHibernateTemplate().createQuery(hql, values);
        List list = query.setFirstResult(startIndex).setMaxResults(pageSize).list();
        return new Pager(pageNo, pageSize, totalCount, list);
    }
    /**
     * 分页查询函数，使用hql.
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常.
     *
     * @param pageNo 页号,从1开始. .
     * @param params 提供map类型的 named绑定
     */
    public Pager pagedQuery(String hql, int pageNo, int pageSize, long totalCount, Map<String, Object>  params) {
        Assert.hasText(hql);
        Assert.isTrue(pageNo >= 0, "pageNo should start from 0");
        if (totalCount < 0) {
            // Count查询
            String countQueryString = " select count(*) " + removeSelect(removeOrders(hql));
            List countlist =  getHibernateTemplate().findByNamedParam(countQueryString, params.keySet().toArray(new String[]{}), params.values().toArray());
            totalCount = (Long) countlist.get(0);
        }
        if (totalCount < 1) {
            return new Pager();
        }
        // 实际查询返回分页对象
        int startIndex = Pager.getStartOfPage(pageNo, pageSize);
        Query query = this.getHibernateTemplate().createQuery(hql,params);
        List list = query.setFirstResult(startIndex).setMaxResults(pageSize).list();
        return new Pager(pageNo, pageSize, totalCount, list);
    }

    /**
     * 取得某个表或集合中满足条件的条件
     *
     * @param entityClass  实体类型
     * @param querySupport 查询对象
     * @return
     */
    public <T> long count(Class<T> entityClass, QuerySupport querySupport) {
        Assert.notNull(querySupport);
        long totalCount = querySupport.getPager() == null ? -1 : querySupport.getPager().getTotalRecordCount();
        if (totalCount < 0) {
            Criteria criteria = this.getHibernateTemplate().createCriteria(entityClass);
            parser.parseCriterions(criteria, querySupport.getCriterions());
            totalCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        }
        return totalCount;
    }

    /**
     * 取得某个表或集合中满足条件的条件
     *
     * @param entityName   实体名称
     * @param querySupport 查询对象
     * @return
     */
    public long count(String entityName, QuerySupport querySupport) {
        return count(loadEntityClass(entityName), querySupport);
    }
    //-------------------------------------------------------------------------
    // 数据修改-添加、修改、删除
    //-------------------------------------------------------------------------

    /**
     * 保存对象.
     * <p/>
     * 本方法是事务安全的.
     *
     * @param entity 实体
     */
    public <T> void insert(T entity) {
        getHibernateTemplate().save(entity);
    }

    /**
     * 保存对象.
     * <p/>
     * 本方法是事务安全的.
     *
     * @param entityName 实体名称
     * @param insertData 数据
     */
    public void insert(String entityName, Map<String, Object> insertData) {
        Object data = populateObject(entityName, insertData);
        this.insert(data);
    }

    /**
     * 批量保存对象.
     *
     * @param entityName 实体名称
     * @param batchData  Map列表
     */
    public void insert(String entityName, Collection<Map<String, Object>> batchData) {
        if (!CollectionUtils.isEmpty(batchData)) {
            for (Map<String, Object> data : batchData) {
                this.insert(entityName, data);
            }
        }
    }

    /**
     * 批量保存对象.
     *
     * @param entities 对象列表
     */
    public void insert(final Collection entities) {
        this.getHibernateTemplate().bulkSave(entities);
    }

    /**
     * 保存或者更新对象，如果对象已经存在则更新，该方法不适合手动生成ID的实体
     * <p/>
     * 本方法是事务安全的.
     *
     * @param entity 实体
     */
    public void save(Object entity) {
        getHibernateTemplate().saveOrUpdate(entity);
    }

    /**
     * 保存或者更新对象，如果对象已经存在则更新，该方法不适合手动生成ID的实体
     * <p/>
     * 本方法是事务安全的.
     *
     * @param entityName 实体名称
     * @param saveData   保存数据
     */
    public void save(String entityName, Map<String, Object> saveData) {
        Object data = populateObject(entityName, saveData);
        this.save(data);
    }

    /**
     * 更新对象
     * <p/>
     * 本方法是事务安全的.
     *
     * @param entity 实体
     */
    public <T> void update(T entity) {
        getHibernateTemplate().update(entity);
    }

    /**
     * 更新对象
     * <p/>
     * 本方法是事务安全的.
     *
     * @param entityName 实体名称
     */
    public void update(String entityName, Serializable id, Map<String, Object> updateData) {
        Object data = populateObject(entityName, updateData);
        try {
            PropertyUtils.setProperty(data, getIdName(data.getClass()), id);
        } catch (Exception ex) {
            logger.error(ex);
            ReflectionUtils.handleReflectionException(ex);
        }
        this.update(data);
    }

    /**
     * 批量更新.
     * 本方法是事务安全的.
     *
     * @param queryString 更新字符串
     * @return 更新条数
     */
    public int update(String queryString) {
        return update(queryString, (Object[]) null);
    }

    /**
     * 批量更新.
     * 本方法是事务安全的.
     *
     * @param queryString 更新字符串
     * @return 更新条数
     */
    public int update(String queryString, Object value) {
        return update(queryString, new Object[]{value});
    }

    /**
     * 批量更新.
     * 本方法是事务安全的.
     *
     * @param queryString 更新字符串
     * @param values      参数
     * @return 更新条数
     */
    public int update(final String queryString, final Object[] values) {
        return getHibernateTemplate().bulkUpdate(queryString, values);
    }

    /**
     * 批量更新.
     * 本方法是事务安全的.
     *
     * @param queryString 更新字符串
     * @param parameters  参数
     * @return 更新条数
     */
    public int update(final String queryString, final Map<String, Object> parameters) {
        Query queryObject = this.getHibernateTemplate().createQuery(queryString, parameters);
        return queryObject.executeUpdate();
    }

    /**
     * 批量更新.
     * 本方法是事务安全的.
     *
     * @param queryString 更新字符串
     * @return 更新条数
     */
    public int updateBySQL(String queryString) {
        return updateBySQL(queryString, (Object[]) null);
    }

    /**
     * 批量更新.
     * 本方法是事务安全的.
     *
     * @param queryString 更新字符串
     * @param value       参数
     * @return 更新条数
     */
    public int updateBySQL(String queryString, Object value) {
        return updateBySQL(queryString, new Object[]{value});
    }

    /**
     * 批量更新.
     * 本方法是事务安全的.
     *
     * @param queryString 更新字符串
     * @param values      参数
     * @return 更新条数
     */
    public int updateBySQL(final String queryString, final Object[] values) {
        Query queryObject = this.getHibernateTemplate().createSQLQuery(queryString, values);
        return queryObject.executeUpdate();
    }

    /**
     * 批量更新.
     * 本方法是事务安全的.
     *
     * @param queryString 更新字符串
     * @param parameters  参数
     * @return 更新条数
     */
    public int updateBySQL(final String queryString, final Map<String, Object> parameters) {
        Query queryObject = this.getHibernateTemplate().createSQLQuery(queryString, parameters);
        return queryObject.executeUpdate();
    }

    /**
     * 删除对象.
     * 本方法是事务安全的.
     *
     * @param entity 对象
     */
    public void delete(Object entity) {
        getHibernateTemplate().delete(entity);
    }

    /**
     * 删除对象.
     * 本方法是事务安全的.
     *
     * @param entityName 实体名称
     */
    public void delete(String entityName, Serializable id) {
        this.deleteById(loadEntityClass(entityName), id);
    }

    /**
     * 根据ID删除对象.
     * 本方法是事务安全的.
     */
    public <T> void deleteById(Class<T> entityClass, Serializable id) {
        T entity = get(entityClass, id);
        if (entity != null) {
            delete(entity);
        }
    }

    /**
     * 根据ID数组删除对象.
     * <p/>
     * 该方法是对象逐个删除，会产生多条SQL语句，如果删除量比较大时会有性能问题
     *
     * @param entityClass 对象列表
     * @param ids         实体id数组
     */
    public <T> void deleteByIds(Class<T> entityClass, final Serializable[] ids) {
        for (Serializable id : ids) {
            deleteById(entityClass, id);
        }
    }

    /**
     * 批量删除对象.
     * 本方法是事务安全的.
     *
     * @param entityClass 对象列表
     * @param ids        实体id数组
     */
    public <T> void bulkDelete(Class<T> entityClass, final Object[] ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(entityClass.getSimpleName()).append(" where ")
                .append(getIdName(entityClass)).append(" in (:ids)");
        getDoSession().createQuery(sql.toString()).setParameterList("ids", ids).executeUpdate();
    }

    //-------------------------------------------------------------------------
    // 业务功能
    //-------------------------------------------------------------------------

    /**
     * 判断对象某些属性的值在数据库中是否唯一.
     * 本方法是非事务安全的.
     * 若当前无事务则抛异常.
     *
     * @param uniquePropertyNames 在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
     */
    public <T> boolean isUnique(Class<T> entityClass, Object entity, String uniquePropertyNames) {
        Assert.hasText(uniquePropertyNames);
        Criteria criteria = this.getHibernateTemplate().createCriteria(entityClass).setProjection(Projections.rowCount());
        String[] nameList = uniquePropertyNames.split(",");
        try {
            // 循环加入唯一列
            for (String name : nameList) {
                criteria.add(Restrictions.eq(name, PropertyUtils.getProperty(entity, name)));
            }
            //排除entity自身.
            String idName = getIdName(entityClass);
            // 取得entity的主键值
            Serializable id = getId(entityClass, entity);
            // 如果id!=null,说明对象已存在,加入排除自身的判断
            if (id != null) {
                criteria.add(Restrictions.not(Restrictions.eq(idName, id)));
            }
        } catch (Exception ex) {
            logger.error(ex);
            ReflectionUtils.handleReflectionException(ex);
        }
        return (Long) criteria.uniqueResult() == 0;
    }


    /**
     * 取得对象的主键值,辅助函数.
     * 本方法为非事务相关方法.
     *
     * @param entityClass 实体类型
     * @param entity      实体
     */
    public <T> Serializable getId(Class<T> entityClass, Object entity) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        Assert.notNull(entity);
        Assert.notNull(entityClass);
        return (Serializable) PropertyUtils.getProperty(entity, getIdName(entityClass));
    }

    /**
     * 取得对象的主键名,辅助函数.
     * 本方法为非事务相关方法.
     *
     * @param entityClass 实体类型
     */
    public <T> String getIdName(Class<T> entityClass) {
        Assert.notNull(entityClass);
        ClassMetadata meta = getSessionFactory().getClassMetadata(entityClass);
        Assert.notNull(meta, "Class " + entityClass + " not define in hibernate4 session factory.");
        String idName = meta.getIdentifierPropertyName();
        Assert.hasText(idName, entityClass.getSimpleName() + " has no identifier property define.");
        return idName;
    }

    //-------------------------------------------------------------------------
    // 内部方法
    //-------------------------------------------------------------------------

    /**
     * 把name转换成class
     *
     * @param entityName
     * @return
     */
    protected Class loadEntityClass(String entityName) {
        Class entityClass = null;
        try {
            entityClass = ClassUtils.loadClass(entityName);
        } catch (Exception ex) {
            logger.error(ex);
            ReflectionUtils.handleReflectionException(ex);
        }
        return entityClass;
    }

    /**
     * 把Map的数据填充到Bean中
     *
     * @param entityName
     * @param properties
     * @return
     */
    protected Object populateObject(String entityName, Map<String, ? extends Object> properties) {
        Object result = null;
        try {
            result = ClassUtils.createNewInstance(entityName);
            BeanUtils.populate(result, properties);
        } catch (Exception ex) {
            logger.error(ex);
            ReflectionUtils.handleReflectionException(ex);
        }
        return result;
    }

    /**
     * 去除hql的select 子句，未考虑union的情况,用于pagedQuery.
     */
    public static String removeSelect(String hql) {
        return HibernateUtils.removeSelect(hql);
    }

    /**
     * 去除hql的orderby 子句，用于pagedQuery.
     */
    public static String removeOrders(String hql) {
        return HibernateUtils.removeOrders(hql);
    }
}