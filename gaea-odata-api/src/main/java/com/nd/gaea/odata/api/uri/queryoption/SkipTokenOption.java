package com.nd.gaea.odata.api.uri.queryoption;

/**
 * Represents the system query option $skiptoken
 * For example: http://.../entitySet?$skiptoken=abv
 */
public interface SkipTokenOption extends SystemQueryOption {

    /**
     * @return Value of $skiptoken
     */
    String getValue();

}
