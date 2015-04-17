package com.nd.gaea.odata.api.uri;

import com.nd.gaea.odata.api.uri.queryoption.*;

import java.util.List;

public interface UriInfoEntityId {

    public List<CustomQueryOption> getCustomQueryOptions();

    public ExpandOption getExpandOption();

    public FormatOption getFormatOption();

    public IdOption getIdOption();

    public SelectOption getSelectOption();

}
