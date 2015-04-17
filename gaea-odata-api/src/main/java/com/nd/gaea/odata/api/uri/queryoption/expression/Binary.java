package com.nd.gaea.odata.api.uri.queryoption.expression;

/**
 * Represents a binary expression node in the expression tree
 * <br>
 * A binary expression node is inserted in the expression tree for any valid
 * ODATA binary operator in {@link BinaryOperatorKind}.
 */
public interface Binary extends Expression {

    /**
     * @return binary operator kind
     * @see BinaryOperatorKind
     */
    public BinaryOperatorKind getOperator();

    /**
     * @return Expression sub tree of the left operand
     */
    public Expression getLeftOperand();

    /**
     * @return Expression sub tree of the right operand
     */
    public Expression getRightOperand();

}
