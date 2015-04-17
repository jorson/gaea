package com.nd.gaea.odata.api.uri;

import com.nd.gaea.odata.api.uri.queryoption.SystemQueryOption;

import java.util.Collection;

public interface UriInfo extends
        UriInfoService, UriInfoAll, UriInfoBatch, UriInfoCrossjoin,
        UriInfoEntityId, UriInfoMetadata, UriInfoResource {

    public UriInfoKind getKind();

    public UriInfoService asUriInfoService();

    public UriInfoAll asUriInfoAll();

    public UriInfoBatch asUriInfoBatch();

    public UriInfoCrossjoin asUriInfoCrossjoin();

    public UriInfoEntityId asUriInfoEntityId();

    public UriInfoMetadata asUriInfoMetadata();

    public UriInfoResource asUriInfoResource();

    public Collection<SystemQueryOption> getSystemQueryOptions();

}
