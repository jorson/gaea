package com.nd.gaea.core.repository.query;


import com.nd.gaea.core.repository.query.criterion.Criterion;
import com.nd.gaea.core.repository.query.criterion.Order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 查询对象
 *
 * @author bifeng.liu
 */
public class QuerySupport {
    /**
     * 规则条件约束列表
     */
    private List<Criterion> criterions = new ArrayList<Criterion>();
    /**
     * 排序
     */
    private List<Order> orders = new ArrayList<Order>();

    /**
     * 查询起始位置
     */
    private int offset = 0;

    /**
     * 查询条数
     */
    private int limit = 0;

    /**
     * 查询结果中是否需要包含记录数
     */
    private boolean hasCount = false;

    /**
     * 创建默认的QuerySupport对象
     *
     * @return
     */
    public static QuerySupport createQuery() {
        return new QuerySupport();
    }

    public QuerySupport addCriterion(Criterion criterion) {
        this.criterions.add(criterion);
        return this;
    }

    public QuerySupport addCriterions(Collection<Criterion> criterions) {
        this.criterions.addAll(criterions);
        return this;
    }

    public QuerySupport addOrder(Order order) {
        this.orders.add(order);
        return this;
    }

    public QuerySupport addOrders(Collection<Order> orders) {
        this.orders.addAll(orders);
        return this;
    }

    /**
     * 设置起始位置
     *
     * @param offset
     * @return
     */
    public QuerySupport offset(int offset) {
        this.offset = offset;
        return this;
    }

    /**
     * 设置查询条数
     *
     * @param limit
     * @return
     */
    public QuerySupport limit(int limit) {
        this.limit = limit;
        return this;
    }

    public List<Criterion> getCriterions() {
        return criterions;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public boolean getHasCount() {
        return hasCount;
    }

    public void setHasCount(boolean hasCount) {
        this.hasCount = hasCount;
    }
}
