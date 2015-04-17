package com.nd.gaea.odata.api.uri;

import com.nd.gaea.odata.api.uri.queryoption.FormatOption;

/**
 * Used for URI info kind {@link UriInfoKind#metadata} to describe URIs like
 * http://.../serviceroot/$metadata...
 */
public interface UriInfoMetadata {

    /**
     * @return Object containing information of the $id option
     */
    public FormatOption getFormatOption();

    /**
     * @return Object containing information of the URI fragment
     */
    public String getFragment();

}
