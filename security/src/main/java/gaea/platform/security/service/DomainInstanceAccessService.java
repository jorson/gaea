package gaea.platform.security.service;

import gaea.platform.security.domain.DomainInstanceAccess;
import gaea.platform.security.domain.UserAccess;

import java.util.Collection;
import java.util.List;

/**
 * Created by wuhy on 14-6-3.
 */
public interface DomainInstanceAccessService {

    /**
     * 校验是否包含给定的权限
     * @param access     DomainInstanceAccess
     * @param purviewKey The purview key.
     * @return
     */
    public Boolean checkPurview(DomainInstanceAccess access, String purviewKey);

    /**
     * 校验是否包含给定的权限
     * @param access      DomainInstanceAccess
     * @param purviewKeys The purview keys
     * @return
     */
    public List<Boolean> checkPurview(final DomainInstanceAccess access, Collection<String> purviewKeys);

    /**
     * 校验是否包含给定的资源
     * @param access      DomainInstanceAccess
     * @param resourceKey 资源KEY
     * @return
     */
    public Boolean checkResource(DomainInstanceAccess access, String resourceKey);

    /**
     * 校验是否包含给定的资源
     * @param access      DomainInstanceAccess
     * @param resourceKeys 资源KEY
     * @return
     */
    public List<Boolean> checkResource(DomainInstanceAccess access, Collection<String> resourceKeys);

    /**
     * 校验是否包含给定的权限及资源
     * @param access      DomainInstanceAccess
     * @param purviewKey  权限KEY
     * @param resourceKey 资源KEY
     * @return
     */
    public Boolean checkAccess(DomainInstanceAccess access, String purviewKey, String resourceKey);

    /**
     * 检验是否包含给定的角色
     * @param access       DomainInstanceAccess
     * @param roleId       角色ID
     * @return
     */
    public boolean checkRole(DomainInstanceAccess access, String roleId);
}
