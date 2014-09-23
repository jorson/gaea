package gaea.platform.security.internal.handler;

import gaea.platform.security.access.User;
import gaea.platform.security.context.UserContext;
import gaea.platform.security.support.SecurityConstant;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 触发登录成功后置业务。
 * <p/>
 * 处理日志的输出，最后一次登录时间的更新操作
 */
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        User user = (User) authentication.getPrincipal();
        // 放入session
        request.getSession().setAttribute(UserContext.KEY_CURRENT_USER, user);
        logger.info("用户(" + user.getUsername() + ")登录系统成功[" + java.util.Calendar.getInstance().getTime() + "]");
        super.onAuthenticationSuccess(request, response, authentication);
    }

    /**
     * 变更最后登录时间
     *
     * @param request
     * @param user
     */
    public void changeLastLoginTime(HttpServletRequest request, User user) {
        Date loginTime = user.getLastLoginTime();
        if (loginTime == null) {
            loginTime = new Date();
        }
        request.getSession().setAttribute(SecurityConstant.USER_LASTLOGINTIME_KEY, loginTime);
    }
}
