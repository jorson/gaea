package gaea.platform.security.domain;

import gaea.foundation.core.bo.EntityObject;

import java.util.ArrayList;
import java.util.List;

/**
 * User: wuhy
 * Date: 14-5-28
 * Time: 上午11:51
 */
public class UserAccess extends EntityObject {

    /**
     * 匿名用户ID
     */
    public static final String ANONYMOUS_USER_ID = "00000000000000000000000000000000";

    //用户别名
    private String alias;

    //用户所在的权限域对象
    private List<DomainInstanceAccess> accesses = new ArrayList<DomainInstanceAccess>();

    public UserAccess() {
    }

    public UserAccess(long id, String alias) {
        this.id = id;
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<DomainInstanceAccess> getAccesses() {
        return accesses;
    }

    public void setAccesses(List<DomainInstanceAccess> accesses) {
        this.accesses = accesses;
    }

    /**
     * 追加DomainInstanceAccess
     * @param accesse  DomainInstanceAccess
     */
    public void appendAccesse(DomainInstanceAccess accesse) {
        accesses.add(accesse);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserAccess that = (UserAccess) o;

        if (!id.equals(that.id)) {
            return false;
        }
        if (!alias.equals(that.alias)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + alias.hashCode();
        result = 31 * result + (accesses != null ? accesses.hashCode() : 0);
        return result;
    }


}
