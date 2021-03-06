package com.nd.gaea.core.repository.query.criterion;

/**
 * 请在这里输入说明
 *
 * @author bifeng.liu
 */
public enum Operator {
    EQ("eq"),
    NE("ne"),
    LIKE("like"),
    LT("lt"),
    GT("gt"),
    LE("le"),
    GE("ge");

    private String value;

    Operator(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
