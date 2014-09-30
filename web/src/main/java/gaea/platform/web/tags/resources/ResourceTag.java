package gaea.platform.web.tags.resources;

/**
 * 静态资源类型标记接口
 * Created by licong on 14-6-24.
 */
public interface ResourceTag extends Cloneable {

    /**
     * 获取静态资源声明的值
     * @return
     */
    public String getValue();

    /**
     * 获取静态资源的页面声明的头
     * e.g. javascript的声明头为 <script type="text/javascript" src="
     * @return
     */
    public String getStart();

    /**
     * 获取静态资源页面声明的尾
     * e.g. javascript的声明尾为 "></script>
     * @return
     */
    public String getEnd();

    /**
     * 获取优先级
     * @return
     */
    public Integer getPriority();
}
