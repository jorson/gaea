package com.nd.gaea.odata.api.uri.queryoption;

/**
 * Super interface used for any query option
 */
public interface QueryOption {

    /**
     * @return Name of query option
     */
    public String getName();

    /**
     * @return Value of query option
     */
    public String getText();

}
