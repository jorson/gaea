package com.nd.gaea.odata.api.uri.queryoption;

/**
 * Represents the system query option $format
 * For example: http://.../entitySet?$format=json
 */
public interface FormatOption extends SystemQueryOption {

    // TODO planned: define best representation for format to enable user defined formats
    String getFormat();
}
