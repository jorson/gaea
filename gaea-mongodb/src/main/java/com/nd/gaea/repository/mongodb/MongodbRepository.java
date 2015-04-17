package com.nd.gaea.repository.mongodb;

import com.nd.gaea.core.exception.BusinessExceptionFactory;
import com.nd.gaea.core.repository.Repository;
import com.nd.gaea.core.repository.annotation.Id;
import com.nd.gaea.core.repository.query.QuerySupport;
import com.nd.gaea.core.repository.support.Pager;
import com.nd.gaea.repository.mongodb.support.MongodbDaoSupport;
import com.nd.gaea.repository.mongodb.support.MongodbQueryParser;
import com.nd.gaea.repository.mongodb.utils.MongodbUtils;
import com.nd.gaea.utils.BeanUtils;
import com.nd.gaea.utils.GenericsUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nd.gaea.core.repository.query.criterion.Restrictions.eq;
import static com.nd.gaea.core.repository.query.criterion.Restrictions.in;

/**
 * 负责为单个Entity对象提供CRUD操作的Mongodb Repository基类.
 * <p/>
 * 子类只要在类定义时指定所管理Entity的Class, 即拥有对单个Entity对象的CRUD操作.
 * <p/>
 * <pre>
 * public class UserRepository extends MongodbRepository&lt;User&gt; {
 * }
 * </pre>
 *
 * @author bifeng.liu
 */
public class MongodbRepository<T> extends MongodbDaoSupport implements Repository<T> {

    private static final Log LOGGER = LogFactory.getLog(MongodbRepository.class);
    /**
     *
     */
    protected static final String ENTITY_IDNAME = "id";
    /**
     * 实体的类名Key
     */
    protected static final String ENTITY_CLASSNAME = "_class";
    /**
     * 空分页信息
     */
    public final Pager<T> EMPTY_PAGER = new Pager(new ArrayList<T>(), 0);
    /**
     * 默认的ID名称
     */
    protected static final String DEFAULT_IDNAME = "_id";
    /**
     * Repository所管理的Entity类型.
     */
    protected Class<T> entityClass;
    /**
     * Query转换器
     */
    protected MongodbQueryParser parser;
    /**
     * Repository所管理Entity类型的ID名称
     */
    protected String idName;

    /**
     * 在构造函数中将泛型T.class赋给entityClass.
     */
    public MongodbRepository() {
        entityClass = GenericsUtils.getSuperClassGenricType(getClass());
        parser = new MongodbQueryParser();
    }


    /**
     * 取得entityClass.JDK1.4不支持泛型的子类可以抛开Class<T> entityClass,重载此函数达到相同效果。
     *
     * @return
     */
    protected Class<T> getEntityClass() {
        return entityClass;
    }

    //-------------------------------------------------------------------------
    // 查询
    //-------------------------------------------------------------------------

    /**
     * 根据ID从集合中取得单条数据
     *
     * @param id
     * @return
     */
    public T get(Serializable id) {
        QuerySupport querySupport = QuerySupport.createQuery().addCriterion(eq(getIdName(), id));
        return findOne(querySupport);
    }


    /**
     * 根据ID列表取得记录列表
     *
     * @param ids
     * @return
     */
    public List<T> getList(Serializable[] ids) {
        QuerySupport querySupport = QuerySupport.createQuery().addCriterion(in(getIdName(), ids));
        return this.getMongoTemplate().find(parseQuery(querySupport), getEntityClass(), getCollectionName());
    }

