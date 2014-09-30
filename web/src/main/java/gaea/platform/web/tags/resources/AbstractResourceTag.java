package gaea.platform.web.tags.resources;

import gaea.foundation.core.utils.StringUtils;
import gaea.platform.web.tags.utils.TagUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 静态资源标签抽象类
 *
 * @author licong
 * @date 2014-08-27
 */
public abstract class AbstractResourceTag extends TagSupport implements ResourceTag {

    private static final Log LOG = LogFactory.getLog(AbstractResourceTag.class);

    private String value;

    @Override
    public int doEndTag() throws JspException {
        Tag parent = getParent();
        // 拥有父标签IncludeTag
        if (parent instanceof IncludeTag) {
            IncludeTag includeTag = (IncludeTag) getParent();
            try {
                includeTag.addResource(this.clone());
            } catch (Exception e) {
                LOG.error("", e);
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(this.getStart())
                    .append(StaticUrlProvider.getUrl(this.value))
                    .append(this.value);
            if (!StringUtils.isEmpty(StaticUrlProvider.getVersion())) {
                sb.append(JspTagConstant.SINGLE_VERSION_START).append(StaticUrlProvider.getVersion());
            }
            sb.append(this.getEnd());
            writeToPage(sb.toString());
        }
        return super.doEndTag();
    }

    @Override
    public String getValue() {
        return value;
    }

    /**
     * 设值
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = TagUtils.fixUrl(value);
    }

    @Override
    protected AbstractResourceTag clone() throws CloneNotSupportedException {
        try {
            AbstractResourceTag clone = this.getClass().newInstance();
            clone.setValue(this.value);
            return clone;
        } catch (Exception e) {
            LOG.error("Clone Failed.", e);
            return this;
        }
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


}
