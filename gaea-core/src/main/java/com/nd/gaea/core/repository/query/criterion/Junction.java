package com.nd.gaea.core.repository.query.criterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 请在这里输入说明
 *
 * @author bifeng.liu
 */
public class Junction implements Criterion {

    private final Nature nature;
    private final List<Criterion> conditions = new ArrayList<Criterion>();

    public Junction(Nature nature) {
        this.nature = nature;
    }

    public Junction(Nature nature, Criterion... criterion) {
        this(nature);
        Collections.addAll(conditions, criterion);
    }

    /**
     * 添加一个条件约束
     *
     * @param criterion
     * @return {@code this}
     */
    public Junction add(Criterion criterion) {
        conditions.add(criterion);
        return this;
    }

    public Nature getNature() {
        return nature;
    }

    /**
     * 取得连接中所有条件约束
     *
     * @return the criterion
     */
    public List<Criterion> conditions() {
        return conditions;
    }

    /**
     * 连接类型
     */
    public static enum Nature {
        /**
         * An AND
         */
        AND,
        /**
         * An OR
         */
        OR;
    }
}
