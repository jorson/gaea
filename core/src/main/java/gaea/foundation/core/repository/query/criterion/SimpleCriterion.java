package gaea.foundation.core.repository.query.criterion;

/**
 * 简单的规则条件约束
 *
 * @author wuhy
 */
public class SimpleCriterion implements Criterion {
    private final String propertyName;
    private final Object value;
    private boolean ignoreCase;
    private final Operator operator;

    protected SimpleCriterion(String propertyName, Object value, Operator operator) {
        this.propertyName = propertyName;
        this.value = value;
        this.operator = operator;
    }

    protected SimpleCriterion(String propertyName, Object value, Operator operator, boolean ignoreCase) {
        this.propertyName = propertyName;
        this.value = value;
        this.ignoreCase = ignoreCase;
        this.operator = operator;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object getValue() {
        return value;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public Operator getOperator() {
        return operator;
    }
}
