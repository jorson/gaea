package com.nd.gaea.odata.api.uri;

import com.nd.gaea.odata.api.uri.queryoption.expression.Expression;

/**
 * Used to describe an any lambda expression used within an resource path
 * For example: http://.../serviceroot/entityset/any(...)
 */
public interface UriResourceLambdaAny extends UriResourcePartTyped {

    /**
     * @return Name of the lambda variable
     */
    public String getLamdaVariable();

    /**
     * @return Lambda expression
     */
    public Expression getExpression();

}
