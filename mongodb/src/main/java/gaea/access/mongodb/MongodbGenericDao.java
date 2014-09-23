package gaea.access.mongodb;

import gaea.foundation.core.repository.Repository;
import gaea.access.mongodb.support.MongodbDaoSupport;
import gaea.access.mongodb.support.MongodbQueryParser;
import gaea.foundation.core.repository.query.QuerySupport;
import gaea.foundation.core.repository.query.criterion.Restrictions;
import gaea.foundation.core.repository.support.Pager;
import gaea.foundation.core.utils.CollectionUtils;
import gaea.foundation.core.utils.Utils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;


/**
 * Mongodb Dao非泛型的基类.
 * <p/>
 * Mongodb连接使用Spring配置
 *
 * @author wuhy
 */
public class MongodbGenericDao extends MongodbDaoSupport implements Repository {

    protected static final String IDNAME = "_id";

    /**
     * Query转换器
     */
    protected MongodbQueryParser parser;

    public MongodbGenericDao() {
        parser = new MongodbQueryParser();
    }

    //-------------------------------------------------------------------------
    // 查询
    //-------------------------------------------------------------------------

    /**
     * 取得集合某个ID的数据，只返回第一条数据
     *
     * @param id             ID值
     * @param collectionName 集合名称
     * @return
     */
    public Object get(String collectionName, Serializable id) {
        return get(collectionName, IDNAME, id);
    }

    /**
     * 取得某个集合的某个栏位与传入值相等的数据，只返回第一条数据
     *
     * @param key            栏位
     * @param value          值
     * @param collectionName 集合名称
     * @return
     */
    public Map get(String collectionName, String key, Object value) {
        Map<String, Object> queryData = new HashMap<String, Object>();
        queryData.put(key, value);
        return get(collectionName, queryData);
    }

    /**
     * 取得某个集合的栏位与传入值相等的数据，只返回第一条数据
     *
     * @param queryData      栏位与值列表
     * @param collectionName 集合名称
     * @return
     */
    public Map get(String collectionName, Map<String, Object> queryData) {
        Assert.notNull(queryData);
        return get(collectionName, QuerySupport.createQuery().add(Restrictions.allEq(queryData)));
    }

    /**
     * 根据查询对象取得某个集合的数据，只返回第一条数据
     *
     * @param querySupport   查询对象
     * @param collectionName 集合名称
     * @return
     */
    public Map get(String collectionName, QuerySupport querySupport) {
        Assert.notNull(querySupport);
        Assert.hasText(collectionName);
        DBCollection collection = this.getMongoTemplate().getCollection(collectionName);
        DBObject object = null;
        if (collection != null) {
            Query query = parseQuery(querySupport);
            object = collection.findOne(query.getQueryObject(), query.getFieldsObject(), query.getSortObject());
        }
        return object != null ? object.toMap() : null;
    }

    /**
     * 查询集合中所有数据
     *
     * @param collectionName 集合名称
     * @return
     */
    public List findAll(String collectionName) {
        DBCollection collection = this.getMongoTemplate().getCollection(collectionName);
        DBCursor cursor = collection.find();
        List<DBObject> dbList = cursor.toArray();
        return MongodbUtils.dbObject2MapList(dbList);
    }


    /**
     * 取得某个集合的某个栏位与传入值相等的所有数据
     *
     * @param key            栏位
     * @param value          值
     * @param collectionName 集合名称
     * @return
     */
    public List find(String collectionName, String key, Object value) {
        Map<String, Object> queryData = new HashMap<String, Object>();
        queryData.put(key, value);
        return find(collectionName, queryData);
    }

    /**
     * 取得某个集合的栏位与传入值相等的所有数据
     *
     * @param queryData      栏位与值列表
     * @param collectionName 集合名称
     * @return
     */
    public List find(String collectionName, Map<String, Object> queryData) {
        Assert.notNull(queryData);
        return find(collectionName, QuerySupport.createQuery().add(Restrictions.allEq(queryData)));
    }

