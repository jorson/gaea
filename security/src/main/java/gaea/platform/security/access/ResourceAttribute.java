package gaea.platform.security.access;

import gaea.platform.security.authorization.Resource;
import org.springframework.security.access.ConfigAttribute;

/**
 * 资源的属性，用于保存符合情况资源的信息
 */
public class ResourceAttribute implements ConfigAttribute, Comparable {
    /**
     * 角色，如果enableAnonymous为true时，该值可以为空
     */
    private String authority;
    /**
     * 取得资源路径
     */
    private String resource;
    /**
     * 该资源串是否允许匿名访问,默认为不允许
     */
    private boolean enableAnonymous = false;
    /**
     * 允许还是禁止
     */
    private boolean permitAccess = true;
    /**
     * 排序号
     */
    private Integer orderIndex;

    // ~ Constructors
    // ===================================================================================================
    public ResourceAttribute(String attr, String res) {
        this.authority = attr;
        this.resource = res;
    }

    public ResourceAttribute(String attr, String res, boolean enableAnonymous, boolean permitAccess) {
        this.resource = res;
        this.enableAnonymous = enableAnonymous;
        this.permitAccess = permitAccess;
        this.authority = attr;
    }

    public ResourceAttribute(String attr, Resource resource) {
        this.enableAnonymous = resource.isEnableAnonymous();
        this.permitAccess = resource.isPermitAccess();
        this.authority = attr;
        this.orderIndex = resource.getOrderIndex();
        this.resource = resource.getResource();
    }

    // ~ Methods
    // ========================================================================================================

    public String getAttribute() {
        return this.authority;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public boolean isEnableAnonymous() {
        return enableAnonymous;
    }

    public void setEnableAnonymous(boolean enableAnonymous) {
        this.enableAnonymous = enableAnonymous;
    }

    public boolean isPermitAccess() {
        return permitAccess;
    }

    public void setPermitAccess(boolean permitAccess) {
        this.permitAccess = permitAccess;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ResourceAttribute) {
            ResourceAttribute attr = (ResourceAttribute) obj;
            return this.getAttribute().equals(attr.getAttribute())
                    && this.isEnableAnonymous() == attr.isEnableAnonymous()
                    && this.isPermitAccess() == attr.isPermitAccess();
        }
        return false;
    }

    public int hashCode() {
        return this.authority.hashCode();
    }

    public String toString() {
        return this.authority;
    }

    public int compareTo(Object obj) {
        int result = 0;
        if (obj != null && obj instanceof ResourceAttribute) {
            ResourceAttribute attr = (ResourceAttribute) obj;
            result = this.getOrderIndex() - attr.getOrderIndex();
        }
        return result;
    }
}
