package gaea.platform.security.support.provider;

import gaea.platform.security.UserAuthorizeService;
import gaea.platform.security.access.User;
import gaea.platform.security.exception.AuthenticationException;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * AccessToken认证
 *
 * Created by jsc on 14-6-3.
 */
public class AccessTokenAuthenticationProvider implements AuthenticationProvider {

    // 日志
    private static Logger logger = Logger.getLogger(AccessTokenAuthenticationProvider.class);

    // 用户认证服务
    private UserAuthorizeService userAuthorizeService;

    @Override
    public Authentication authenticate(Authentication authentication) throws org.springframework.security.core.AuthenticationException {
        String accessToken = retrieveAccessToken(authentication);
        try {
            // 用户认证
            UserDetails user = userAuthorizeService.validAccessToken(accessToken);
            return this.createAuthenticationToken(user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AuthenticationException(e.getMessage(), e);
        }
    }

    /**
     * 创建认证凭证
     *
     * @param user
     * @return
     */
    private Authentication createAuthenticationToken(UserDetails user) {
        // 构造spring的token信息
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        token.setDetails(user);
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 自动登录 支持
        return (RememberMeAuthenticationToken.class.isAssignableFrom(authentication));
    }

    /**
     * 获取AccessToken
     *
     * @param authentication
     * @return
     */
    private String retrieveAccessToken(Authentication authentication) {
        if (authentication.getPrincipal() instanceof User) {
            return ((User) authentication.getPrincipal()).getAccessToken();
        }
        return null;
    }

    public UserAuthorizeService getUserAuthorizeService() {
        return userAuthorizeService;
    }

    public void setUserAuthorizeService(UserAuthorizeService userAuthorizeService) {
        this.userAuthorizeService = userAuthorizeService;
    }
}
