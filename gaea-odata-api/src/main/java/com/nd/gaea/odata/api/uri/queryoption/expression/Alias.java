package com.nd.gaea.odata.api.uri.queryoption.expression;

/**
 * Represents an alias expression node in the expression tree
 * <br>
 * A alias expression node is inserted in the expression tree for any valid alias<br>
 * E.g. $filter=name eq @alias
 */
public interface Alias extends Expression {

    /**
     * @return Name of the used alias
     */
    public String getParameterName();

}
