package gaea.platform.security.domain;

import gaea.foundation.core.bo.EntityObject;
import gaea.foundation.core.utils.CollectionUtils;
import gaea.foundation.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 一个UserAccess里会存在多个DomainInstanceAccess;</br>
 * 每个DomainInstanceAccess代表该用户在某个域实例的权限集合
 *
 * @author wuhy
 * @date 14-8-25
 */
public class DomainInstanceAccess extends EntityObject {

    //获取或设置域实例的键名
    private String instanceKey;

    //获取或设置域实例的权限项
    private List<String> purviewKeys = new ArrayList<String>();

    //获取或设置域实例的资源项
    private List<String> resourceKeys = new ArrayList<String>();

    //获取或设置用户拥有的角色对象
    private List<String> roleIds = new ArrayList<String>();


    public DomainInstanceAccess() {
    }

    public DomainInstanceAccess(String instanceKey, List<String> purviewKeys, List<String> resourceKeys, Collection<Role> roles) {
        if (StringUtils.isEmpty(instanceKey)) {
            throw new IllegalArgumentException("instanceKey");
        }
        this.instanceKey = instanceKey;
        this.purviewKeys = purviewKeys;
        this.resourceKeys = resourceKeys;
        this.roleIds = new ArrayList<String>();
        for (Role r : roles) {
            if (!r.getInstanceKey().equals(instanceKey)) {
                throw new IllegalArgumentException("给定的角色的权限实例域与权限实例域不一致。");
            }
            roleIds.add(r.getId().toString());
        }
    }

    /**
     * 追加权限
     *
     * @param purviewKey 权限KEY
     */
    public void appendPurivew(String purviewKey) {
        if (!purviewKeys.contains(purviewKey)) {
            purviewKeys.add(purviewKey);
        }
    }

    /**
     * 删除权限
     *
     * @param purviewKey 权限KEY
     */
    public void removePurivew(String purviewKey) {
        this.purviewKeys.remove(purviewKey);
    }

    /**
     * 追加角色
     *
     * @param roleId 角色ID
     */
    public void appendRole(String roleId) {
        if (!roleIds.contains(roleId)) {
            roleIds.add(roleId);
        }
    }

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     */
    public void removeRole(String roleId) {
        roleIds.remove(roleId);
    }

    /**
     * 追加资源
     *
     * @param resourceKey 资源KEY
     */
    public void appendResource(String resourceKey) {
        if (!this.resourceKeys.contains(resourceKey)) {
            this.resourceKeys.add(resourceKey);
        }
    }

    /**
     * 删除资源
     *
     * @param resourceKey 资源KEY
     */
    public void removeResource(String resourceKey) {
        this.resourceKeys.remove(resourceKey);
    }


    public String getInstanceKey() {
        return instanceKey;
    }

    public void setInstanceKey(String instanceKey) {
        this.instanceKey = instanceKey;
    }

    public List<String> getPurviewKeys() {
        return purviewKeys;
    }

    public void setPurviewKeys(List<String> purviewKeys) {
        this.purviewKeys = purviewKeys;
    }

    public List<String> getResourceKeys() {
        return resourceKeys;
    }

    public void setResourceKeys(List<String> resourceKeys) {
        this.resourceKeys = resourceKeys;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DomainInstanceAccess that = (DomainInstanceAccess) o;
        if (!instanceKey.equals(that.instanceKey)) {
            return false;
        }

        if (purviewKeys != null ? !CollectionUtils.isEqualCollection(purviewKeys, that.purviewKeys) : that.purviewKeys != null) {
            return false;
        }
        if (resourceKeys != null ? !CollectionUtils.isEqualCollection(resourceKeys, that.resourceKeys) : that.resourceKeys != null) {
            return false;
        }
        if (roleIds != null ? !CollectionUtils.isEqualCollection(roleIds, that.roleIds) : that.roleIds != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (instanceKey != null ? instanceKey.hashCode() : 0);
        result = 31 * result + (purviewKeys != null ? purviewKeys.hashCode() : 0);
        result = 31 * result + (resourceKeys != null ? resourceKeys.hashCode() : 0);
        result = 31 * result + (roleIds != null ? roleIds.hashCode() : 0);
        return result;
    }
}
