package com.nd.gaea.utils.object;

/**
 * Created by ND on 14-4-28.
 */
public class GrandsonTestObject extends SubTestObject {

    private String value;

    protected boolean doSubMethod_01(String val, int id) {
        return true;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
