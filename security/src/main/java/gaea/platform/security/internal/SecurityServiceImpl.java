package gaea.platform.security.internal;

import gaea.foundation.core.utils.CollectionUtils;
import gaea.platform.security.AuthenticationService;
import gaea.platform.security.SecurityService;
import gaea.platform.security.access.User;
import gaea.platform.security.authorization.HuaYuGrantedAuthority;
import gaea.platform.security.authorization.Resource;
import gaea.platform.security.context.UserContext;
import gaea.platform.security.domain.Role;
import gaea.platform.security.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 默认的安全服务类
 *
 * @author wuhy
 */
@Service("securityService")
public class SecurityServiceImpl implements SecurityService, AuthenticationService {

    @Autowired
    private RoleService roleService;

    private static final String URL_RESOURCE_PREFIX = "url:";

    @Override
    public List<Resource> getResourcesByType(String resType) {
        return null;
    }

    @Override
    public List<Resource> getResources() {
        // Map<URL_PATTERN,List<GrantedAuthority>>
        Map<String, List<GrantedAuthority>> resourceMap = new HashMap<String, List<GrantedAuthority>>();

        // 转换数据结构为：权限(KEY) - 角色(VALUE)
        List<Role> roles = roleService.findAll();
        if (CollectionUtils.isEmpty(roles)) {
            return Collections.emptyList();
        }
        for (Role role : roles) {
            for (String resourceKey : role.getResourceKeys()) {
                if (resourceKey.startsWith(URL_RESOURCE_PREFIX)) {
                    List<GrantedAuthority> list = resourceMap.get(resourceKey);
                    if (CollectionUtils.isEmpty(list)) {
                        list = new ArrayList<GrantedAuthority>();
                        resourceMap.put(resourceKey, list);
                    }
                    list.add(new HuaYuGrantedAuthority(role));
                }
            }
        }

        Iterator<Map.Entry<String, List<GrantedAuthority>>> iterator = resourceMap.entrySet().iterator();
        Map.Entry<String, List<GrantedAuthority>> entry = null;
        List<Resource> resources = new ArrayList<Resource>(resourceMap.size());
        while (iterator.hasNext()) {
            entry = iterator.next();
            resources.add(new Resource(
                    getUrlPattern(entry.getKey()),
                    Resource.RESOURCE_TYPE_URL,
                    entry.getValue().toArray(new GrantedAuthority[]{})));
        }
        return resources;
    }

    /**
     * 获取URL PATTERN
     *
     * @param resourceKey
     * @return
     */
    private String getUrlPattern(String resourceKey) {
        return resourceKey.substring(URL_RESOURCE_PREFIX.length(), resourceKey.length());
    }

    @Override
    public User getUser(String username) {
        return UserContext.getCurrentUser();
    }
}
