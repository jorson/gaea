package com.nd.gaea.web.controller.support;


import com.nd.gaea.utils.StringUtils;
import com.nd.gaea.utils.Utils;
import com.nd.gaea.web.controller.ControllerResult;
import com.nd.gaea.web.controller.ErrorControllerResult;
import com.nd.gaea.web.controller.OpenApiControllerResult;
import com.nd.gaea.web.exception.ControllerException;

import java.lang.reflect.InvocationTargetException;

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

    /**
     * 根据异常取得返回值
     *
     * @param ex
     * @param isOpenApi
     * @return
     */
    public static ControllerResult wrapException(Throwable ex, boolean isOpenApi) {
        int code = ControllerConstants.CODE_UNKNOWN;
        if (ex instanceof InvocationTargetException) {
            InvocationTargetException targetException = (InvocationTargetException) ex;
            ex = targetException.getTargetException() != null ? targetException.getTargetException() : targetException;
        } else if (ex instanceof ControllerException) {
            ControllerException targetException = (ControllerException) ex;
            code = Utils.toInt(targetException.getCode(), ControllerConstants.CODE_UNKNOWN);
        }

        ControllerResult result = isOpenApi ? new OpenApiControllerResult() : new ErrorControllerResult();
        result.setCode(code);
        result.setMessage(ex.getMessage());
        return result;
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
