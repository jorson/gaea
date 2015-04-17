package com.nd.gaea.repository.mongodb.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Mongodb的Dao支持类
 *
 * @author bifeng.liu
 */
public abstract class MongodbDaoSupport extends DaoSupport {

    /**
     * 通过外部设置MongoTemplate
     */
    @Autowired(required = false)
    private MongoTemplate mongoTemplate = null;

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 检查Dao的配置
     * <p/>
     * 如果MongoTemplate，则直接报错
     */
    @Override
    protected final void checkDaoConfig() {
        if (this.mongoTemplate == null) {
            throw new IllegalArgumentException("'mongoTemplate' is required");
        }
    }
}
