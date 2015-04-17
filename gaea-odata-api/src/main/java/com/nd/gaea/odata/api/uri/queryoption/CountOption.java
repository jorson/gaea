package com.nd.gaea.odata.api.uri.queryoption;

/**
 * Represents the system query option $count
 * For example:
 * http://.../entitySet?$count=true
 */
public interface CountOption extends SystemQueryOption {

    /**
     * @return Value of $count
     */
    boolean getValue();

}
