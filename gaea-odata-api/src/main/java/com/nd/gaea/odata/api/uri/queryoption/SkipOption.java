package com.nd.gaea.odata.api.uri.queryoption;

/**
 * Represents the system query option $skip
 * For example: http://.../entitySet?$skip=10
 */
public interface SkipOption extends SystemQueryOption {

    /**
     * @return Value of $skip
     */
    int getValue();

}
