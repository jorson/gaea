package com.nd.gaea.web.converter.object;

/**
 * 在这里输入标题
 * <p/>
 * 说明
 *
 * @author bifeng.liu
 * @since 2014/12/6
 */
public class SubJsonEntity {
    private String subJsonName;
    private int subJsonCode;

    public SubJsonEntity() {

    }

    public SubJsonEntity(String subJsonName, int subJsonCode) {
        this.subJsonName = subJsonName;
        this.subJsonCode = subJsonCode;
    }

    public String getSubJsonName() {
        return subJsonName;
    }

    public void setSubJsonName(String subJsonName) {
        this.subJsonName = subJsonName;
    }

    public int getSubJsonCode() {
        return subJsonCode;
    }

    public void setSubJsonCode(int subJsonCode) {
        this.subJsonCode = subJsonCode;
    }
}
