package gaea.access.mongodb.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Mongodb的Dao支持类
 *
 * @author wuhy
 */
public abstract class MongodbDaoSupport extends DaoSupport {
    @Autowired(required = false)
    private MongoTemplate mongoTemplate = null;

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    protected final void checkDaoConfig() {
        if (this.mongoTemplate == null) {
            throw new IllegalArgumentException("'mongoTemplate' is required");
        }
    }
}
