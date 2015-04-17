package com.nd.gaea.odata.api.uri.queryoption.expression;

/**
 * Represents a literal expression node in the expression tree
 */
public interface Literal extends Expression {

    /**
     * @return Literal
     */
    public String getText();

    /**
     * @return Type of the literal if detected
     */
    public Class<?> getType();

}
