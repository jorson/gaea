package gaea.platform.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 用户认证服务
 *
 * Created by jsc on 14-6-6.
 */
public interface UserAuthorizeService extends UserDetailsService {

    /**
     * 根据用户名密码认证用户
     *
     * @param username
     * @param encodePassword
     * @return
     */
    public UserDetails authorize(String username, String encodePassword);

    /**
     * 验证accessToken
     *
     * @param accessToken
     * @return
     */
    public UserDetails validAccessToken(String accessToken);
}
