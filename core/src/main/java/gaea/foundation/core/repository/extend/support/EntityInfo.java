package gaea.foundation.core.repository.extend.support;

import gaea.foundation.core.repository.extend.Undeletable;

/**
 * 装载Entity信息的内部类.
 *
 * @author wuhy
 */
public class EntityInfo {
    /**
     * entity是否undeleteable的标志
     */
    private boolean undeletable = false;
    /**
     * 是否可以更新
     */
    private boolean updatable;
    /**
     * 状态的属性名
     */
    private String statusProperty;
    /**
     * 状态的无效值
     */
    private String unvalidValue;
    /**
     * 状态的有效值
     */
    private String validValue;
    /**
     * 状态的类型
     */
    public String statusType;

    public EntityInfo(Class entityClass) {
        init(entityClass);
    }

    public boolean isUndeletable() {
        return undeletable;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public String getStatusProperty() {
        return statusProperty;
    }

    /**
     * 取得状态的无效值，转换成相应的类型
     * 类型由statusType决定，
     * 只支持四种类型string,int,long,boolean
     *
     * @return
     */
    public Object getUnvalidValue() {
        return UndeletableSupportType.getTypeInstance(statusType, unvalidValue);
    }

    public String getStatusType() {
        return statusType;
    }

    /**
     * 初始化数据，判断EntityClass是否UndeletableEntity
     * <p/>
     * 如果是则设置相关的值
     *
     * @param entityClass 实体类
     */
    private void init(Class entityClass) {
        // 通过EntityClass的annotation判断entity是否undeletable
        if (entityClass.isAnnotationPresent(Undeletable.class)) {
            undeletable = true;
            Undeletable anno = (Undeletable) entityClass.getAnnotation(Undeletable.class);
            updatable = anno.updatable();
            statusProperty = anno.property();
            unvalidValue = anno.unvalid();
            validValue = anno.valid();
            statusType = anno.type();
        }
    }
}
