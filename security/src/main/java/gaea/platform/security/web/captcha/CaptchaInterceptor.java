package gaea.platform.security.web.captcha;


import gaea.foundation.core.config.SystemConfig;
import gaea.foundation.core.utils.MessageUtils;
import gaea.platform.security.support.SecurityConstant;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 验证码拦截类，用于处理验证码操作
 */
public class CaptchaInterceptor {

    /**
     * 拦载处理
     *
     * @param request
     * @param response
     * @return
     * @throws SecurityException
     */
    public boolean intercept(ServletRequest request, ServletResponse response) throws SecurityException {
        // 如果没有打开
        boolean enabled = SystemConfig.Instance.getBooleanProperty(SecurityConstant.CAPTCHA_ENABLED);
        if (enabled) {
            HttpSession session = ((HttpServletRequest) request).getSession();
            String validateCode = (String) request.getAttribute(SecurityConstant.CAPTCHA_FIELD_NAME);
            // 比较验证码是否正确。
            boolean validateFlag = CaptchaValidator.validateCaptchaResponse(validateCode, session);
            if (validateFlag) {
                return true;
            } else {
                String errorMessage = MessageUtils.format("login_error_captcha");
                session.setAttribute("SPRING_SECURITY_LAST_EXCEPTION", new SecurityException(errorMessage));
                throw new SecurityException(errorMessage);
            }
        }
        return true;
    }
}
