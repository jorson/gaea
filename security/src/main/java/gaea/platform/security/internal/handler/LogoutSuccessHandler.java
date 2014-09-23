package gaea.platform.security.internal.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 触发登出成功后置业务。
 * <p/>
 * 处理日志的输出
 *
 * @author wuhy
 */
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication)
            throws IOException, ServletException {
        super.onLogoutSuccess(request, response, authentication);
    }
}


