package gaea.platform.security.domain;

import gaea.foundation.core.bo.EntityObject;
import gaea.foundation.core.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * User: wuhy
 * Date: 14-5-27
 * Time: 下午5:19
 */
public class Role extends EntityObject {

    public final static String REGION_INSTANCE_KEY = "instancekey";

    //角色的名称
    private String name;

    //角色的域实例键名
    private String instanceKey;

    //角色的权限KEY
    private List<String> purviewKeys = new ArrayList<String>();

    //角色的资源KEY
    private List<String> resourceKeys = new ArrayList<String>();

    public Role() {
    }

    public Role(String name, String instanceKey, List<String> purviewKeys, List<String> resourceKeys) {
        this.name = name;
        this.instanceKey = instanceKey;
        this.purviewKeys = purviewKeys;
        this.resourceKeys = resourceKeys;
    }

    public List<String> getResourceKeys() {
        return resourceKeys;
    }

    public void setResourceKeys(List<String> resourceKeys) {
        this.resourceKeys = resourceKeys;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Role that = (Role) o;

        if (!id.equals(that.id)) {
            return false;
        }
        if (instanceKey != null ? !instanceKey.equals(that.instanceKey) : that.instanceKey != null) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if (purviewKeys != null ? !CollectionUtils.isEqualCollection(purviewKeys, that.purviewKeys) : that.purviewKeys != null) {
            return false;
        }
        if (resourceKeys != null ? !CollectionUtils.isEqualCollection(resourceKeys, that.resourceKeys) : that.resourceKeys != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (instanceKey != null ? instanceKey.hashCode() : 0);
        result = 31 * result + (purviewKeys != null ? purviewKeys.hashCode() : 0);
        result = 31 * result + (resourceKeys != null ? resourceKeys.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", instanceKey='" + instanceKey + '\'' +
                ", purviewKeys=" + purviewKeys +
                ", resourceKeys=" + resourceKeys +
                '}';
    }
}
