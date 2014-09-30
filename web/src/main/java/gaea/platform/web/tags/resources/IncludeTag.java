package gaea.platform.web.tags.resources;

import gaea.foundation.core.config.SystemConfig;
import gaea.foundation.core.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 用于将js、css静态文件声明合并为Minify的请求格式
 * e.g. http://debug.static.huayu.nd/?f=addins/bootstrap/v2.2.2/css/bootstrap.css,addins/jquery/udialog/v1.0/udialog-nae.css&v=20140827
 * Created by licong on 14-6-23.
 */
public class IncludeTag extends BodyTagSupport {

    private static final Log LOG = LogFactory.getLog(IncludeTag.class);

    // 存放标签
    private Queue<ResourceTagWrapper> resourceQueue = new PriorityQueue<ResourceTagWrapper>(1,
            new Comparator<ResourceTagWrapper>() {
                @Override
                public int compare(ResourceTagWrapper o1, ResourceTagWrapper o2) {
                    return o1.getPriority().compareTo(o2.getPriority());
                }
            });

    @Override
    public int doStartTag() throws JspException {
        resourceQueue.clear();
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        writeToPage(SystemConfig.Instance.getBooleanProperty(JspTagConstant.PROP_KEY_STATIC_MINIFY)
                ? minify() : common());
        return super.doEndTag();
    }

    /**
     * 不做minify处理
     */
    private String common() {
        StringBuilder sb = new StringBuilder();
        ResourceTag resourceTag;
        while (!resourceQueue.isEmpty()) {
            resourceTag = resourceQueue.poll().getResourceTag();
            sb.append(resourceTag.getStart())
                    .append(StaticUrlProvider.getUrl(resourceTag.getValue()))
                    .append(resourceTag.getValue());
            // 加上版本号
            if (!StringUtils.isEmpty(StaticUrlProvider.getVersion())) {
                sb.append(JspTagConstant.SINGLE_VERSION_START).append(StaticUrlProvider.getVersion());
            }
            sb.append(resourceTag.getEnd());
        }
        return sb.toString();
    }

    /**
     * 处理minify
     */
    private String minify() {
        if (resourceQueue.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        do {
            // 增加第一个静态标签
            ResourceTag resourceTag = resourceQueue.poll().getResourceTag();
            StringBuilder url = new StringBuilder();
            url.append(resourceTag.getValue());

            // 循环添加剩下的所有静态文件声明
            while (!resourceQueue.isEmpty()
                    && resourceQueue.peek().getResourceTag().getClass().equals(resourceTag.getClass())) {
                resourceTag = resourceQueue.poll().getResourceTag();
                url.append(JspTagConstant.COMMA).append(resourceTag.getValue());
            }
            // 加标签头以及静态文件声明
            sb.append(resourceTag.getStart())
                    .append(StaticUrlProvider.getUrl(url.toString()))
                    .append(JspTagConstant.FILE_START)
                    .append(url.toString());

            // 加上版本号
            if (!StringUtils.isEmpty(StaticUrlProvider.getVersion())) {
                sb.append(JspTagConstant.VERSION_START).append(StaticUrlProvider.getVersion());
            }
            // 加上标签尾
            sb.append(resourceTag.getEnd());
        } while (!resourceQueue.isEmpty());
        return sb.toString();
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

    /**
     * 添加静态资源标签
     *
     * @param resourceTag 静态资源标签
     */
    public void addResource(ResourceTag resourceTag) {
        ResourceTagWrapper resourceTagWrapper = new ResourceTagWrapper();
        resourceTagWrapper.setResourceTag(resourceTag);
        // 保证添加的顺序
        resourceTagWrapper.setPriority(resourceQueue.size() + resourceTag.getPriority() * 100);
        resourceQueue.add(resourceTagWrapper);
    }

    /**
     * ResourceTag的包装类，增加
     */
    private class ResourceTagWrapper {

        private Integer priority;
        private ResourceTag resourceTag;

        public Integer getPriority() {
            return priority;
        }

        public void setPriority(Integer priority) {
            this.priority = priority;
        }

        public ResourceTag getResourceTag() {
            return resourceTag;
        }

        public void setResourceTag(ResourceTag resourceTag) {
            this.resourceTag = resourceTag;
        }
    }
}
