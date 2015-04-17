package com.nd.gaea.odata.api.uri;

public enum UriInfoKind {

    /**
     * Class: {@link UriInfoAll}<br>
     * URI: http://.../serviceroot/$all
     */
    all,

    /**
     * Class: {@link UriInfoBatch}<br>
     * URI: http://.../serviceroot/$batch
     */
    batch,

    /**
     * Class: {@link UriInfoCrossjoin}<br>
     * URI: http://.../serviceroot/$crossjoin
     */
    crossjoin,

    /**
     * Class: {@link UriInfoEntityId}<br>
     * URI: http://.../serviceroot/$entity(...)
     */
    entityId,

    /**
     * Class: {@link UriInfoMetadata}<br>
     * URI: http://.../serviceroot/$metadata...
     */
    metadata,

    /**
     * Class: {@link UriInfoResource}<br>
     * URI: http://.../serviceroot/entitySet
     */
    resource,

    /**
     * Class: {@link UriInfoService}<br>
     * URI: http://.../serviceroot
     */
    service;
}
