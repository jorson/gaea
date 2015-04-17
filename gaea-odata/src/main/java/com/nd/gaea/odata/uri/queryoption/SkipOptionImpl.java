package com.nd.gaea.odata.uri.queryoption;

import com.nd.gaea.odata.api.uri.queryoption.SkipOption;
import com.nd.gaea.odata.api.uri.queryoption.SystemQueryOptionKind;

public class SkipOptionImpl extends SystemQueryOptionImpl implements SkipOption {
    private int value;

    public SkipOptionImpl() {
        setKind(SystemQueryOptionKind.SKIP);
    }

    @Override
    public int getValue() {
        return value;
    }

    public SkipOptionImpl setValue(final int value) {
        this.value = value;
        return this;
    }

}
