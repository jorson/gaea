package gaea.access.hibernate.support;

import gaea.foundation.core.repository.shard.PartitionInfo;
import gaea.foundation.core.repository.shard.ShardDataRegister;
import gaea.foundation.core.repository.shard.ShardParameter;
import gaea.foundation.core.repository.support.Pager;
import gaea.foundation.core.repository.exception.RepositoryException;
import gaea.foundation.core.repository.query.QuerySupport;
import gaea.foundation.core.repository.query.criterion.*;
import gaea.foundation.core.repository.query.criterion.Criterion;
import gaea.foundation.core.repository.query.criterion.Junction;
import gaea.foundation.core.repository.query.criterion.Order;
import gaea.foundation.core.utils.BeanUtils;
import gaea.foundation.core.utils.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 请在这里输入说明
 *
 * @author wuhy
 */
public class HibernateQueryParser {

    public void parse(Criteria criteria, QuerySupport querySupport) {
        if (criteria != null && querySupport != null) {
            parseCriterions(criteria, querySupport.getCriterions());
            parseOrders(criteria, querySupport.getOrders());
            parsePager(criteria, querySupport.getPager());
            parsePartitionInfos(querySupport.getPartitionInfos());
        }
    }

    /**
     * 解析分页约束
     *
     * @param criteria
     * @param pager
     */
    public void parsePager(Criteria criteria, Pager pager) {
        if (pager != null) {
            int startIndex = Pager.getStartOfPage(pager.getPageNo(), pager.getPageSize());
            criteria.setFirstResult(startIndex).setMaxResults(pager.getPageSize());
        }
    }

    /**
     * 解析排序约束
     *
     * @param criteria
     * @param orders
     */
    public void parseOrders(Criteria criteria, List<Order> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return;
        }
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            criteria.addOrder(order.isAscending() ? org.hibernate.criterion.Order.asc(order.getPropertyName())
                    : org.hibernate.criterion.Order.desc(order.getPropertyName()));
        }
    }

    /**
     * 解析查询条件约束列表
     *
     * @param criteria
     * @param criterions
     */
    public void parseCriterions(Criteria criteria, List<Criterion> criterions) {
        if (CollectionUtils.isEmpty(criterions)) {
            return;
        }
        try {
            for (int i = 0; i < criterions.size(); i++) {
                Criterion criterion = criterions.get(i);
                criteria.add(parseCriterion(criterion));
            }
        } catch (Exception ex) {
            throw new RepositoryException("转换查询对象出错", ex);
        }
    }

    /**
     * 注册分区分表信息
     *
     * @param criteria
     * @param partitionInfos
     */
    public void parsePartitionInfos(List<PartitionInfo> partitionInfos) {
        if (CollectionUtils.isEmpty(partitionInfos)) {
            return;
        }
        ShardDataRegister.register(partitionInfos);
    }

    /**
     * 解决单个查询条件约束，把其变成Hibernate的Criterion
     *
     * @param criterion
     * @return
     * @throws Exception
     */
    private org.hibernate.criterion.Criterion parseCriterion(Criterion criterion) throws Exception {
        org.hibernate.criterion.Criterion result = null;
        if (criterion instanceof SimpleCriterion) {
            SimpleCriterion doCriterion = (SimpleCriterion) criterion;
            result = invokeRestrictionsMethod(doCriterion.getOperator().value(), doCriterion.getPropertyName(), doCriterion.getValue());
        } else if (criterion instanceof PropertyCriterion) {
            PropertyCriterion doCriterion = (PropertyCriterion) criterion;
            result = invokeRestrictionsPropertyMethod(doCriterion.getOperator().value(), doCriterion.getPropertyName(), doCriterion.getOtherPropertyName());
        } else if (criterion instanceof InCriterion) {
            InCriterion doCriterion = (InCriterion) criterion;
            result = Restrictions.in(doCriterion.getPropertyName(), doCriterion.getValues());
        } else if (criterion instanceof NotCriterion) {
            NotCriterion doCriterion = (NotCriterion) criterion;
            result = Restrictions.not(parseCriterion(doCriterion.getCriterion()));
        } else if (criterion instanceof NotNullCriterion) {
            NotNullCriterion doCriterion = (NotNullCriterion) criterion;
            result = Restrictions.isNotNull(doCriterion.getPropertyName());
        } else if (criterion instanceof NullCriterion) {
            NullCriterion doCriterion = (NullCriterion) criterion;
            result = Restrictions.isNull(doCriterion.getPropertyName());
        } else if (criterion instanceof Junction) {
            Junction doCriterion = (Junction) criterion;
            org.hibernate.criterion.Criterion[] criterions = parseCriterions(doCriterion.conditions()).toArray(new org.hibernate.criterion.Criterion[]{});
            return doCriterion.getNature() == Junction.Nature.AND ? Restrictions.conjunction(criterions) : Restrictions.disjunction(criterions);
        } else {
            throw new RepositoryException("Hibernate不支持该种条件约束");
        }
        return result;
    }

    /**
     * 解析查询条件约束
     *
     * @param criterions
     */
    private List<org.hibernate.criterion.Criterion> parseCriterions(List<Criterion> criterions) {
        List<org.hibernate.criterion.Criterion> result = new ArrayList<org.hibernate.criterion.Criterion>();
        if (CollectionUtils.isEmpty(criterions)) {
            return result;
        }
        try {
            for (int i = 0; i < criterions.size(); i++) {
                Criterion criterion = criterions.get(i);
                result.add(parseCriterion(criterion));
            }
        } catch (Exception ex) {
            throw new RepositoryException("转换查询对象出错", ex);
        }
        return result;
    }

    private org.hibernate.criterion.Criterion invokeRestrictionsMethod(String method, String propertyName, Object value) throws Exception {
        Method doMethod = BeanUtils.getDeclaredMethod(Restrictions.class, method, String.class, Object.class);
        return (org.hibernate.criterion.Criterion) doMethod.invoke(Restrictions.class, propertyName, value);
    }

    private org.hibernate.criterion.Criterion invokeRestrictionsPropertyMethod(String method, String propertyName, String otherPropertyName) throws Exception {
        Method doMethod = BeanUtils.getDeclaredMethod(Restrictions.class, method + "Property", String.class, String.class);
        return (org.hibernate.criterion.Criterion) doMethod.invoke(Restrictions.class, propertyName, otherPropertyName);
    }
}
