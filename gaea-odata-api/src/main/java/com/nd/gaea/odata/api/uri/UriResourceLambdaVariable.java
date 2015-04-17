package com.nd.gaea.odata.api.uri;

/**
 * Used to describe an lambda variable used within an resource path
 * For example: http://.../serviceroot/entityset/listofstring/any(d: 'string' eq d)
 */
public interface UriResourceLambdaVariable extends UriResourcePartTyped {

    /**
     * @return Name of the lambda variable
     */
    public String getVariableName();

}
