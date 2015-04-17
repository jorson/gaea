package com.nd.gaea.utils.object;

/**
 * Created by ND on 14-4-28.
 */
public class SubTestObject extends TestObject implements ITestObject {
    private String code;

    private String privateVariable = "private";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String doPublicMethod() {
        return "doPublicMethod";
    }

    private String doPrivateMethod() {
        return "doPrivateMethod";
    }

    protected boolean doSubMethod_01(String val, int id) {
        return false;
    }

    public boolean doMethod(TestObject testObject) {
        return true;
    }

    @Override
    public String doInterface(String value) {
        return "interface" + value;
    }
}
