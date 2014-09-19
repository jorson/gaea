package gaea.foundation.core.utils.object;

/**
 * 请在这里输入说明
 *
 * @author wuhy
 */
public enum EnumObject {
    /**
     * 字符类型
     */
    STRING("string"),
    /**
     * 整型
     */
    INT("int"),
    /**
     * 长整型
     */
    LONG("long");


    private String value;

    EnumObject(String value) {

        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public static EnumObject getEnum(String type) {
        for (EnumObject t : EnumObject.values()) {
            if (type.equalsIgnoreCase(t.value())) {
                return t;
            }
        }
        return null;
    }
}

