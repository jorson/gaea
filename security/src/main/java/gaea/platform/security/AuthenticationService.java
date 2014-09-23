package gaea.platform.security;

import gaea.platform.security.authorization.Resource;

import java.util.List;

/**
 * Created by jsc on 14-6-4.
 */
public interface AuthenticationService {

    /**
     * 根据资源类型取得资源
     *
     * @param resType 资源类型
     * @return
     */
    public List<Resource> getResourcesByType(String resType);

    /**
     * 取得所有的资源
     *
     * @return
     */
    public List<Resource> getResources();
}
