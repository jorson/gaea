package gaea.access.mongodb;

import gaea.foundation.core.bo.EntityObject;
import gaea.foundation.core.exception.BusinessException;
import gaea.foundation.core.exception.BusinessExceptionFactory;
import gaea.foundation.core.repository.EntityRepository;
import gaea.foundation.core.repository.exception.RepositoryException;
import gaea.foundation.core.repository.query.QuerySupport;
import gaea.foundation.core.repository.query.criterion.Restrictions;
import gaea.foundation.core.repository.support.Pager;
import gaea.foundation.core.repository.support.SqlUtils;
import gaea.foundation.core.utils.ArrayUtils;
import gaea.foundation.core.utils.BeanUtils;
import gaea.foundation.core.utils.CollectionUtils;
import gaea.foundation.core.utils.GenericsUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

/**
 * 负责为单个Entity对象提供CRUD操作的Mongodb DAO基类.
 * <p/>
 * 子类只要在类定义时指定所管理Entity的Class, 即拥有对单个Entity对象的CRUD操作.
 * <p/>
 * <pre>
 * public class UserManager extends MongodbEntityDao&lt;User&gt; {
 * }
 * </pre>
 *
 * @author wuhy
 * @see MongodbGenericDao
 */
public class MongodbEntityDao<T extends EntityObject> extends MongodbGenericDao implements EntityRepository<T> {

    protected static final String ENTITY_IDNAME = "id";
    protected static final String ENTITY_CLASSNAME = "_class";

    protected Class<T> entityClass;// DAO所管理的Entity类型.

    /**
     * 在构造函数中将泛型T.class赋给entityClass.
     */
    public MongodbEntityDao() {
        entityClass = GenericsUtils.getSuperClassGenricType(getClass());
    }

    /**
     * 取得entityClass.JDK1.4不支持泛型的子类可以抛开Class<T> entityClass,重载此函数达到相同效果。
     */
    protected Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * 取得集合名称
     *
     * @return
     */
    public String getCollectionName() {
        return this.getMongoTemplate().getCollectionName(entityClass);
    }

    //-------------------------------------------------------------------------
    // 查询
    //-------------------------------------------------------------------------

    /**
     * 根据ID从表或者集合中取得单条数据
     *
     * @param id
     * @return
     */
    public T get(Serializable id) {
        return get(IDNAME, (Object) id);
    }

    /**
     * 取得某个集合的某个栏位与传入值相等的数据，只返回第一条数据
     *
     * @param key   栏位
     * @param value 值
     * @return
     */
    public T get(String key, Object value) {
        Map<String, Object> queryData = new HashMap<String, Object>();
        queryData.put(key, value);
        return get(queryData);
    }

    /**
     * 取得某个集合的栏位与传入值相等的数据，只返回第一条数据
     *
     * @param queryData 栏位与值列表
     * @return
     */
    public T get(Map<String, Object> queryData) {
        Assert.notNull(queryData);
        return get(QuerySupport.createQuery().add(Restrictions.allEq(queryData)));
    }

    /**
     * 根据查询对象取得某个集合中符合条件的数据，只返回第一条数据
     *
     * @param querySupport 查询对象
     * @return
     */
    public T get(QuerySupport querySupport) {
        Assert.notNull(querySupport);
        return this.getMongoTemplate().findOne(parseQuery(querySupport), entityClass, getCollectionName());
    }

    /**
     * 查询集合中所有数据
     *
     * @return
     */
    public List<T> findAll() {
        return this.getMongoTemplate().findAll(entityClass);
    }

    /**
     * 根据对象中的值查询集合中的数据
     *
     * @return
     */
    @Override
    public List<T> find(T queryObject) {
        try {
            return find(BeanUtils.toMap(queryObject));
        } catch (IllegalAccessException ex) {
            throw BusinessExceptionFactory.wrapBusinessException(ex);
        }
    }

    /**
     * 根据ID列表取得记录列表
     *
     * @param ids
     * @return
     */
    public List<T> find(Serializable[] ids) {
        return find(QuerySupport.createQuery().add(Restrictions.in(IDNAME, ids)));
    }

    /**
     * 取得某个集合的某个栏位与传入值相等的所有数据
     *
     * @param key   栏位
     * @param value 值
     * @return
     */
    public List<T> find(String key, Object value) {
        Map<String, Object> queryData = new HashMap<String, Object>();
        queryData.put(key, value);
        return find(queryData);
    }

    /**
     * 取得某个集合的栏位与传入值相等的所有数据
     *
     * @param queryData 栏位与值列表
     * @return
     */
    public List<T> find(Map<String, Object> queryData) {
        Assert.notNull(queryData);
        return find(QuerySupport.createQuery().add(Restrictions.allEq(queryData)));
    }

    /**
     * 根据查询对象取得某个集合中符合条件的数据
     *
     * @param querySupport 查询对象
     * @return
     */
    public List<T> find(QuerySupport querySupport) {
        Assert.notNull(querySupport);
        return this.getMongoTemplate().find(parseQuery(querySupport), entityClass, getCollectionName());
    }

    /**
     * 取得某个表或集合中满足条件的条数
     *
     * @param querySupport 查询对象
     * @return
     */
    public long count(QuerySupport querySupport) {
        return this.count(getCollectionName(), querySupport);
    }

    /**
     * 根据查询对象分页数据，会返回totalCount
     *
     * @param querySupport 查询对象
     * @return 含总记录数和当前页数据的Page对象.
     */
    public Pager pagedQuery(QuerySupport querySupport) {
        return this.pagedQuery(getCollectionName(), querySupport);
    }
    //-------------------------------------------------------------------------
    // 更新
    //-------------------------------------------------------------------------

