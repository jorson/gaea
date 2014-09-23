package gaea.platform.security.authorization;


import gaea.foundation.core.bo.EntityObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * ResourceDetails的实现类 resString 资源串， 如Url资源串 /admin/index.jsp, Method资源串
 * com.abc.gaea.platform.security.service.userManager.save 等 resType 资源类型，如URL, METHOD 等 authorities
 * 该资源所拥有的权限
 *
 * @author wuhy
 */
public class Resource extends EntityObject {

    public static final String RESOURCE_TYPE_URL = "URL";
    public static final String RESOURCE_TYPE_EXTENSION = "EXTENSION";
    public static final String RESOURCE_TYPE_OPERATION = "OPERATION";
    /**
     * 资源串
     */
    private String resource;
    /**
     * 类型
     */
    private String resourceType;
    /**
     * 该资源串是否允许匿名访问,默认为不允许
     */
    private boolean enableAnonymous;
    /**
     * 允许还是禁止
     */
    private boolean permitAccess = true;
    /**
     * 描述
     */
    private String description;

    /**
     * 排序
     */
    private Integer orderIndex;

    private GrantedAuthority[] authorities;

    public Resource() {
    }

    public Resource(String resource, String resourceType, GrantedAuthority[] authorities) {
        Assert.notNull(resourceType, "Cannot pass null or empty values to resource type");
        this.resource = resource;
        this.resourceType = resourceType;
        setAuthorities(authorities);
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public GrantedAuthority[] getAuthorities() {
        return authorities;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public void setAuthorities(GrantedAuthority[] authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority array");
        List<GrantedAuthority> ga = new ArrayList<GrantedAuthority>();
        for (int i = 0; i < authorities.length; i++) {
            if (authorities[i] != null) {
                ga.add(authorities[i]);
            }
        }
        this.authorities = ga.toArray(new GrantedAuthority[ga.size()]);
    }

    public int hashCode() {
        int code = 168;
        if (getAuthorities() != null) {
            for (int i = 0; i < getAuthorities().length; i++)
                code *= getAuthorities()[i].hashCode() % 7;
        }
        if (getResource() != null) {
            code *= getResource().hashCode() % 7;
        }
        return code;
    }

    public boolean equals(Object rhs) {
        if (!(rhs instanceof Resource)) {
            return false;
        }
        Resource resauth = (Resource) rhs;
        if (!getResource().equals(resauth.getResource())) {
            return false;
        }
        if (!getResourceType().equals(resauth.getResourceType())) {
            return false;
        }
        if (resauth.getAuthorities().length != getAuthorities().length) {
            return false;
        }
        for (int i = 0; i < getAuthorities().length; i++) {
            if (!getAuthorities()[i].equals(resauth.getAuthorities()[i])) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Resource:\n");
        sb.append("resourceType:" + resourceType);
        sb.append("resource:" + resource);
        sb.append("enableAnonymous:" + enableAnonymous);
        sb.append("permitAccess:" + permitAccess);
        sb.append("authorities:" + authorities);
        return sb.toString();
    }

}
