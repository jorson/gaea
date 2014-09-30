package gaea.platform.web.tags.utils;

import gaea.foundation.core.utils.StringUtils;
import gaea.platform.web.tags.resources.JspTagConstant;

/**
 * @author licong
 * @date 14-8-27
 */
public class TagUtils {

    /**
     * 容错处理
     * 确保最前面没有'/',不带?v=
     *
     * @param url URL
     * @return
     */
    public static final String fixUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        if (url.indexOf(JspTagConstant.HTTP_SEPARATOR) == 0) {
            return url.substring(1, url.length());
        }
        int paramIndex = url.indexOf(JspTagConstant.PARAM_START);
        if (paramIndex != -1) {
            return url.substring(0, paramIndex);
        }
        return url;
    }
}
