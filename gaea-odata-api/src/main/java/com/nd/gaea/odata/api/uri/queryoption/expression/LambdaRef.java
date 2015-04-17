package com.nd.gaea.odata.api.uri.queryoption.expression;

/**
 * Used to within a lambda expression tree to define an access to the lambda variable
 */
public interface LambdaRef extends Expression {

    /**
     * @return Name of the lambda variable
     */
    public String getVariableName();

}
