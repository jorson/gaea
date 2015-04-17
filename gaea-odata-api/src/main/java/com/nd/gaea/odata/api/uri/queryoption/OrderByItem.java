package com.nd.gaea.odata.api.uri.queryoption;

import com.nd.gaea.odata.api.uri.queryoption.expression.Expression;

/**
 * Represents a single orderby information
 * For example: http://.../Employees?$orderby=Name
 */
public interface OrderByItem {

    /**
     * Returns the sort order of the orderby item
     *
     * @return if false (default) the sort order is ascending, if true the sort order is descending
     */
    boolean isDescending();

    /**
     * @return Expression which is used to order the items
     */
    Expression getExpression();

}
