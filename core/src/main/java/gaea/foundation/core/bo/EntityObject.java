package gaea.foundation.core.bo;


import gaea.foundation.core.utils.ClassUtils;

import java.io.Serializable;

/**
 * 实体抽象类
 * <p/>
 * 该类作为所有实体类的父类
 *
 * @author wuhy
 */
public abstract class EntityObject implements Serializable {

    /**
     * ID
     */
    protected Object id;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String toString() {
        return ClassUtils.getShortName(getClass()) + ": id=" + getId();
    }

    /**
     * 表示两个实体类是否相等，只有两个ID同时有值、相等并且两个实体类型一致，返回TRUE
     * <p/>
     *
     * @see Object#equals(Object)
     */
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || !this.getClass().equals(other.getClass())) {
            return false;
        }
        EntityObject entity = (EntityObject) other;
        if (this.getId() == null || entity.getId() == null) {
            return false;
        }
        return this.getId().equals(entity.getId());
    }

    /**
     * 使用ID来建立Hash Code，如果ID号不存在，则使用Object.hashCode()来创建 。
     *
     * @see Object#hashCode()
     */
    public int hashCode() {
        if (id == null) {
            return super.hashCode();
        }
        return 39 + 87 * id.hashCode();
    }

}
