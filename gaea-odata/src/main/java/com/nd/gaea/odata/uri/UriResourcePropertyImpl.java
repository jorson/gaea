package com.nd.gaea.odata.uri;

import com.nd.gaea.odata.api.property.SingleProperty;
import com.nd.gaea.odata.api.uri.UriResource;
import com.nd.gaea.odata.api.uri.UriResourceKind;

/**
 * Created by Administrator on 14-11-19.
 */
public class UriResourcePropertyImpl extends UriResourceImpl implements UriResource {

    private SingleProperty property;

    public UriResourcePropertyImpl() {
        super(UriResourceKind.primitiveProperty);
    }

    public UriResourcePropertyImpl setProperty(final SingleProperty property) {
        this.property = property;
        return this;
    }

    public SingleProperty getProperty() {
        return this.property;
    }
}
