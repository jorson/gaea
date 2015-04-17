package com.nd.gaea.web.support;

import com.nd.gaea.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>与Version相关URL路径帮助类</p>
 *
 * @author bifeng.liu
 * @see org.springframework.web.util.UrlPathHelper
 * @since 2014/11/19
 */
public class UrlPathVersionHelper extends UrlPathHelper {
    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog(UrlPathVersionHelper.class);

    /**
     * 根据Request取得路径
     * <p/>
     * 在请求时判断Accept中有带有有效的version参数，则会自动加入v+version的前缀
     * <code>
     * request:
     * <p>GET:/simple/list</p>
     * <p>HOST: localhost</p>
     * <p>Accept: application/json; version=2</p>
     * </code>
     * 就会自动匹配/v2/simple/list路径
     *
     * @param request
     * @return
     * @see org.springframework.web.util.UrlPathHelper#getLookupPathForRequest(javax.servlet.http.HttpServletRequest)
     */
    public String getLookupPathForRequest(HttpServletRequest request) {
        String path = super.getLookupPathForRequest(request);
        String version = getVersion(request);
        if (StringUtils.isNotEmpty(version)) {
            return "/v" + version + path;
        }
        return path;
    }

    private String getVersion(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        if (StringUtils.isNotEmpty(accept)) {
            String[] items = accept.split(";");
            for (int i = 0; i < items.length; i++) {
                String item = items[i];
                if (item.indexOf("version") > -1) {
                    return item.substring(item.indexOf("=") + 1);
                }
            }
        }
        return "";
    }
}
