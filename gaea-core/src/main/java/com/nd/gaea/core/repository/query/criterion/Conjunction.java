package com.nd.gaea.core.repository.query.criterion;

/**
 * 使用And连接的条件约束
 *
 * @author bifeng.liu
 * @see Disjunction
 */
public class Conjunction extends Junction {
    /**
     * Constructs a Conjunction
     */
    public Conjunction() {
        super(Nature.AND);
    }

    public Conjunction(Criterion... criterion) {
        super(Nature.AND, criterion);
    }
}