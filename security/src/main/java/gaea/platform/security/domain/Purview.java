package gaea.platform.security.domain;


import gaea.foundation.core.bo.EntityObject;

/**
 * 权限
 * User: wuhy
 * Date: 14-5-28
 * Time: 上午11:42
 */
public class Purview extends EntityObject {

    public Purview() {
    }

    public Purview(String id, String name, Domain domain) {
        this.id = id;
        this.name = name;
        this.domainKey = domain.getId().toString();
    }

    //权限项所属的域键名
    private String domainKey;

    //权限项的名称
    private String name;

    //是否父权限
    private boolean isParent;

    //父权限的KEY
    private String parent;

    public String getDomainKey() {
        return domainKey;
    }

    public void setDomainKey(String domainKey) {
        this.domainKey = domainKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Purview purview = (Purview) o;

        if (domainKey != null ? !domainKey.equals(purview.domainKey) : purview.domainKey != null) {
            return false;
        }
        if (!id.equals(purview.id)) {
            return false;
        }
        if (name != null ? !name.equals(purview.name) : purview.name != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (domainKey != null ? domainKey.hashCode() : 0);
        //result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Purview{" +
                "id='" + id + '\'' +
                ", domainKey='" + domainKey + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
