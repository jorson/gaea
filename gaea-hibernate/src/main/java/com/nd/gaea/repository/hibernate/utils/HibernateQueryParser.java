package com.nd.gaea.repository.hibernate.utils;

import com.nd.gaea.core.repository.exception.RepositoryException;
import com.nd.gaea.core.repository.query.QuerySupport;
import com.nd.gaea.core.repository.query.criterion.*;
import com.nd.gaea.utils.BeanUtils;
import com.nd.gaea.core.repository.query.criterion.Junction;
import org.hibernate.Criteria;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 请在这里输入说明
 *
 * @author bifeng.liu
 */
public class HibernateQueryParser {

    public static void parse(Criteria criteria, QuerySupport querySupport) {
        if (criteria != null && querySupport != null) {
            parseCriterions(criteria, querySupport.getCriterions());
            parseOrders(criteria, querySupport.getOrders());

        }
    }

    /**
     * 解析分页约束
     *
     * @param criteria
     * @param querySupport
     */
    public static void parsePager(Criteria criteria, QuerySupport querySupport) {
        if (querySupport != null) {

            criteria.setFirstResult(querySupport.getOffset()).setMaxResults(querySupport.getLimit());
        }
    }

    /**
     * 解析排序约束
     *
     * @param criteria
     * @param orders
     */
    public static void parseOrders(Criteria criteria, List<com.nd.gaea.core.repository.query.criterion.Order> orders) {
        if (orders==null||orders.size()==0) {
            return;
        }
        for (int i = 0; i < orders.size(); i++) {
            com.nd.gaea.core.repository.query.criterion.Order order = orders.get(i);
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
    public static void parseCriterions(Criteria criteria, List<com.nd.gaea.core.repository.query.criterion.Criterion> criterions) {
        if (criterions==null||criterions.size()==0) {
            return;
        }
        try {
            for (int i = 0; i < criterions.size(); i++) {
                com.nd.gaea.core.repository.query.criterion.Criterion criterion = criterions.get(i);
                criteria.add(parseCriterion(criterion));
            }
        } catch (Exception ex) {
            throw new RepositoryException("转换查询对象出错", ex);
        }
    }

    /**
     * 解决单个查询条件约束，把其变成Hibernate的Criterion
     *
     * @param criterion
     * @return
     * @throws Exception
     */
    private static org.hibernate.criterion.Criterion parseCriterion(com.nd.gaea.core.repository.query.criterion.Criterion criterion) throws Exception {
        org.hibernate.criterion.Criterion result = null;
        if (criterion instanceof SimpleCriterion) {
            SimpleCriterion doCriterion = (SimpleCriterion) criterion;
            result = invokeRestrictionsMethod(doCriterion.getOperator().value(), doCriterion.getPropertyName(), doCriterion.getValue());
        } else if (criterion instanceof PropertyCriterion) {
            PropertyCriterion doCriterion = (PropertyCriterion) criterion;
            result = invokeRestrictionsPropertyMethod(doCriterion.getOperator().value(), doCriterion.getPropertyName(), doCriterion.getOtherPropertyName());
        } else if (criterion instanceof InCriterion) {
            InCriterion doCriterion = (InCriterion) criterion;
            result =  org.hibernate.criterion.Restrictions.in(doCriterion.getPropertyName(), doCriterion.getValues());
        } else if (criterion instanceof NotCriterion) {
            NotCriterion doCriterion = (NotCriterion) criterion;
            result = org.hibernate.criterion.Restrictions.not(parseCriterion(doCriterion.getCriterion()));
        } else if (criterion instanceof NotNullCriterion) {
            NotNullCriterion doCriterion = (NotNullCriterion) criterion;
            result = org.hibernate.criterion.Restrictions.isNotNull(doCriterion.getPropertyName());
        } else if (criterion instanceof NullCriterion) {
            NullCriterion doCriterion = (NullCriterion) criterion;
            result = org.hibernate.criterion.Restrictions.isNull(doCriterion.getPropertyName());
        } else if (criterion instanceof Junction) {
            Junction doCriterion = (Junction) criterion;
            org.hibernate.criterion.Criterion[] criterions = parseCriterions(doCriterion.conditions()).toArray(new org.hibernate.criterion.Criterion[]{});
            return doCriterion.getNature() == Junction.Nature.AND ? org.hibernate.criterion.Restrictions.conjunction(criterions) : org.hibernate.criterion.Restrictions.disjunction(criterions);
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
    private static List<org.hibernate.criterion.Criterion> parseCriterions(List<com.nd.gaea.core.repository.query.criterion.Criterion> criterions) {
        List<org.hibernate.criterion.Criterion> result = new ArrayList<>();
        if (criterions==null||criterions.size()==0) {
            return result;
        }
        try {
            for (int i = 0; i < criterions.size(); i++) {
                com.nd.gaea.core.repository.query.criterion.Criterion criterion = criterions.get(i);
                result.add(parseCriterion(criterion));
            }
        } catch (Exception ex) {
            throw new RepositoryException("转换查询对象出错", ex);
        }
        return result;
    }

    private static org.hibernate.criterion.Criterion invokeRestrictionsMethod(String method, String propertyName, Object value) throws Exception {
        Method doMethod = BeanUtils.getDeclaredMethod(org.hibernate.criterion.Restrictions.class, method, String.class, Object.class);
        return (org.hibernate.criterion.Criterion) doMethod.invoke(org.hibernate.criterion.Restrictions.class, propertyName, value);
    }

    private static org.hibernate.criterion.Criterion invokeRestrictionsPropertyMethod(String method, String propertyName, String otherPropertyName) throws Exception {
        Method doMethod = BeanUtils.getDeclaredMethod(org.hibernate.criterion.Restrictions.class, method + "Property", String.class, String.class);
        return (org.hibernate.criterion.Criterion) doMethod.invoke(org.hibernate.criterion.Restrictions.class, propertyName, otherPropertyName);
    }
}
