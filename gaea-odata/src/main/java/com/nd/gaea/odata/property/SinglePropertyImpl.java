package com.nd.gaea.odata.property;

import com.nd.gaea.odata.api.property.SingleProperty;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Administrator on 14-11-19.
 */
public class SinglePropertyImpl implements SingleProperty {

    private String propertyName;

    public SinglePropertyImpl(String propertyName) {
        if(StringUtils.isEmpty(propertyName)) {
            throw new IllegalArgumentException("property name can't empty");
        }
        this.propertyName = propertyName;
    }

    @Override
    public String getName() {
        return propertyName;
    }
}
