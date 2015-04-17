package com.nd.gaea.odata.uri.queryoption;

import com.nd.gaea.odata.api.uri.queryoption.CountOption;
import com.nd.gaea.odata.api.uri.queryoption.SystemQueryOptionKind;

/**
 * Created by Administrator on 2014-11-24.
 */
public class CountOptionImpl extends SystemQueryOptionImpl implements CountOption {

    private boolean count;

    public CountOptionImpl() {
        setKind(SystemQueryOptionKind.COUNT);
    }

    @Override
    public boolean getValue() {
        return count;
    }

    public CountOptionImpl setValue(final boolean count) {
        this.count = count;
        return this;
    }

}
