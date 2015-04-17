package com.nd.gaea.odata.api.uri;

import com.nd.gaea.odata.api.uri.queryoption.expression.Expression;

/**
 * Used to describe an all lambda expression used within an resource path
 * For example: http://.../serviceroot/entityset/all(...)
 */
public interface UriResourceLambdaAll extends UriResourcePartTyped {

    /**
     * @return Name of the lambda variable
     */
    public String getLambdaVariable();

    /**
     * @return Lambda expression
     */
    public Expression getExpression();

}
