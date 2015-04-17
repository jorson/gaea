package com.nd.gaea.web.converter.object;

/**
 * 在这里输入标题
 * <p/>
 * 说明
 *
 * @author bifeng.liu
 * @since 2014/12/6
 */
public enum EnumType {
    INT(1),

    STRING(2);

    private int value;

    EnumType(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
