package com.nd.gaea.odata.api.uri.queryoption.expression;

import java.lang.reflect.Type;

/**
 * Represents a type literal expression in the expression tree
 */
public interface TypeLiteral extends Expression {

    /**
     * @return Type defined by the type literal
     */
    public Type getType();

}
