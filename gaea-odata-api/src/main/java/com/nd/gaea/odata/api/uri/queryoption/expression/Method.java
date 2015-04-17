package com.nd.gaea.odata.api.uri.queryoption.expression;

import java.util.List;

/**
 * Represents a method expression in the expression tree
 */
public interface Method extends Expression {

    /**
     * @return The used method
     * @see MethodKind
     */
    public MethodKind getMethod();

    /**
     * @return The list of expression tree which form the actual method parameters
     */
    public List<Expression> getParameters();

}