    /**
     * 根据查询对象取得某个集合中符合条件的数据
     *
     * @param querySupport          查询对象
     * @param collectionName 集合名称
     * @return
     */
    public List find(String collectionName, QuerySupport querySupport) {
        Assert.notNull(querySupport);
        Assert.hasText(collectionName);
        DBCollection collection = this.getMongoTemplate().getCollection(collectionName);
        if (collection != null) {
            Query query = parseQuery(querySupport);
            DBCursor cursor = collection.find(query.getQueryObject(), query.getFieldsObject());
            if(query.getSkip() > 0) {
                cursor.skip(query.getSkip());
            }
            if (query.getLimit() > 0) {
                cursor.limit(query.getLimit());
            }
            if (query.getSortObject() != null) {
                cursor.sort(query.getSortObject());
            }
            List<DBObject> dbList = cursor.toArray();
            return MongodbUtils.dbObject2MapList(dbList);
        }
        return new ArrayList();
    }

    /**
     * 取得某个表或集合中满足条件的条件
     *
     * @param collectionName 集合名称
     * @param querySupport   查询对象
     * @return
     */
    public long count(String collectionName, QuerySupport querySupport) {
        return this.getMongoTemplate().count(parseQuery(querySupport), collectionName);
    }

    /**
     * 根据查询对象分页数据，会返回totalCount
     *
     * @param collectionName 集合名称
     * @param querySupport   查询对象
     * @return 含总记录数和当前页数据的Page对象.
     */
    public Pager pagedQuery(String collectionName, QuerySupport querySupport) {
        Assert.notNull(querySupport);
        Pager pager = querySupport.getPager() == null ? Pager.EMPTY_PAGER : querySupport.getPager();
        long totalCount = count(collectionName, querySupport);
        // 返回分页对象
        if (totalCount < 1) {
            return new Pager();
        }
        List dataList = this.find(collectionName, querySupport);
        pager.setData(dataList);
        pager.setTotalRecordCount(totalCount);
        return pager;
    }

    //-------------------------------------------------------------------------
    // 更新
    //-------------------------------------------------------------------------

