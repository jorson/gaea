package com.nd.gaea.repository.mongodb.object;

import com.nd.gaea.repository.mongodb.MongodbRepository;
import org.springframework.stereotype.Repository;

/**
 * 请在这里输入说明
 *
 * @author bifeng.liu
 */
@Repository("simpleEntityDao")
public class SimpleEntityRepositoryImpl extends MongodbRepository<SimpleEntity> implements SimpleEntityRepository {
    /**
     * 删除集合
     *
     * @return
     */
    public void dropCollection() {
        this.getMongoTemplate().dropCollection(entityClass);
    }
}
