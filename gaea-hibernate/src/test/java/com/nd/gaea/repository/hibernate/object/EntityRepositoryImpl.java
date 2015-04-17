package com.nd.gaea.repository.hibernate.object;

import com.nd.gaea.core.service.DomainService;
import com.nd.gaea.repository.hibernate.HibernateRepository;
import com.nd.gaea.utils.DateUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;

/**
 * Entity Dao的测试类
 *
 * @author bifeng.liu
 */
@Repository("entityDao")
@DomainService
public class EntityRepositoryImpl extends HibernateRepository<SimpleEntity> implements EntityRepository {
    public void deleteAll() {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(entityClass.getSimpleName());
        getSession().createQuery(sql.toString()).executeUpdate();
    }

    @Override
    public void testThrowException() throws Exception {
        SimpleEntity simpleObject = new SimpleEntity("simple_object", DateUtils.parse("2009-05-21", "yyyy-MM-dd"), 20, 1, "remark");
        this.create(simpleObject);
        simpleObject.setAge(1000);
        this.update(simpleObject);
        if (true) {
            throw new Exception("出错了，事务回滚");
        }
    }

    /**
     * 判断对象某些属性的值在数据库中是否唯一.
     *
     * @param uniquePropertyNames 在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
     */
    public boolean isUnique(SimpleEntity entity, String uniquePropertyNames) {
        Assert.hasText(uniquePropertyNames);
        Criteria criteria = this.getHibernateTemplate().createCriteria(entityClass).setProjection(Projections.rowCount());
        String[] nameList = uniquePropertyNames.split(",");
        try {
            // 循环加入唯一列
            for (String name : nameList) {
                criteria.add(Restrictions.eq(name, PropertyUtils.getProperty(entity, name)));
            }
            //排除entity自身.
            String idName = getIdName(getEntityClass());
            // 取得entity的主键值
            Serializable id = getId(entity);
            // 如果id!=null,说明对象已存在,加入排除自身的判断
            if (id != null) {
                criteria.add(Restrictions.not(Restrictions.eq(idName, id)));
            }
        } catch (Exception ex) {
            logger.error(ex);
            ReflectionUtils.handleReflectionException(ex);
        }
        return (Long) criteria.uniqueResult() == 0;
    }
}
