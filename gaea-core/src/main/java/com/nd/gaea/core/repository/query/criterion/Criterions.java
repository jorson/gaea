package com.nd.gaea.core.repository.query.criterion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>限制条件列表工具类</p>
 *
 * @author bifeng.liu
 * @since 2014/11/25
 */
public class Criterions {
    /**
     * 规则条件约束列表
     */
    private List<Criterion> criterions = new ArrayList<Criterion>();

    /**
     * 创建限制条件列表
     *
     * @return
     */
    public static Criterions create() {
        return new Criterions();
    }

    /**
     * 取得条件约束列表
     *
     * @return
     */
    public List<Criterion> list() {
        return criterions;
    }

    /**
     * 获取栏位值相等的约束
     *
     * @param propertyName 栏位名称
     * @param value        值
     * @return SimpleCriterion
     */
    public Criterions eq(String propertyName, Object value) {
        criterions.add(Restrictions.eq(propertyName, value));
        return this;
    }

    /**
     * 批量添加所有栏位值相等的约束
     *
     * @param propertyNameValues
     * @return
     */
    public Criterions allEq(Map<String, ?> propertyNameValues) {
        final Conjunction conj = new Conjunction();

        for (Map.Entry<String, ?> entry : propertyNameValues.entrySet()) {
            conj.add(Restrictions.eq(entry.getKey(), entry.getValue()));
        }
        criterions.add(conj);
        return this;
    }

    /**
     * 获取栏位值相等的约束，如果值为null，则使用isnull的约束来替代
     *
     * @param propertyName 栏位名称
     * @param value        值
     * @return this
     * @see #eq
     * @see #isNull
     */
    public Criterions eqOrIsNull(String propertyName, Object value) {
        criterions.add(value == null ? Restrictions.isNull(propertyName) : Restrictions.eq(propertyName, value));
        return this;
    }

    /**
     * 获取栏位值不相等的约束
     *
     * @param propertyName 栏位名称
     * @param value        值
     * @return this
     */
    public Criterions ne(String propertyName, Object value) {
        criterions.add(Restrictions.ne(propertyName, value));
        return this;
    }

    /**
     * 获取栏位值相等的约束，如果值为null，则使用is not null的约束来替代
     *
     * @param propertyName 栏位名称
     * @param value        值
     * @return this
     * @see #ne
     * @see #isNotNull
     */
    public Criterions neOrIsNotNull(String propertyName, Object value) {
        criterions.add(value == null ? Restrictions.isNotNull(propertyName) : Restrictions.ne(propertyName, value));
        return this;
    }

    /**
     * 获取栏位值"Like"的约束
     *
     * @param propertyName 栏位名称
     * @param value        值
     * @return this
     */
    public Criterions like(String propertyName, Object value) {
        criterions.add(Restrictions.like(propertyName, value));
        return this;
    }

    /**
     * 获取栏位值大于值的约束
     *
     * @param propertyName 栏位名称
     * @param value        值
     * @return this
     */
    public Criterions gt(String propertyName, Object value) {
        criterions.add(Restrictions.gt(propertyName, value));
        return this;
    }

    /**
     * 获取栏位值小于值的约束
     *
     * @param propertyName 栏位名称
     * @param value        值
     * @return this
     */
    public Criterions lt(String propertyName, Object value) {
        criterions.add(Restrictions.lt(propertyName, value));
        return this;
    }

    /**
     * 获取栏位值小于等于值的约束
     *
     * @param propertyName 栏位名称
     * @param value        值
     * @return this
     */
    public Criterions le(String propertyName, Object value) {
        criterions.add(Restrictions.le(propertyName, value));
        return this;
    }

    /**
     * 获取栏位值大于等于值的约束
     *
     * @param propertyName 栏位名称
     * @param value        值
     * @return this
     */
    public Criterions ge(String propertyName, Object value) {
        criterions.add(Restrictions.ge(propertyName, value));
        return this;
    }

    /**
     * 获取栏位值In值的约束
     *
     * @param propertyName 栏位名称
     * @param values       值列表
     * @return this
     * @see Restrictions#in
     */
    public Criterions in(String propertyName, Object[] values) {
        criterions.add(Restrictions.in(propertyName, values));
        return this;
    }

