package gaea.platform.security.support.provider;

import gaea.platform.security.UserAuthorizeService;
import gaea.platform.security.exception.AuthenticationException;
import gaea.platform.security.utils.PasswordEncoderUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * 密码认证
 *
 * Created by jsc on 14-6-3.
 */
public class PasswordAuthenticationProvider implements AuthenticationProvider {

    // 日志
    private static Logger logger = Logger.getLogger(PasswordAuthenticationProvider.class);

    // 用户认证服务
    private UserAuthorizeService userAuthorizeService;

    @Override
    public Authentication authenticate(Authentication authentication) throws org.springframework.security.core.AuthenticationException {
        String username = retrieveUserName(authentication);
        String password = retrievePassword(authentication);
        if (StringUtils.isEmpty(username)) {
            throw new AuthenticationException("用户名不能为空！");
        }
        if (StringUtils.isEmpty(password)) {
            throw new AuthenticationException("密码不能为空！");
        }
        try {
            String encodePassword = password;
            // 用户正常登录
            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                // 加密
                encodePassword = PasswordEncoderUtils.encryptPassword(password);
            }
            // 用户认证
            UserDetails user = userAuthorizeService.authorize(username, encodePassword);
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
    protected Authentication createAuthenticationToken(UserDetails user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        token.setDetails(user);
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 用户名密码登录 支持
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    /**
     * 是否User
     *
     * @param authentication
     * @return
     */
    private boolean isInstanceOfUserDetails(Authentication authentication) {
        return authentication.getPrincipal() instanceof UserDetails;
    }

    /**
     * 接收用户名
     *
     * @param authentication
     * @return
     */
    protected String retrieveUserName(Authentication authentication) {
        if (isInstanceOfUserDetails(authentication)) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            return authentication.getPrincipal().toString();
        }
    }

    /**
     * 接收密码
     *
     * @param authentication
     * @return
     */
    protected String retrievePassword(Authentication authentication) {
        if (isInstanceOfUserDetails(authentication)) {
            return ((UserDetails) authentication.getPrincipal()).getPassword();
        } else {
            if (authentication.getCredentials() == null) {
                return null;
            }
            return authentication.getCredentials().toString();
        }
    }

    public UserAuthorizeService getUserAuthorizeService() {
        return userAuthorizeService;
    }

    public void setUserAuthorizeService(UserAuthorizeService userAuthorizeService) {
        this.userAuthorizeService = userAuthorizeService;
    }
}
