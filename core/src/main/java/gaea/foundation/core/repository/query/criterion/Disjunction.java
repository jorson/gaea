package gaea.foundation.core.repository.query.criterion;


/**
 * 使用And连接的条件约束
 *
 * @author wuhy
 * @see Conjunction
 */
public class Disjunction extends Junction {
    /**
     * Constructs a Disjunction
     */
    public Disjunction() {
        super(Nature.OR);
    }

    public Disjunction(Criterion... conditions) {
        super(Nature.OR, conditions);
    }
}
