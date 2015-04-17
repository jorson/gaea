package com.nd.gaea.odata.api.uri.queryoption;

/**
 * Represents the system query option $top
 * For example: http://.../entitySet?$top=10
 */
public interface TopOption extends SystemQueryOption {

    /**
     * @return Value of $top
     */
    int getValue();

}
