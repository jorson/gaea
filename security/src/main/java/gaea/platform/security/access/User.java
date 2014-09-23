package gaea.platform.security.access;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by jsc on 14-6-3.
 */
public class User implements Serializable, UserDetails {

    // 用户ID (ID=0为匿名用户)
    private String id;

    // 用户名
    private String name;

    // 密码
    private String password;

    // accessToken
    private String accessToken;

    // 上次登录时间
    private Date lastLoginTime;

    // 实例的权限项
    private List<String> purviewKeys;

    // 域实例的资源项
    private List<String> resourceKeys;

    // 通过角色生成的GrantedAuthority
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * 用户认证信息
     */
    private AccessGrant accessGrant;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public AccessGrant getAccessGrant() {
        return accessGrant;
    }

    public void setAccessGrant(AccessGrant accessGrant) {
        this.accessGrant = accessGrant;
        if(accessGrant != null){
            this.accessToken = accessGrant.getAccessToken();
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
