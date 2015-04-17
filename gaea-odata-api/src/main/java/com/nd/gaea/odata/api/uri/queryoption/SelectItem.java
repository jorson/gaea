package com.nd.gaea.odata.api.uri.queryoption;

import com.nd.gaea.odata.api.uri.UriInfoResource;

import java.lang.reflect.Type;

/**
 * Represents a single select item information
 * For example: http://.../Employees?select=name,age
 */
public interface SelectItem {

    /**
     * @return A star is used as select item
     */
    boolean isStar();

    /**
     * @return Namespace and star is used as select item in order to select operations
     */
    boolean isAllOperationsInSchema();

    UriInfoResource getResourcePath();

    /**
     * @return Before resource path segments which should be selected a type filter may be used.
     * For example: ...Suppliers?$select=Namespace.PreferredSupplier/AccountRepresentative
     */
    Type getStartTypeFilter();

}
