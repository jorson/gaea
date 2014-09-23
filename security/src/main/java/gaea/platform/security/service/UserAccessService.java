package gaea.platform.security.service;

import gaea.foundation.core.repository.query.QuerySupport;
import gaea.foundation.core.repository.support.Pager;
import gaea.foundation.core.service.EntityService;
import gaea.platform.security.access.User;
import gaea.platform.security.domain.DomainInstanceAccess;
import gaea.platform.security.domain.Role;
import gaea.platform.security.domain.UserAccess;

import java.util.Collection;
import java.util.List;

/**
 * User: wuhy
 * Date: 14-5-30
 * Time: 下午4:13
 */
public interface UserAccessService extends EntityService<UserAccess> {

    /**
     * 查询User对象的权限并加入User
     * @param user
     */
    public void addUserAccessToUser(User user);


    /**
     * 校验是否包含给定的权限
     *
     * @param userAccess  用户访问权限
     * @param instanceKey 域实例的KEY
     * @param purviewKey  权限的KEY
     * @return true:包含
     */
    public boolean checkPurview(UserAccess userAccess,
                                String instanceKey,
                                String purviewKey);

    /**
     * 校验是否包含给定的权限
     *
     * @param instanceKey 域实例的KEY
     * @param purviewKeys 权限的KEY
     * @return true:包含
     */
    public List<Boolean> checkPurviews(final UserAccess userAccess,
                                       final String instanceKey,
                                       Collection<String> purviewKeys);

    /**
     * 校验是否包含给定的资源
     *
     * @param instanceKey 域实例的KEY
     * @param resourceKey 资源的KEY
     * @return true:包含
     */
    public boolean checkResource(final UserAccess userAccess,
                                 String instanceKey,
                                 String resourceKey);

    /**
     * 校验是否包含给定的资源
     *
     * @param instanceKey  域实例的KEY
     * @param resourceKeys 资源的KEY
     * @return true:包含
     */
    public List<Boolean> checkResources(final UserAccess userAccess,
                                        final String instanceKey,
                                        Collection<String> resourceKeys);

    /**
     * 校验是否包含给定的权限及资源
     *
     * @param instanceKey 域实例的KEY
     * @param purviewKey  权限的KEY
     * @param resourceKey 资源的KEY
     * @return true:包含
     */
    public boolean checkAccess(final UserAccess userAccess,
                               String instanceKey,
                               String purviewKey,
                               String resourceKey);

    /**
     * 检验是否包含给定的角色
     * @param userAccess
     * @param instanceKey  域实例的KEY
     * @param roleId       角色ID
     * @return
     */
    public boolean checkRole(final UserAccess userAccess,
                             String instanceKey,
                             String roleId);

    /**
     * 获取accesses中第一个满足instanceKey的 DomainInstanceAccess
     * @param instanceKey  域实例的KEY
     * @return DomainInstanceAccess
     */
    public DomainInstanceAccess firstDomainInstanceAccess(UserAccess userAccess,
                                                          final String instanceKey);

    /**
     * 给用户添加角色
     * @param userId 用户ID
     * @param role   角色
     */
    public void addRole(String userId, Role role);

    /**
     * 为删除用户增加角色
     * @param userId  用户ID
     * @param role  角色
     */
    public void delRole(String userId, Role role);

    /**
     * 新增用户
     * @param userId  用户ID
     * @param role  角色
     */
    public void addUser(String userId, Role role);
}
