package gaea.web.demo.simple;

import gaea.foundation.core.service.EntityService;

/**
 * Created by Administrator on 14-9-30.
 */
public interface SimpleEntityService extends EntityService<SimpleEntity> {

    public void deleteAll();
}
