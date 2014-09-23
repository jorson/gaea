package gaea.platform.security.utils;

import gaea.platform.security.access.ResourceAttribute;
import gaea.platform.security.authorization.Resource;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AuthenticationUtils {

    private AuthenticationUtils(){

    }

    /**
     * 根据Resource添加Attribute
     *
     * @param attributes
     * @param resource
     * @param enableAnonymous
     */
    public static void addAttributes(Collection<ConfigAttribute> attributes, Resource resource, boolean enableAnonymous) {
        if (attributes == null){
            attributes = new ArrayList<ConfigAttribute>();
        }
        if (resource != null) {
            if (resource.getAuthorities() != null) {
                GrantedAuthority[] authorities = resource.getAuthorities();
                for (int i = 0; i < authorities.length; i++) {
                    GrantedAuthority auth = authorities[i];
                    addAttribute(attributes, resource, auth.getAuthority());
                }
            } else {
                addAttribute(attributes, resource, "");
            }
        }
    }

    /**
     * 添加资源权限
     *
     * @param attributes
     * @param resource
     * @param attr
     */
    private static void addAttribute(Collection<ConfigAttribute> attributes, Resource resource, String attr) {
        if (resource != null) {
            ResourceAttribute attrObj = new ResourceAttribute(attr, resource);
            if (!attributes.contains(attrObj)) {
                attributes.add(attrObj);
            }
        }
    }

}
