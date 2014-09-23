package gaea.platform.security.support;

import gaea.foundation.core.config.SystemConfig;
import gaea.foundation.core.utils.Utils;
import gaea.platform.security.AuthenticationService;
import gaea.platform.security.ResourceDefinitionResolver;
import gaea.platform.security.access.ResourceAttribute;
import gaea.platform.security.authorization.Resource;
import gaea.platform.security.internal.ResourceDefinitionResolverImpl;
import gaea.platform.security.utils.AuthenticationUtils;
import org.apache.log4j.Logger;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 获取用户权限
 * @author wuhy
 */
@Component
public class UserSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private static final Logger logger = Logger.getLogger(UserSecurityMetadataSource.class);
    public static final String CONFIG_ATTRIBUTE_CACHE_KEY = "aframe.config.attribute.cache";

    /**
     * 缓存
     */
    private static Map<String, List<ConfigAttribute>> cache = new HashMap<String, List<ConfigAttribute>>();

    @javax.annotation.Resource(name="securityService")
    private AuthenticationService authenticationService;

    private ResourceDefinitionResolver resourceDefinitionResolver = new ResourceDefinitionResolverImpl();

    public Collection<ConfigAttribute> getAttributes(Object filter) throws IllegalArgumentException {
        logger.debug("invoke method: getAttributes ");
        List<ConfigAttribute> results = new ArrayList<ConfigAttribute>();
        FilterInvocation filterInvocation = (FilterInvocation) filter;
        String url = filterInvocation.getRequestUrl();
        Collection<ConfigAttribute> attributes = getAllConfigAttributes();
        // 取得符合模块下所有的角色和用户
        for (ConfigAttribute attribute : attributes) {
            ResourceAttribute resourceAttribute = (ResourceAttribute) attribute;
            //以首先匹配成功的资源的权限作为当前Url的权限
            if (resourceDefinitionResolver.isResourceMatch(url, resourceAttribute.getResource())) {
                results.add(attribute);
            }
        }
        return results;
    }

    /**
     * 取得所有的配置属性
     *
     * @return
     */
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        List<ConfigAttribute> attributes = cache.get(CONFIG_ATTRIBUTE_CACHE_KEY);
        if (attributes == null) {
            attributes = new ArrayList<ConfigAttribute>();
            boolean enableAnonymouse = Utils.toBoolean(SystemConfig.Instance.getProperty(SecurityConstant.SECURITY_ENABLE_ANONYMOUSE, "true"));
            //取得所有的资源
            List<Resource> resources = authenticationService.getResources();
            for (Resource resource : resources) {
                AuthenticationUtils.addAttributes(attributes, resource, enableAnonymouse);
            }
            cache.put(CONFIG_ATTRIBUTE_CACHE_KEY, attributes);
        }
        return attributes;
    }


    public boolean supports(Class clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}
