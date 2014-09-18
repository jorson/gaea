package gaea.foundation.core.repository.query.criterion;

import gaea.foundation.core.utils.ArrayUtils;

/**
 * In规则条件
 *
 * @author wuhy
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
    protected InCriterion(String propertyName, Object[] values) {
        this.propertyName = propertyName;
        this.values = values;
    }

    @Override
    public String toString() {
        return propertyName + " in (" + ArrayUtils.join(values) + ')';
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object[] getValues() {
        return values;
    }
}