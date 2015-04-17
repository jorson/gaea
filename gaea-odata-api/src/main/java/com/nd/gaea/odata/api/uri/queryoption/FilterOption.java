package com.nd.gaea.odata.api.uri.queryoption;


import com.nd.gaea.odata.api.uri.queryoption.expression.Expression;

public interface FilterOption extends SystemQueryOption {

    /**
     * @return Expression tree created from the filter value (see {@link com.nd.gaea.core.odata.api.uri.queryoption.expression.Expression})
     */
    Expression getExpression();
}
