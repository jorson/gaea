package gaea.platform.security.domain;

import gaea.foundation.core.bo.EntityObject;

/**
 * 域
 * User: wuhy
 * Date: 14-5-28
 * Time: 上午11:43
 */
public class Domain extends EntityObject {


    public Domain() {
    }

    private String name; //域的名称

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Domain domain = (Domain) o;

        if (!id.equals(domain.id)){
            return false;
        }
        if (!name.equals(domain.name)){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Domain{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
