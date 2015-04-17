package com.nd.gaea.odata.api.uri;

/**
 * Super interface for all objects representing resource parts.
 * See {@link UriInfoResource} for details.
 */
public interface UriResource {

    /**
     * @return Kind of the resource part
     */
    UriResourceKind getKind();

    @Override
    String toString();

}
