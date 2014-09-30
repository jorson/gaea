package gaea.platform.web.tags.resources;

import gaea.foundation.core.config.SystemConfig;
import gaea.foundation.core.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 静态站URL提供者
 * Created by licong on 14-6-23.
 */
public abstract class StaticUrlProvider {

    private static final Log LOG = LogFactory.getLog(StaticUrlProvider.class);

    private static List<String> STATIC_URLS = new ArrayList<String>();
    private static final String VERSION = SystemConfig.Instance.getProperty("static.version");

    static {
        String urls = SystemConfig.Instance.getProperty("static.url");
        if (StringUtils.isEmpty(urls)) {
            LOG.error("Couldn't find static.url defined in property files.");
        } else {
            StringTokenizer stringTokenizer = new StringTokenizer(urls, JspTagConstant.COMMA);
            String url;
            while (stringTokenizer.hasMoreTokens()) {
                url = stringTokenizer.nextToken();
                if(StringUtils.isEmpty(url)) {
                    continue;
                }
                STATIC_URLS.add(fixStaticUrl(url));
            }
        }
    }

    /**
     * 获取URL
     *
     * @param url e.g. /gaea/common.js
     * @return e.g. http://new.static.ty.nd
     * http://s12.tianyuimg.com
     */
    public static String getUrl(String url) {
        return STATIC_URLS.get(Math.abs(url.hashCode()) % STATIC_URLS.size());
    }

    /**
     * 获取版本号
     * @return
     */
    public static String getVersion() {
        return VERSION;
    }

    /**
     * 容错处理
     * @param url
     * @return
     */
    private final static String fixStaticUrl(String url) {
        if(-1 == url.indexOf(JspTagConstant.HTTP_HEAD)) {
            url = JspTagConstant.HTTP_HEAD + url;
        }
        if (url.lastIndexOf(JspTagConstant.HTTP_SEPARATOR) != url.length()-1) {
            url = url + JspTagConstant.HTTP_SEPARATOR;
        }
        return url;
    }
}
