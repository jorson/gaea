package gaea.platform.security.web.captcha;

import gaea.foundation.core.config.SystemConfig;
import gaea.foundation.core.utils.BeanUtils;
import gaea.foundation.core.utils.StringUtils;
import gaea.platform.security.support.SecurityConstant;
import com.octo.captcha.image.ImageCaptcha;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;

public class CaptchaValidator {

    private static final Logger logger = Logger.getLogger(CaptchaValidator.class);

    /**
     * 验证验证码是否正确,该验证方法区分大小写
     * <p/>
     * 如果正确返回TRUE，否则返回FALSE
     *
     * @param validateCode 输入的验证码
     * @param session      Session
     * @return 验证码是否正确
     */
    public static boolean validateCaptchaResponse(String validateCode, HttpSession session) {
        boolean flag = false;
        ImageCaptcha imageCaptcha = null;
        if (!StringUtils.isEmpty(validateCode)) {
            try {
                imageCaptcha = (ImageCaptcha) session.getAttribute(SecurityConstant.CAPTCHA_SESSION_KEYNAME);
                if (imageCaptcha != null) {
                    if (SystemConfig.Instance.getBooleanProperty(SecurityConstant.CAPTCHA_IGNORE_CASE)) {
                        validateCode = validateCode.toLowerCase();
                        try {
                            Object obj = BeanUtils.forceGetProperty(imageCaptcha, "response");
                            if (obj instanceof String) {
                                BeanUtils.forceSetProperty(imageCaptcha, "response", ((String) obj).toLowerCase());
                            }
                        } catch (Exception ex) {
                            logger.error("获取对象变量值异常，" + ex.getMessage(), ex);
                        }
                    }

                    flag = (imageCaptcha.validateResponse(validateCode)).booleanValue();
                    session.removeAttribute("imageCaptcha");
                }
            } catch (Exception ex) {
                logger.error("校验码校验异常 ，" + ex, ex);
            }
        }
        return flag;
    }
}
