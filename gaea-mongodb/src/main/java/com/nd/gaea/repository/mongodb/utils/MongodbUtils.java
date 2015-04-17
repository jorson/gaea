package com.nd.gaea.repository.mongodb.utils;

import com.nd.gaea.utils.StringUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.*;

/**
 * 请在这里输入说明
 *
 * @author bifeng.liu
 */
public class MongodbUtils {
    /**
     * 把DBObject列表转换成Map列表
     *
     * @param dataList
     * @return
     */
    public static List<Map<String, Object>> dbObject2MapList(Collection<DBObject> dataList) {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>(dataList.size());
        if (resultList != null) {
            for (Iterator<DBObject> iterator = dataList.iterator(); iterator.hasNext(); ) {
                DBObject data = iterator.next();
                resultList.add(data.toMap());
            }
        }
        return resultList;
    }

    /**
     * 把Map列表转换成DBObject列表
     *
     * @param dataList
     * @return
     */
    public static List<DBObject> map2DBObjectList(Collection<Map<String, Object>> dataList) {
        List<DBObject> resultList = new ArrayList<DBObject>(dataList.size());
        if (resultList != null) {
            for (Iterator<Map<String, Object>> iterator = dataList.iterator(); iterator.hasNext(); ) {
                Map data = iterator.next();
                DBObject obj = new BasicDBObject();
                obj.putAll(data);
                resultList.add(obj);
            }
        }
        return resultList;
    }

    /**
     * Map转换Query对象
     *
     * @param queryData
     * @return
     */
    public static Query map2Query(Map<String, Object> queryData) {
        Query query = new Query();
        for (Iterator<Map.Entry<String, Object>> iterator = queryData.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Object> data = iterator.next();
            String key = data.getKey();
            Object value = data.getValue();
            if (StringUtils.hasText(key) && value != null) {
                query.addCriteria(Criteria.where(data.getKey()).is(data.getValue()));
            }
        }
        return query;
    }

    /**
     * Map转换Update对象
     *
     * @param updateData
     * @return
     */
    public static Update map2Update(Map<String, Object> updateData) {
        Update update = new Update();
        for (Iterator<Map.Entry<String, Object>> iterator = updateData.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Object> data = iterator.next();
            String key = data.getKey();
            Object value = data.getValue();
            if (StringUtils.hasText(key)) {
                update.set(key, value);
            }
        }
        return update;
    }
}
