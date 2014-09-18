package gaea.foundation.core.repository.query.criterion;


/**
 * Not规则条件
 *
 * @author wuhy
 */
public class NotCriterion implements Criterion {
    private Criterion criterion;

    /**
     * 初始化Not规则条件
     *
     * @param criterion 要否定的规则
     */
    protected NotCriterion(Criterion criterion) {
        this.criterion = criterion;
    }

    @Override
    public String toString() {
        return "not " + criterion.toString();
    }

    public Criterion getCriterion() {
        return criterion;
    }
}
