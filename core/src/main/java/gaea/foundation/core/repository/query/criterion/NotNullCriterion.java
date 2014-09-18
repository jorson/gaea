package gaea.foundation.core.repository.query.criterion;

/**
 * Null规则条件约束
 *
 * @author wuhy
 */
public class NotNullCriterion implements Criterion {
    private final String propertyName;

    /**
     * 初始化Null规则条件
     *
     * @param propertyName 标准名称
     */
    protected NotNullCriterion(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String toString() {
        return propertyName + " is not null";
    }

    public String getPropertyName() {
        return propertyName;
    }
}
