package com.nd.gaea.repository.hibernate.mapping.model.collections;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jorson on 2015/3/20.
 */
public class AttributeLayeredValues implements Serializable {

    private final Map<String, LayeredValues> inner;

    public AttributeLayeredValues() {
        inner = new HashMap<String, LayeredValues>();
    }

    public LayeredValues getLayeredValues(String attribute) {
        ensureValueExists(attribute);
        return inner.get(attribute);
    }

    private void ensureValueExists(String attribute) {
        if(!inner.containsKey(attribute)) {
            inner.put(attribute, new LayeredValues());
        }
    }

    public void copyTo(AttributeLayeredValues other) {
        for(Map.Entry<String, LayeredValues> layeredValues : inner.entrySet()) {
            other.inner.put(layeredValues.getKey(), layeredValues.getValue());
        }
    }
}
