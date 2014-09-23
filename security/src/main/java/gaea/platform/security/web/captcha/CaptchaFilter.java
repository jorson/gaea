package gaea.platform.security.web.captcha;

import gaea.foundation.core.config.SystemConfig;
import gaea.foundation.core.utils.MessageUtils;
import gaea.platform.security.exception.AuthenticationException;
import gaea.platform.security.support.SecurityConstant;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CaptchaFilter implements Filter {

    private static final String FILTER_APPLIED = "__windawn_security_CaptchaFilter_filterApplied";

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession();
        session.setAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY,
                request.getAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY));
//                RequestUtils.getString(request, UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY));
        // 如果没有打开
        boolean enabled = SystemConfig.Instance.getBooleanProperty(SecurityConstant.CAPTCHA_ENABLED);
        if (enabled) {
            // 如果本次请求已经验证过，则不处理
            if (request.getAttribute(FILTER_APPLIED) != null) {
                chain.doFilter(request, response);
            } else {
                // 第一次请求时处理
                request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
                String validateCode = (String) request.getAttribute(SecurityConstant.CAPTCHA_FIELD_NAME);
                // 比较验证码是否正确。
                boolean validateFlag = CaptchaValidator.validateCaptchaResponse(validateCode, session);
                if (validateFlag) {
                    chain.doFilter(request, response);
                } else {
                    String errorMessage = MessageUtils.format("login_error_captcha");
                    session.setAttribute("SPRING_SECURITY_LAST_EXCEPTION", new AuthenticationException(errorMessage));
                    request.getRequestDispatcher("login.jsp?error=true").forward(request, response);
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

}
