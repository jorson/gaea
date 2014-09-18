package gaea.foundation.core.repository.query.criterion;

/**
 * Null规则条件
 *
 * @author wuhy
 */
public class NullCriterion implements Criterion {
    private final String propertyName;

    /**
     * 初始化Null规则条件
     *
     * @param propertyName 标准名称
     */
    protected NullCriterion(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String toString() {
        return propertyName + " is null";
    }

    public String getPropertyName() {
        return propertyName;
    }
}
