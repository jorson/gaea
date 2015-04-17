package com.nd.gaea.odata.api.processor;

import com.nd.gaea.odata.api.uri.UriInfoKind;
import com.nd.gaea.odata.api.uri.queryoption.SystemQueryOption;
import com.nd.gaea.odata.api.uri.queryoption.SystemQueryOptionKind;

/**
 * Created by Administrator on 2014-11-20.
 */
public interface QueryOptions {

    public UriInfoKind getQueryKind();

    public <T extends SystemQueryOption> T getQueryOption(SystemQueryOptionKind kind);

    public boolean containQueryOption(SystemQueryOptionKind kind);

    public <T extends SystemQueryOption> void setQueryOption(T option);
}
