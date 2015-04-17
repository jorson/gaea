package com.nd.gaea.core.repository.query.criterion;

import com.nd.gaea.utils.StringUtils;

/**
 * In规则条件
 *
 * @author bifeng.liu
 */
public class InCriterion implements Criterion {

    private final String propertyName;
    private final Object[] values;

    /**
     * 初始化In规则条件
     *
     * @param propertyName 栏位名称
     * @param values       值
     */
    public InCriterion(String propertyName, Object[] values) {
        this.propertyName = propertyName;
        this.values = values;
    }

    @Override
    public String toString() {
        return propertyName + " in (" + StringUtils.join(values, ",") + ')';
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object[] getValues() {
        return values;
    }
}