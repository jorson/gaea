package com.nd.gaea.odata.api.uri;

/**
 * Class indicating the $root reference. $root may be used within expressions to
 * refer to the current OData service
 * For example: http://.../serviceroot/entityset(1)?$filter=property eq $root/singleton/configstring
 */
public interface UriResourceRoot extends UriResource {

}
