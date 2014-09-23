package gaea.platform.security.internal.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 触发登录失败后置业务。
 *
 * @author wuhy
 */
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        // TODO 处理登录失败N次后自动锁定该用户
        logger.error("用户(" + obtainUsername(request) + ")登录失败[" + java.util.Calendar.getInstance().getTime() + "]"
                + exception.getMessage(), exception);
        super.onAuthenticationFailure(request, response, exception);
    }

    /**
     * 取得登录的用户名称
     *
     * @param request
     * @return
     */
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
    }

}
