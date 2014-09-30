package gaea.platform.web.controller.support;


import gaea.foundation.core.utils.StringUtils;

/**
 * 控制器的工具类
 *
 * @author bifeng.liu
 */
public class ControllerUtils {

    /**
     * Controller页面的默认扩展名
     */
    public static final String DEFAULT_CONTROLLER_PAGE_EXTENSION = "ftl";

    /**
     * Controller页面的默认前缀
     */
    public static final String DEFAULT_CONTROLLER_PAGE_PREFIX = "/mod/";


    /**
     * 使用默认Action存放路径拼接页面路径
     * <p/>
     * Action页面存放的目录 + Namespace + Action + 扩展名，该方法只支持顶层模块
     *
     * @param rootPath 根目录
     * @param action   Action名称
     * @return
     */
    public static String concatViewPagePath(final String rootPath, final String action) {
        return concatViewPagePath(DEFAULT_CONTROLLER_PAGE_PREFIX, rootPath, action);
    }

    /**
     * 使用默认Action存放路径拼接页面路径
     * <p/>
     * Action页面存放的目录 + Namespace + Action + 扩展名，该方法只支持顶层模块
     *
     * @param rootPath 根目录
     * @param action   Action名称
     * @return
     */
    public static String concatViewPagePath(final String prefix, final String rootPath, final String action) {
        StringBuilder path = new StringBuilder();
        String temp = (prefix != null ? prefix : DEFAULT_CONTROLLER_PAGE_PREFIX).trim();
        path.append(temp);
        if (!temp.trim().endsWith("/")) {
            path.append("/");
        }
        if (!StringUtils.isEmpty(rootPath)) {
            path.append(rootPath.trim().startsWith("/") ? rootPath.substring(1) : rootPath);
            if (!rootPath.trim().endsWith("/")) {
                path.append("/");
            }
        }
        return path.append(action).toString();
    }

    public static void main(String[] args) {
        System.out.println(concatViewPagePath(null, "/simple", "hello"));
        System.out.println(concatViewPagePath("/cccc", "/simple", "hello"));
        System.out.println(concatViewPagePath("/cccc/", "/simple/", "hello"));
        System.out.println(concatViewPagePath("/simple", "hello"));
    }

//    /**
//     * 连接页面路径
//     *
//     * @param actionName Action名称
//     * @return
//     */
//    protected String concatPagepath(final String actionName) {
//        StringBuffer sb = new StringBuffer();
//        sb.append("/");
//        if (!StringUtils.isEmpty(this.getRootPath())) {
//            sb.append("mod/").append(getRootPath()).append("/");
//        }
//        return sb.append(actionName).toString();
//    }
}
