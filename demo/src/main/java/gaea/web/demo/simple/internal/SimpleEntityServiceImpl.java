package gaea.web.demo.simple.internal;

import gaea.foundation.core.repository.EntityRepository;
import gaea.foundation.core.service.AbstractService;
import gaea.foundation.core.service.DomainService;
import gaea.web.demo.simple.SimpleEntity;
import gaea.web.demo.simple.SimpleEntityDao;
import gaea.web.demo.simple.SimpleEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by Administrator on 14-9-30.
 */
@Transactional
@Service("simpleEntityService")
@DomainService
public class SimpleEntityServiceImpl extends AbstractService<SimpleEntity> implements SimpleEntityService {

    @Autowired
    private SimpleEntityDao simpleEntityDao;

//    @Override
//    public EntityRepository<SimpleEntity> getEntityDao() {
//        return simpleEntityDao;
//    }
    /**
     * 实体Dao
     *
     * @return
     */
    public EntityRepository<SimpleEntity> getEntityDao(){
        return simpleEntityDao;
    }

    /**
     * 删除表所有数据,用于测试
     */
    public void deleteAll() {
        this.simpleEntityDao.deleteAll();
    }
}
