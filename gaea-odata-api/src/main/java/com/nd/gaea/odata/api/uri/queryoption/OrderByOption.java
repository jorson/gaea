package com.nd.gaea.odata.api.uri.queryoption;

import java.util.List;

/**
 * Represents the system query option $orderby
 * For example: http://.../Employees?$orderby=Name, Age desc
 */
public interface OrderByOption extends SystemQueryOption {

    /**
     * @return List of single orders used in $orderby
     */
    List<OrderByItem> getOrders();

}
