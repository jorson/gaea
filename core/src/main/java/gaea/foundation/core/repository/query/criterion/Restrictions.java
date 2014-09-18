package gaea.foundation.core.repository.query.criterion;

import java.util.Collection;
import java.util.Map;

/**
 * 限制条件
 *
 * @author wuhy
 */
public class Restrictions {


    /**
     * 获取栏位值相等的约束
     *
     * @param propertyName 栏位名称
     * @param value        值
     * @return SimpleCriterion
     */
    public static SimpleCriterion eq(String propertyName, Object value) {
        return new SimpleCriterion(propertyName, value, Operator.EQ);
    }

    /**
     * 批量添加所有栏位值相等的约束
     *
     * @param propertyNameValues
     * @return
     */
    public static Criterion allEq(Map<String, ?> propertyNameValues) {
        final Conjunction conj = new Conjunction();

        for (Map.Entry<String, ?> entry : propertyNameValues.entrySet()) {
            conj.add(eq(entry.getKey(), entry.getValue()));
        }
        return conj;
    }

    /**
     * 获取栏位值相等的约束，如果值为null，则使用isnull的约束来替代
     *
     * @param propertyName 栏位名称
     * @param value        值
     * @return The Criterion
     * @see #eq
     * @see #isNull
     */
    public static Criterion eqOrIsNull(String propertyName, Object value) {
        return value == null ? isNull(propertyName) : eq(propertyName, value);
    }

    /**
     * 获取栏位值不相等的约束
     *
     * @param propertyName 栏位名称
     * @param value        值
     * @return SimpleCriterion
     */
    public static SimpleCriterion ne(String propertyName, Object value) {
        return new SimpleCriterion(propertyName, value, Operator.NE);
    }

    /**
     * 获取栏位值相等的约束，如果值为null，则使用is not null的约束来替代
     *
     * @param propertyName 栏位名称
     * @param value        值
     * @return The Criterion
     * @see #ne
     * @see #isNotNull
     */
    public static Criterion neOrIsNotNull(String propertyName, Object value) {
        return value == null ? isNotNull(propertyName) : ne(propertyName, value);
    }

    /**
     * 获取栏位值"Like"的约束
     *
     * @param propertyName 栏位名称
     * @param value        值
     * @return SimpleCriterion
     */
    public static SimpleCriterion like(String propertyName, Object value) {
        return new SimpleCriterion(propertyName, value, Operator.LIKE);
    }

    /**
     * 获取栏位值大于值的约束
     *
     * @param propertyName 栏位名称
     * @param value        值
     * @return SimpleCriterion
     */
    public static SimpleCriterion gt(String propertyName, Object value) {
        return new SimpleCriterion(propertyName, value, Operator.GT);
    }

    /**
     * 获取栏位值小于值的约束
     *
     * @param propertyName 栏位名称
     * @param value        值
     * @return SimpleCriterion
     */
    public static SimpleCriterion lt(String propertyName, Object value) {
        return new SimpleCriterion(propertyName, value, Operator.LT);
    }

    /**
     * 获取栏位值大于等于值的约束
     *
     * @param propertyName 栏位名称
     * @param value        值
     * @return SimpleCriterion
     */
    public static SimpleCriterion le(String propertyName, Object value) {
        return new SimpleCriterion(propertyName, value, Operator.LE);
    }

    /**
     * 获取栏位值小于等于值的约束
     *
     * @param propertyName 栏位名称
     * @param value        值
     * @return SimpleCriterion
     */
    public static SimpleCriterion ge(String propertyName, Object value) {
        return new SimpleCriterion(propertyName, value, Operator.GE);
    }

    /**
     * 获取栏位值In值的约束
     *
     * @param propertyName 栏位名称
     * @param values       值列表
     * @return The Criterion
     * @see InCriterion
     */
    public static InCriterion in(String propertyName, Object[] values) {
        return new InCriterion(propertyName, values);
    }

    /**
     * 获取栏位值In值的约束
     *
     * @param propertyName 栏位名称
     * @param values       值列表
     * @return The Criterion
     * @see InCriterion
     */
    public static InCriterion in(String propertyName, Collection values) {
        return new InCriterion(propertyName, values.toArray());
    }

    /**
     * 获取栏位值为NULL的约束
     *
     * @param propertyName 栏位名称
     * @return The Criterion
     * @see NullCriterion
     */
    public static NullCriterion isNull(String propertyName) {
        return new NullCriterion(propertyName);
    }

    /**
     * 获取栏位值不为NULL的约束
     *
     * @param propertyName 栏位名称
     * @return The Criterion
     * @see NotNullCriterion
     */
    public static NotNullCriterion isNotNull(String propertyName) {
        return new NotNullCriterion(propertyName);
    }

    /**
     * 获取两个栏位值相等的约束
     *
     * @param propertyName      栏位名称
     * @param otherPropertyName 另一个栏位名称
     * @return The Criterion
     * @see PropertyCriterion
     */
    public static PropertyCriterion eqProperty(String propertyName, String otherPropertyName) {
        return new PropertyCriterion(propertyName, otherPropertyName, Operator.EQ);
    }

    /**
     * 获取两个栏位值不相等的约束
     *
     * @param propertyName      栏位名称
     * @param otherPropertyName 另一个栏位名称
     * @return The Criterion
     * @see PropertyCriterion
     */
    public static PropertyCriterion neProperty(String propertyName, String otherPropertyName) {
        return new PropertyCriterion(propertyName, otherPropertyName, Operator.NE);
    }

    /**
     * 获取一个栏位值小于另一个栏位值的约束
     *
     * @param propertyName      栏位名称
     * @param otherPropertyName 另一个栏位名称
     * @return The Criterion
     * @see PropertyCriterion
     */
    public static PropertyCriterion ltProperty(String propertyName, String otherPropertyName) {
        return new PropertyCriterion(propertyName, otherPropertyName, Operator.LT);
    }

    /**
     * 获取一个栏位值小于等于另一个栏位值的约束
     *
     * @param propertyName      栏位名称
     * @param otherPropertyName 另一个栏位名称
     * @return The Criterion
     * @see PropertyCriterion
     */
    public static PropertyCriterion leProperty(String propertyName, String otherPropertyName) {
        return new PropertyCriterion(propertyName, otherPropertyName, Operator.LE);
    }

    /**
     * 获取一个栏位值大于另一个栏位值的约束
     *
     * @param propertyName      栏位名称
     * @param otherPropertyName 另一个栏位名称
     * @return The Criterion
     * @see PropertyCriterion
     */
    public static PropertyCriterion gtProperty(String propertyName, String otherPropertyName) {
        return new PropertyCriterion(propertyName, otherPropertyName, Operator.GT);
    }

    /**
     * 获取一个栏位值大于等于另一个栏位值的约束
     *
     * @param propertyName      栏位名称
     * @param otherPropertyName 另一个栏位名称
     * @return The Criterion
     * @see PropertyCriterion
     */
    public static PropertyCriterion geProperty(String propertyName, String otherPropertyName) {
        return new PropertyCriterion(propertyName, otherPropertyName, Operator.GE);
    }


    /**
     * 对某个规则约束进行反向(否定)处理
     *
     * @param expression 要处理的约束
     * @return Criterion
     * @see NotCriterion
     */
    public static Criterion not(Criterion expression) {
        return new NotCriterion(expression);
    }

}