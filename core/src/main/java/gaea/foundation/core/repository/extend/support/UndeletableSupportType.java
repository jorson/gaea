package gaea.foundation.core.repository.extend.support;

import gaea.foundation.core.repository.exception.RepositoryException;

import java.lang.reflect.Constructor;

/**
 * 支持审核支持的类型
 *
 * @author wuhy
 */
public enum UndeletableSupportType {

    /**
     * 字符类型
     */
    STRING(String.class, "string"),
    /**
     * 整型
     */
    INT(Integer.class, "int"),
    /**
     * 长整型
     */
    LONG(Long.class, "long"),
    /**
     * 布尔类型
     */
    BOOLEAN(Boolean.class, "boolean");

    private String value;

    private Class clazz;

    UndeletableSupportType(Class clazz, String value) {
        this.clazz = clazz;
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public static UndeletableSupportType getEnum(String type) {
        for (UndeletableSupportType t : UndeletableSupportType.values()) {
            if (type.equalsIgnoreCase(t.value())) {
                return t;
            }
        }
        return null;
    }

    /**
     * 根据类型字符串和值，取得生成对象值
     *
     * @param type
     * @param value
     * @return
     */
    public static Object getTypeInstance(String type, String value) throws RepositoryException {
        UndeletableSupportType supportType = getEnum(type);
        String errorMessage = "deletable entity unsupport this type[" + type + "]";
        if (supportType == null) {
            throw new RepositoryException(errorMessage);
        }
        try {
            Constructor constructor = supportType.clazz.getConstructor(String.class);
            return constructor.newInstance(value);
        } catch (Exception ex) {
            throw new RepositoryException(errorMessage, ex);
        }
    }

}
