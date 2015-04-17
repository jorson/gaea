package com.nd.gaea.odata.api.uri.queryoption.expression;

/**
 * Represents a unary expression node in the expression tree
 * <br>
 * A binary expression node is inserted in the expression tree for any valid
 * ODATA unary operator in {@link UnaryOperatorKind}
 */
public interface Unary extends Expression {

    /**
     * @return The used binary operator
     * @see UnaryOperatorKind
     */
    public Expression getOperand();

    /**
     * @return Expression sub tree to which the operator applies
     */
    public UnaryOperatorKind getOperator();

}
