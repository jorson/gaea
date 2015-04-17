package com.nd.gaea.repository.hibernate.support;

import com.nd.gaea.core.repository.exception.RepositoryException;
import com.nd.gaea.core.repository.query.QuerySupport;
import com.nd.gaea.utils.ArrayUtils;
import com.nd.gaea.utils.BeanUtils;
import com.nd.gaea.core.repository.query.criterion.*;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Hibernate查询参数解析类
 * <p/>
 * 把QuerySupport中的参数传入到Criteria对象中
 *
 * @author bifeng.liu
 */
public class HibernateQueryParser {

    public void parse(Criteria criteria, QuerySupport querySupport) {
        if (criteria != null && querySupport != null) {
            parseCriterions(criteria, querySupport.getCriterions());
            parseOrders(criteria, querySupport.getOrders());
            parsePager(criteria, querySupport.getOffset(), querySupport.getLimit());
            //parsePartitionInfos(querySupport.getPartitionInfos());
        }
    }

    /**
     * 解析分页约束
     *
     * @param criteria
     * @param offset
     * @param limit
     */
    public void parsePager(Criteria criteria, int offset, int limit) {
        criteria.setFirstResult(offset).setMaxResults(limit);
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

//    /**
//     * 注册分区分表信息
//     *
//     * @param criteria
//     * @param partitionInfos
//     */
//    public void parsePartitionInfos(List<PartitionInfo> partitionInfos) {
//        if (CollectionUtils.isEmpty(partitionInfos)) {
//            return;
//        }
//        ShardDataRegister.register(partitionInfos);
//    }

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
            if (!ArrayUtils.isEmpty(doCriterion.getValues())) {  //如果In的数组为空，则直接使用1!=1返回
                result = Restrictions.in(doCriterion.getPropertyName(), doCriterion.getValues());
            } else {
                result = Restrictions.sqlRestriction(" 1!=1 ");
            }
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
