package gaea.platform.security.service;

import gaea.platform.security.access.User;

/**
 * Created by wuhy on 14-6-3.
 */
public interface UserService {
    /**
     * 获取匿名用户
     * @return 匿名用户
     */
    public User getAnonymousUser();
}
