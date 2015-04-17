package com.nd.gaea.odata.api.uri.queryoption;

import com.nd.gaea.odata.api.uri.queryoption.expression.Expression;

/**
 * Represents an alias value defined as query option
 * For example:
 * http://.../?filter=@value eq name&@value='test'
 */
public interface AliasQueryOption extends QueryOption {

    /**
     * @return Value of the alias
     */
    public Expression getValue();

}