    /**
     * 获取栏位值In值的约束
     *
     * @param propertyName 栏位名称
     * @param values       值列表
     * @return this
     * @see Restrictions#in
     */
    public Criterions in(String propertyName, Collection values) {
        criterions.add(Restrictions.in(propertyName, values.toArray()));
        return this;
    }

    /**
     * 获取栏位值为NULL的约束
     *
     * @param propertyName 栏位名称
     * @return this
     * @see Restrictions#isNull
     */
    public Criterions isNull(String propertyName) {
        criterions.add(Restrictions.isNull(propertyName));
        return this;
    }

    /**
     * 获取栏位值不为NULL的约束
     *
     * @param propertyName 栏位名称
     * @return this
     * @see Restrictions#isNotNull
     */
    public Criterions isNotNull(String propertyName) {
        criterions.add(Restrictions.isNotNull(propertyName));
        return this;
    }

    /**
     * 获取两个栏位值相等的约束
     *
     * @param propertyName      栏位名称
     * @param otherPropertyName 另一个栏位名称
     * @return this
     * @see Restrictions#eqProperty
     */
    public Criterions eqProperty(String propertyName, String otherPropertyName) {
        criterions.add(Restrictions.eqProperty(propertyName, otherPropertyName));
        return this;
    }

    /**
     * 获取两个栏位值不相等的约束
     *
     * @param propertyName      栏位名称
     * @param otherPropertyName 另一个栏位名称
     * @return this
     * @see Restrictions#neProperty
     */
    public Criterions neProperty(String propertyName, String otherPropertyName) {
        criterions.add(Restrictions.neProperty(propertyName, otherPropertyName));
        return this;
    }

    /**
     * 获取一个栏位值小于另一个栏位值的约束
     *
     * @param propertyName      栏位名称
     * @param otherPropertyName 另一个栏位名称
     * @return this
     * @see Restrictions#ltProperty
     */
    public Criterions ltProperty(String propertyName, String otherPropertyName) {
        criterions.add(Restrictions.ltProperty(propertyName, otherPropertyName));
        return this;
    }

    /**
     * 获取一个栏位值小于等于另一个栏位值的约束
     *
     * @param propertyName      栏位名称
     * @param otherPropertyName 另一个栏位名称
     * @return this
     * @see Restrictions#leProperty
     */
    public Criterions leProperty(String propertyName, String otherPropertyName) {
        criterions.add(Restrictions.leProperty(propertyName, otherPropertyName));
        return this;
    }

    /**
     * 获取一个栏位值大于另一个栏位值的约束
     *
     * @param propertyName      栏位名称
     * @param otherPropertyName 另一个栏位名称
     * @return this
     * @see Restrictions#gtProperty
     */
    public Criterions gtProperty(String propertyName, String otherPropertyName) {
        criterions.add(Restrictions.gtProperty(propertyName, otherPropertyName));
        return this;
    }

    /**
     * 获取一个栏位值大于等于另一个栏位值的约束
     *
     * @param propertyName      栏位名称
     * @param otherPropertyName 另一个栏位名称
     * @return this
     * @see Restrictions#geProperty
     */
    public Criterions geProperty(String propertyName, String otherPropertyName) {
        criterions.add(Restrictions.geProperty(propertyName, otherPropertyName));
        return this;
    }


    /**
     * 对某个规则约束进行反向(否定)处理
     *
     * @param expression 要处理的约束
     * @return this
     * @see Restrictions#not
     */
    public Criterions not(Criterion expression) {
        criterions.add(Restrictions.not(expression));
        return this;
    }

    /**
     * 对多个规则约束使用AND连接
     *
     * @param criterions
     * @return this
     * @see Restrictions#and
     */
    public Criterions and(Criterion... criterions) {
        this.criterions.add(Restrictions.and(criterions));
        return this;
    }

    /**
     * 对多个规则约束使用OR连接
     *
     * @param criterions
     * @return this
     * @see Restrictions#or
     */
    public Criterions or(Criterion... criterions) {
        this.criterions.add(Restrictions.or(criterions));
        return this;
    }

}