    /**
     * 添加单条记录数据
     *
     * @param saveData       记录数据
     * @param collectionName 集合名称
     */
    public void insert(String collectionName, Map<String, Object> saveData) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        dataList.add(saveData);
        insert(collectionName, dataList);
    }

    /**
     * 批量添加数据
     *
     * @param batchData      批量数据
     * @param collectionName 集合名称
     * @return
     */
    public void insert(String collectionName, Collection<Map<String, Object>> batchData) {
        Assert.hasText(collectionName);
        if (!CollectionUtils.isEmpty(batchData)) {
            DBCollection collection = this.getMongoTemplate().getCollection(collectionName);
            collection.insert(MongodbUtils.map2DBObjectList(batchData));
        }
    }

    /**
     * 保存对象，如果对象已经存在则更新
     *
     * @param collectionName 表或集合名称
     * @param saveData       保存数据
     */
    @Override
    public void save(String collectionName, Map<String, Object> saveData) {
        Object id = Utils.getMapValue(saveData, IDNAME);
        if (id == null) { //如果saveData不保存有ID，则直接插入
            insert(collectionName, saveData);
        } else {
            Map obj = get(collectionName, IDNAME, id);
            if (obj == null) {
                insert(collectionName, obj);
            } else {
                update(collectionName, (Serializable) id, saveData);
            }
        }
    }

    /**
     * 更新单条数据
     *
     * @param id             ID
     * @param updateData     更新数据
     * @param collectionName 表或集合名称
     * @return
     */
    public void update(String collectionName, Serializable id, Map<String, Object> updateData) {
        QuerySupport query = QuerySupport.createQuery().add(Restrictions.eq(IDNAME, id));
        update(collectionName, query, updateData);
    }

    /**
     * 更新单条数据
     *
     * @param queryData      查询条件
     * @param updateData     更新数据
     * @param collectionName 集合名称
     * @return
     */
    public void update(String collectionName, Map<String, Object> queryData, Map<String, Object> updateData) {
        Assert.notNull(queryData);
        update(collectionName, QuerySupport.createQuery().add(Restrictions.allEq(queryData)), updateData);
    }

    /**
     * 更新单条数据
     *
     * @param querySupport   查询对象
     * @param updateData     更新数据
     * @param collectionName 集合名称
     * @return
     */
    public void update(String collectionName, QuerySupport querySupport, Map<String, Object> updateData) {
        Assert.notNull(querySupport);
        Assert.notNull(updateData);
        Assert.hasText(collectionName);
        DBCollection collection = this.getMongoTemplate().getCollection(collectionName);
        if (collection != null) {
            DBObject updateObj = new BasicDBObject();
            Map<String, Object> tempData = this.get(collectionName, querySupport);
            if (tempData != null) {
                tempData.remove("_id");
                updateObj.putAll(tempData);
            }
            updateObj.putAll(updateData);
            Query query = parseQuery(querySupport);
            collection.update(query.getQueryObject(), updateObj, true, false).getN();
        }

    }

    /**
     * 更新所有符合条件的记录
     *
     * @param queryData      查询条件
     * @param updateData     更新数据
     * @param collectionName 集合名称
     * @return
     */
    public void bulkUpdate(String collectionName, Map<String, Object> queryData, Map<String, Object> updateData) {
        Assert.notNull(queryData);
        Assert.notNull(updateData);
        Assert.hasText(collectionName);
        bulkUpdate(collectionName, QuerySupport.createQuery().add(Restrictions.allEq(queryData)), updateData);
    }

    /**
     * 更新所有符合条件的记录
     *
     * @param querySupport   查询对象
     * @param updateData     更新数据
     * @param collectionName 集合名称
     * @return
     */
    public void bulkUpdate(String collectionName, QuerySupport querySupport, Map<String, Object> updateData) {
        Assert.notNull(querySupport);
        Assert.notNull(updateData);
        Assert.hasText(collectionName);
        this.getMongoTemplate().updateMulti(parseQuery(querySupport), MongodbUtils.map2Update(updateData), collectionName).getN();
    }

    /**
     * 删除某个ID的记录
     *
     * @param id             ID栏位
     * @param collectionName 集合名称
     * @return
     */
    @Override
    public void delete(String collectionName, Serializable id) {
        Assert.notNull(id);
        delete(collectionName, IDNAME, id);
    }

    /**
     * 删除所有符合条件的记录
     *
     * @param key            栏位
     * @param value          值
     * @param collectionName 集合名称
     * @return
     */
    public void delete(String collectionName, String key, Object value) {
        Assert.hasText(key);
        Assert.notNull(value);
        Map<String, Object> queryData = new HashMap<String, Object>();
        queryData.put(key, value);
        delete(queryData, collectionName);
    }


    /**
     * 删除所有符合条件的记录
     *
     * @param queryData      查询条件
     * @param collectionName 集合名称
     * @return
     */
    public void delete(Map<String, Object> queryData, String collectionName) {
        Assert.notNull(queryData);
        delete(collectionName, QuerySupport.createQuery().add(Restrictions.allEq(queryData)));
    }

    /**
     * 删除所有符合条件的记录
     *
     * @param querySupport          查询对象
     * @param collectionName 集合名称
     * @return
     */
    public void delete(String collectionName, QuerySupport querySupport) {
        Assert.notNull(querySupport);
        Assert.hasText(collectionName);
        DBCollection collection = this.getMongoTemplate().getCollection(collectionName);
        if (collection != null) {
            collection.remove(parseQuery(querySupport).getQueryObject()).getN();
        }
    }

    //-------------------------------------------------------------------------
    // 扩展功能
    //-------------------------------------------------------------------------

    /**
     * 取得集合
     *
     * @param collectionName 集合名称
     * @return
     */
    public DBCollection getCollection(String collectionName) {
        return this.getMongoTemplate().getCollection(collectionName);
    }

    /**
     * 创建集合
     *
     * @param collectionName 集合名称
     * @return
     */
    public DBCollection createCollection(String collectionName) {
        return this.getMongoTemplate().createCollection(collectionName);
    }

    /**
     * 删除集合
     *
     * @param collectionName 集合名称
     * @return
     */
    public void dropCollection(String collectionName) {
        this.getMongoTemplate().dropCollection(collectionName);
    }

    protected Query parseQuery(QuerySupport querySupport) {
        Query query = new Query();
        parser.parse(query, querySupport);
        return query;
    }

}
