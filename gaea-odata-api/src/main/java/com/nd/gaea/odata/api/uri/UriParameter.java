package com.nd.gaea.odata.api.uri;

import com.nd.gaea.odata.api.uri.queryoption.expression.Expression;

/**
 * Represents an function parameter or key predicate when used in the URI.
 */
public interface UriParameter {

    /**
     * @return Alias name if the parameters values is an alias, otherwise null
     */
    public String getAlias();

    /**
     * @return Text of the parameters value
     */
    public String getText();

    /**
     * @return If the parameters value is a expression and expression is returned, otherwise null
     */
    public Expression getExpression();

    /**
     * @return Name of the parameter
     */
    public String getName();

    /**
     * @return Name of the referenced property when referential constrains are used
     */
    public String getReferencedProperty();

}
