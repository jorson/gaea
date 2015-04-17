package com.nd.gaea.odata.api.uri.queryoption;

/**
 * Super interface used for any system query option
 */
public interface SystemQueryOption extends QueryOption {

    /**
     * @return Kind of system query option
     */
    SystemQueryOptionKind getKind();
}
