package com.nd.gaea.odata.api.uri;

import com.nd.gaea.odata.api.uri.queryoption.*;

import java.util.List;

/**
 * Used for URI info kind {@link UriInfoKind#resource} to describe URIs like
 * E.g. http://.../serviceroot/entitySet
 */
public interface UriInfoResource {

    /**
     * @return List of custom query options used in the URI
     */
    List<CustomQueryOption> getCustomQueryOptions();

    /**
     * @return Object containing information of the $expand option
     */
    ExpandOption getExpandOption();

    /**
     * @return Object containing information of the $filter option
     */
    FilterOption getFilterOption();

    /**
     * @return Object containing information of the $format option
     */
    FormatOption getFormatOption();

    /**
     * @return Object containing information of the $id option
     */
    IdOption getIdOption();

    /**
     * @return Object containing information of the $count option
     */
    CountOption getCountOption();

    /**
     * @return Object containing information of the $orderby option
     */
    OrderByOption getOrderByOption();

    /**
     * @return Object containing information of the $search option
     */
    SearchOption getSearchOption();

    /**
     * @return Object containing information of the $select option
     */
    SelectOption getSelectOption();

    /**
     * @return Object containing information of the $skip option
     */
    SkipOption getSkipOption();

    /**
     * @return Object containing information of the $skiptoken option
     */
    SkipTokenOption getSkipTokenOption();

    /**
     * @return Object containing information of the $top option
     */
    TopOption getTopOption();


    List<UriResource> getUriResourceParts();

    /**
     * @param alias
     * @return the value for the given alias or null if no value is defined
     */
    String getValueForAlias(String alias);

}
