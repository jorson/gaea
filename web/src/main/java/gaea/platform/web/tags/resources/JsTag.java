package gaea.platform.web.tags.resources;

/**
 * 引入JAVASCRIPT文件的标签，替代script标签
 * @author licong
 * @date 2014-08-27
 */
public class JsTag extends AbstractResourceTag {

    /**
     * 脚本头
     */
    private static final String SCRIPT_START = "<script type=\"text/javascript\" src=\"";

    /**
     * 脚本尾
     */
    private static final String SCRIPT_END = "\"></script>\n";

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
        return 1;
    }
}
