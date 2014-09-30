package gaea.platform.web.tags.resources;

/**
 * 引入CSS文件的标签，替代link标签
 * @author licong
 * @date 2014-08-27
 */
public class CssTag extends AbstractResourceTag {

    /**
     * 脚本头
     */
    private static final String SCRIPT_START = "<link rel=\"stylesheet\" href=\"";

    /**
     * 脚本尾
     */
    private static final String SCRIPT_END = "\">";

    @Override
    public String getStart() {
        return SCRIPT_START;
    }

    @Override
    public String getEnd() {
        return SCRIPT_END;
    }

    @Override
    public Integer getPriority() {
        return 0;
    }
}
