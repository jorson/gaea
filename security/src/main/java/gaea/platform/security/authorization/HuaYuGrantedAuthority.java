package gaea.platform.security.authorization;

import gaea.platform.security.domain.Role;
import org.springframework.security.core.GrantedAuthority;

/**
 * 实现Spring Security框架的GrantedAuthority接口；
 * 返回角色名称作为autority
 */
public class HuaYuGrantedAuthority implements GrantedAuthority {

    private String autority;

    public HuaYuGrantedAuthority(Role role) {
        this.autority = role.getId().toString();
    }


    @Override
    public String getAuthority() {
        return autority;
    }

}