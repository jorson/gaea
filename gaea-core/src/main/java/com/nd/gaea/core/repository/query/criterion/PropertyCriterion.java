package com.nd.gaea.core.repository.query.criterion;

/**
 * 属性规则条件约束
 *
 * @author bifeng.liu
 */
public class PropertyCriterion implements Criterion {

    private final String propertyName;
    private final String otherPropertyName;
    private final Operator operator;

    protected PropertyCriterion(String propertyName, String otherPropertyName, Operator operator) {
        this.propertyName = propertyName;
        this.otherPropertyName = otherPropertyName;
        this.operator = operator;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getOtherPropertyName() {
        return otherPropertyName;
    }

    public Operator getOperator() {
        return operator;
    }
}
