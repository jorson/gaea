package com.nd.gaea.repository.mongodb.support;

import com.nd.gaea.core.repository.exception.RepositoryException;
import com.nd.gaea.core.repository.query.QuerySupport;
import com.nd.gaea.core.repository.query.criterion.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Mongodb查询参数解析类
 * <p/>
 * 把QuerySupport中的参数传入到Query对象中
 *
 * @author bifeng.liu
 */
public class MongodbQueryParser {

    /**
     * 解析Query   QuerySupport-> Query
     *
     * @param query        Query
     * @param querySupport QuerySupport
     */
    public void parse(Query query, QuerySupport querySupport) {
        if (query != null && querySupport != null) {
            parseCriterions(query, querySupport.getCriterions());
            parseOrders(query, querySupport.getOrders());
            parsePager(query, querySupport.getOffset(), querySupport.getLimit());
        }
    }

    /**
     * 解析分页约束
     *
     * @param query
     * @param offset
     * @param limit
     */
    public void parsePager(Query query, int offset, int limit) {
        query.skip(offset).limit(limit);
    }

    /**
     * 解析排序约束
     *
     * @param query
     * @param orders
     */
    public void parseOrders(Query query, List<Order> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return;
        }
        List<Sort.Order> results = new ArrayList<Sort.Order>(orders.size());
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            Sort.Order sOrder = parseOrder(order);
            results.add(sOrder);
        }
        query.with(new Sort(results));
    }

    /**
     * 解析查询条件约束
     *
     * @param query
     * @param criterions
     */
    public void parseCriterions(Query query, List<Criterion> criterions) {
        if (CollectionUtils.isEmpty(criterions)) {
            return;
        }
        try {
            for (int i = 0; i < criterions.size(); i++) {
                Criterion criterion = criterions.get(i);
                Criteria criteria = parseCriterion(criterion);
                if (criteria != null) {
                    query.addCriteria(criteria);
                }
            }
        } catch (Exception ex) {
            throw new RepositoryException("转换查询对象出错", ex);
        }
    }

    /**
     * 解析Order
     *
     * @param order
     * @return
     */
    protected Sort.Order parseOrder(Order order) {
        return new Sort.Order(order.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC, order.getPropertyName());
    }

    /**
     * 解决单个查询条件约束，把其变成Mongodb的Criteria
     *
     * @param criterion
     * @return
     * @throws Exception
     */
    protected Criteria parseCriterion(Criterion criterion) throws Exception {
        Criteria result = null;
        if (criterion instanceof SimpleCriterion) {
            SimpleCriterion doCriterion = (SimpleCriterion) criterion;
            result = parseSimpleCriterion(doCriterion.getOperator(), doCriterion.getPropertyName(), doCriterion.getValue());
        } else if (criterion instanceof InCriterion) {
            InCriterion doCriterion = (InCriterion) criterion;
            result = Criteria.where(doCriterion.getPropertyName()).in(doCriterion.getValues());
        } else if (criterion instanceof NotNullCriterion) {
            NotNullCriterion doCriterion = (NotNullCriterion) criterion;
            result = Criteria.where(doCriterion.getPropertyName()).exists(true);
        } else if (criterion instanceof NullCriterion) {
            NullCriterion doCriterion = (NullCriterion) criterion;
            result = Criteria.where(doCriterion.getPropertyName()).exists(false);
        } else if (criterion instanceof NotCriterion) {
            NotCriterion doCriterion = (NotCriterion) criterion;
            result = parseCriterion(doCriterion.getCriterion()).not();
        } else if (criterion instanceof Junction) {
            Junction doCriterion = (Junction) criterion;
            result = parseCriterions(doCriterion.conditions(), doCriterion.getNature() == Junction.Nature.AND);
        } else {
            throw new RepositoryException("Mongodb不支持该种条件约束");
        }
        return result;
    }

    /**
     * 解析查询条件约束
     *
     * @param criterions
     */
    protected Criteria parseCriterions(List<Criterion> criterions, boolean isAnd) {
        Criteria result = null;
        if (CollectionUtils.isEmpty(criterions)) {
            return result;
        }
        try {
            List<Criteria> criterias = new ArrayList<Criteria>();
            for (int i = 0; i < criterions.size(); i++) {
                Criterion criterion = criterions.get(i);
                Criteria criteria = parseCriterion(criterion);
                if (result == null) {
                    result = criteria;
                } else {
                    criterias.add(criteria);
                }
            }
            if (isAnd && !CollectionUtils.isEmpty(criterias)) {
                result.andOperator(criterias.toArray(new Criteria[]{}));
            } else if (!CollectionUtils.isEmpty(criterias)) {
                result.orOperator(criterias.toArray(new Criteria[]{}));
            }
        } catch (Exception ex) {
            throw new RepositoryException("转换查询对象出错", ex);
        }

        return result;
    }

    /**
     * 转换简单的条件约束
     *
     * @param operator
     * @param propertyName
     * @param value
     * @return
     * @throws Exception
     */
    protected Criteria parseSimpleCriterion(Operator operator, String propertyName, Object value) throws Exception {
        Criteria result = null;
        switch (operator) {
            case EQ:
                result = Criteria.where(propertyName).is(value);
                break;
            case NE:
                result = Criteria.where(propertyName).ne(value);
                break;
            case LIKE:
                result = Criteria.where(propertyName).regex(".*(?i)" + convertRegexp(value.toString()) + ".*");
                break;
            case LT:
                result = Criteria.where(propertyName).lt(value);
                break;
            case GT:
                result = Criteria.where(propertyName).gt(value);
                break;
            case LE:
                result = Criteria.where(propertyName).lte(value);
                break;
            case GE:
                result = Criteria.where(propertyName).gte(value);
                break;
        }
        return result;
    }

    /**
     * 将正则表达式的符号转义掉
     *
     * @param str
     * @return
     */
    private String convertRegexp(String str) {
        return str.replaceAll("\\\\", "\\\\\\\\")
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\$", "\\\\$")
                .replaceAll("\\^", "\\\\^")
                .replaceAll("\\{", "\\\\{")
                .replaceAll("\\}", "\\\\}")
                .replaceAll("\\[", "\\\\[")
                .replaceAll("\\]", "\\\\]")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)")
                .replaceAll("\\|", "\\\\|")
                .replaceAll("\\*", "\\\\*")
                .replaceAll("\\+", "\\\\+")
                .replaceAll("\\?", "\\\\?");
    }
}
