package com.nd.gaea.odata.uri.queryoption;

import com.nd.gaea.odata.api.uri.queryoption.SkipTokenOption;
import com.nd.gaea.odata.api.uri.queryoption.SystemQueryOptionKind;

public class SkipTokenOptionImpl extends SystemQueryOptionImpl implements SkipTokenOption {
    private String skipTokenValue;

    public SkipTokenOptionImpl() {
        setKind(SystemQueryOptionKind.SKIPTOKEN);
    }

    @Override
    public String getValue() {
        return skipTokenValue;
    }

    public SkipTokenOptionImpl setValue(final String skipTokenValue) {
        this.skipTokenValue = skipTokenValue;
        return this;
    }

}