    /**
     * 添加单条记录数据
     * <p/>
     *
     * @param saveData 记录数据
     * @return
     */
    public void insert(T saveData) {
        List<T> dataList = new ArrayList<T>();
        dataList.add(saveData);
        insert(dataList);
    }

    /**
     * 批量添加数据
     * <p/>
     *
     * @param batchData 批量数据
     * @return
     */
    public void insert(Collection<T> batchData) {
        if (!CollectionUtils.isEmpty(batchData)) {
            this.getMongoTemplate().insert(batchData, this.getCollectionName());
        }
    }


    /**
     * 保存数据
     *
     * @param entity 实体
     * @return
     */
    public void save(T entity) {
        if (entity.getId() == null) { //如果saveData不保存有ID，则直接插入
            insert(entity);
        } else {
            T ret = get(IDNAME, entity.getId());
            if (ret == null) {
                insert(entity);
            } else {
                update(entity);
            }
        }
    }

    /**
     * 更新单条数据
     *
     * @param querySupport 查询对象
     * @param updateData   更新数据
     * @return
     */
    public void update(QuerySupport querySupport, Map<String, Object> updateData) {
        Assert.notNull(querySupport);
        Assert.notNull(updateData);
        Map<String, Object> tempData = this.get(this.getCollectionName(), querySupport);
        DBObject updateObj = new BasicDBObject();
        if (tempData != null) {
            tempData.remove(IDNAME);
            tempData.remove(ENTITY_CLASSNAME);
            updateObj.putAll(tempData);
        }
        updateObj.putAll(updateData);
        Update update = MongodbUtils.map2Update(updateObj.toMap());
        update(querySupport, update);
    }

    /**
     * 更新单条数据
     *
     * @param updateObject       更新数据
     * @return
     */
    @Override
    public void update(T updateObject) {
        try {
            Map<String, Object> updateData = BeanUtils.toMap(updateObject);
            updateData.remove(ENTITY_IDNAME);
            updateData.remove(ENTITY_CLASSNAME);
            update(QuerySupport.createQuery().add(Restrictions.eq(IDNAME, updateObject.getId())), updateData);
        } catch (IllegalAccessException ex) {
            throw BusinessExceptionFactory.wrapBusinessException(ex);
        }
    }

    /**
     * 更新单条数据
     *
     * @param querySupport 查询对象
     * @param update       更新数据
     * @return
     */
    public void update(QuerySupport querySupport, Update update) {
        Assert.notNull(querySupport);
        Assert.notNull(update);
        this.getMongoTemplate().updateFirst(parseQuery(querySupport), update, entityClass, getCollectionName());
    }

    /**
     * 更新所有符合条件的记录
     *
     * @param querySupport 查询对象
     * @param updateData   更新数据
     * @return
     */
    public void bulkUpdate(QuerySupport querySupport, Map<String, Object> updateData) {
        Assert.notNull(querySupport);
        Assert.notNull(updateData);
        Update update = MongodbUtils.map2Update(updateData);
        bulkUpdate(querySupport, update);
    }

    /**
     * 更新所有符合条件的记录
     *
     * @param querySupport 查询对象
     * @param update       更新数据
     * @return
     */
    public void bulkUpdate(QuerySupport querySupport, Update update) {
        Assert.notNull(querySupport);
        Assert.notNull(update);
        this.getMongoTemplate().updateMulti(parseQuery(querySupport), update, entityClass, getCollectionName());
    }

    /**
     * 删除所有符合条件的记录
     *
     * @param key   栏位
     * @param value 值
     * @return
     */
    public void delete(String key, Object value) {
        Map<String, Object> queryData = new HashMap<String, Object>();
        queryData.put(key, value);
        delete(queryData);
    }

    /**
     * 删除所有符合条件的记录
     *
     * @param queryData 栏位与值列表
     * @return
     */
    public void delete(Map<String, Object> queryData) {
        Assert.notNull(queryData);
        delete(QuerySupport.createQuery().add(Restrictions.allEq(queryData)));
    }

    /**
     * 删除所有符合条件的记录
     *
     * @param querySupport 查询对象
     * @return
     */
    public void delete(QuerySupport querySupport) {
        Assert.notNull(querySupport);
        this.getMongoTemplate().remove(parseQuery(querySupport), entityClass, getCollectionName());
    }

    /**
     * 删除集合中的某条记录
     *
     * @param removeObject 删除对象
     * @return
     */
    public void delete(T removeObject) {
        Assert.notNull(removeObject);
        this.getMongoTemplate().remove(removeObject);
    }

    /**
     * 删除某个ID的记录
     *
     * @param id   id
     * @return
     */
    @Override
    public void deleteById(Serializable id) {
        this.delete(getCollectionName(), id);
    }

    /**
     * 根据ID数组删除对象.
     *
     * @param ids 实体id数组
     */
    @Override
    public void deleteByIds(Serializable[] ids) {
        if (!ArrayUtils.isEmpty(ids)) {
            Query query = new Query();
            query.addCriteria(Criteria.where(IDNAME).in(ids));
            this.getMongoTemplate().remove(query, entityClass, getCollectionName());
        }
    }

    public void refresh(T entity) {
        //this.getMongoTemplate().
        //TODO 暂未实现
        throw new RepositoryException("the method does not realized");
    }

    /**
     * 删除集合
     *
     * @return
     */
    public void dropCollection() {
        this.getMongoTemplate().dropCollection(entityClass);
    }

}
