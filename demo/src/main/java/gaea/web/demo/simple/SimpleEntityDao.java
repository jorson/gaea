package gaea.web.demo.simple;

import gaea.foundation.core.repository.EntityRepository;

/**
 * Created by Administrator on 14-9-30.
 */
public interface SimpleEntityDao extends EntityRepository<SimpleEntity> {

    public <T> void deleteAll();
}
