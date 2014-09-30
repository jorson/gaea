package gaea.web.demo.simple.internal;

import gaea.access.hibernate.HibernateEntityDao;
import gaea.web.demo.simple.SimpleEntity;
import gaea.web.demo.simple.SimpleEntityDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 14-9-30.
 */
@Repository("simpleEntityDao")
public class SimpleEntityDaoImpl extends HibernateEntityDao<SimpleEntity> implements SimpleEntityDao {
    @Override
    public <T> void deleteAll() {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(getEntityClass().getSimpleName());
        getDoSession().createQuery(sql.toString()).executeUpdate();
    }
}
