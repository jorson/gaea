package gaea.platform.web.tags.resources;

import gaea.foundation.core.utils.StringUtils;
import gaea.platform.web.tags.utils.TagUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author licong
 * @date 14-8-28
 */
public class UrlTag extends TagSupport {

    private String value;
    private static final Log LOG = LogFactory.getLog(UrlTag.class);


    @Override
    public int doEndTag() throws JspException {
        if (StringUtils.isEmpty(value)) {
            writeToPage("");
        }
        StringBuilder stringBuilder = new StringBuilder();
        String fixUrl =  TagUtils.fixUrl(value);
        stringBuilder.append(StaticUrlProvider.getUrl(fixUrl))
                .append(fixUrl);
        if (!StringUtils.isEmpty(StaticUrlProvider.getVersion())) {
            stringBuilder.append(JspTagConstant.SINGLE_VERSION_START).append(StaticUrlProvider.getVersion());
        }
        writeToPage(stringBuilder.toString());
        return super.doEndTag();
    }

    /**
     * 将内容写到页面上
     *
     * @param content
     */
    private void writeToPage(String content) {
        try {
            pageContext.getOut().write(content);
        } catch (Exception e) {
            LOG.error("Output Script Tag To JSP FAILED!", e);
        }
    }

    public void setValue(String value) {
        this.value = value;
    }
}
