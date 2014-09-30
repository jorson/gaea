package gaea.platform.web.tags.resources;

import gaea.foundation.core.utils.StringUtils;
import gaea.platform.web.tags.utils.TagUtils;

/**
 * @author licong
 * @date 14-8-27
 */
public class ELTag {

    /**
     * 处理src的url，加上静态站地址以及版本
     * @param url
     * @return
     */
    public static String url(String url) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        String fixUrl = TagUtils.fixUrl(url);
        stringBuilder.append(StaticUrlProvider.getUrl(fixUrl))
                .append(fixUrl);
        if (!StringUtils.isEmpty(StaticUrlProvider.getVersion())) {
            stringBuilder.append(JspTagConstant.SINGLE_VERSION_START).append(StaticUrlProvider.getVersion());
        }
        return stringBuilder.toString();
    }
}
