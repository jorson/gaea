package com.nd.gaea.odata.uri.queryoption;

import com.nd.gaea.odata.api.uri.queryoption.SystemQueryOptionKind;
import com.nd.gaea.odata.api.uri.queryoption.TopOption;

public class TopOptionImpl extends SystemQueryOptionImpl implements TopOption {
    private int value;

    public TopOptionImpl() {
        setKind(SystemQueryOptionKind.TOP);
    }

    @Override
    public int getValue() {
        return value;
    }

    public TopOptionImpl setValue(final int value) {
        this.value = value;
        return this;
    }

}
