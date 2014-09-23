package gaea.platform.security.access;


import gaea.foundation.core.config.SystemConfig;
import gaea.foundation.core.utils.Utils;
import gaea.platform.security.support.SecurityConstant;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AuthenticatedVoter implements AccessDecisionVoter<Object> {

    public boolean supports(ConfigAttribute attribute) {
        if (attribute.getAttribute() != null) {
            return true;
        }
        return false;
    }

    public boolean supports(Class<?> clazz) {
        return ResourceAttribute.class.isAssignableFrom(clazz);
    }

    /**
     * 进行投票
     *
     * @param authentication
     * @param object
     * @param attributes
     * @return
     */
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        int result = ACCESS_ABSTAIN;
        boolean enableAnonymouse = Utils.toBoolean(SystemConfig.Instance.getProperty(SecurityConstant.SECURITY_ENABLE_ANONYMOUSE, ""));
        // 取出登录用户的所有角色
        Collection<GrantedAuthority> authorities = extractAuthorities(authentication);
        for (ConfigAttribute attribute : attributes) {
            if (this.supports(attribute) && this.supports(attribute.getClass())) {
                ResourceAttribute rc = (ResourceAttribute) attribute;
                if (rc.isEnableAnonymous() && enableAnonymouse) {  // 如果该资源允许匿名访问，则直接返回允许访问
                    return ACCESS_GRANTED;
                }
                for (GrantedAuthority authority : authorities) {
                    // 如果角色相同
                    if (rc.getAttribute().equals(authority.getAuthority())) {
                        if (rc.isPermitAccess()) {
                            return ACCESS_GRANTED;
                        } else {
                            return ACCESS_DENIED;
                        }
                    }
                }
            }
        }
        return result;
    }

    protected Collection<GrantedAuthority> extractAuthorities(Authentication authentication) {
        return (Collection<GrantedAuthority>) authentication.getAuthorities();
    }
}