    /**
     * 根据查询对象取得表或者集合中满足条件的所有数据
     *
     * @param querySupport 查询对象
     * @return
     */
    public T findOne(QuerySupport querySupport) {
        Assert.notNull(querySupport);
        return this.getMongoTemplate().findOne(parseQuery(querySupport), getEntityClass(), getCollectionName());
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    public List<T> findAll() {
        return this.getMongoTemplate().findAll(entityClass, getCollectionName());
    }


    /**
     * 根据查询满足条件的所有数据
     *
     * @param querySupport 查询对象
     * @return
     */
    public List<T> find(QuerySupport querySupport) {
        if (querySupport == null) {
            return findAll();
        }
        return this.getMongoTemplate().find(parseQuery(querySupport), getEntityClass(), getCollectionName());
    }

    /**
     * 取得满足条件的条数
     *
     * @param querySupport 查询对象
     * @return
     */
    public int count(QuerySupport querySupport) {
        Long count = this.getMongoTemplate().count(parseQuery(querySupport), getEntityClass());
        return count.intValue();
    }

    /**
     * 根据查询对象分页数据，会返回totalCount
     *
     * @param querySupport 查询对象
     * @return 含总记录数和当前页数据的Page对象.
     */
    public Pager findPager(QuerySupport querySupport) {
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
     * 插入对象
     *
     * @param entity 记录数据
     */
    public void create(T entity) {
        this.getMongoTemplate().insert(entity);
    }

    /**
     * 更新单条数据
     *
     * @param querySupport 查询对象
     * @param updateData   更新数据
     * @return
     */
    public void update(QuerySupport querySupport, Map<String, Object> updateData) throws IllegalAccessException {
        Assert.notNull(querySupport);
        Assert.notNull(updateData);
        T ret = this.findOne(querySupport);
        Map<String, Object> tempData = BeanUtils.toMap(ret);
        DBObject updateObj = new BasicDBObject();
        if (tempData != null) {
            tempData.remove(getIdName());
            tempData.remove(DEFAULT_IDNAME);
            tempData.remove(ENTITY_IDNAME);
            tempData.remove(ENTITY_CLASSNAME);
            updateObj.putAll(tempData);
        }
        updateObj.putAll(updateData);
        Update update = MongodbUtils.map2Update(updateObj.toMap());
        this.getMongoTemplate().updateFirst(parseQuery(querySupport), update, getEntityClass(), getCollectionName());
    }

    /**
     * 修改对象
     *
     * @param entity 要删除的对象
     */
    public void update(T entity) {
        try {
            Map<String, Object> updateData = BeanUtils.toMap(entity);
            updateData.remove(getIdName());
            updateData.remove(DEFAULT_IDNAME);
            updateData.remove(ENTITY_IDNAME);
            updateData.remove(ENTITY_CLASSNAME);
            update(QuerySupport.createQuery().addCriterion(eq(getIdName(), getId(entity))), updateData);
        } catch (IllegalAccessException ex) {
            throw BusinessExceptionFactory.wrapBusinessException(ex);
        } catch (NoSuchMethodException ex) {
            throw BusinessExceptionFactory.wrapBusinessException(ex);
        } catch (InvocationTargetException ex) {
            throw BusinessExceptionFactory.wrapBusinessException(ex);
        }
    }

    /**
     * 删除对象
     *
     * @param
     */
    public void delete(Serializable id) {
        T entity = get(id);
        if (entity != null) {
            this.getMongoTemplate().remove(entity, getCollectionName());
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
        String idName = getIdName();
        if (DEFAULT_IDNAME.equals(idName)) {
            try {
                Field field = BeanUtils.getDeclaredField(entityClass, ENTITY_IDNAME);
                idName = ENTITY_IDNAME;
            } catch (NoSuchFieldException e) {
                //
            }
        }
        return (Serializable) PropertyUtils.getProperty(entity, idName);
    }

    /**
     * 取得Repository所管理Entity类型的ID名称
     *
     * @return
     */
    protected String getIdName() {
        if (idName == null) {
            Field[] fields = BeanUtils.getDeclaredFields(entityClass);
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                // 如果栏位注解了Id，则设置idName
                if (field.isAnnotationPresent(Id.class)) {
                    idName = field.getName();
                    break;
                }
            }
            idName = DEFAULT_IDNAME;
        }
        return idName;
    }

    /**
     * 取得集合名称
     *
     * @return
     */
    protected String getCollectionName() {
        return this.getMongoTemplate().getCollectionName(getEntityClass());
    }

    protected Query parseQuery(QuerySupport querySupport) {
        Query query = new Query();
        if (querySupport != null) {
            parser.parse(query, querySupport);
        }
        return query;
    }

}
