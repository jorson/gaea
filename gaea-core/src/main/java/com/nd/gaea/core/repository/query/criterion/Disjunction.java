package com.nd.gaea.core.repository.query.criterion;


/**
 * 使用OR连接的条件约束
 *
 * @author bifeng.liu
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
