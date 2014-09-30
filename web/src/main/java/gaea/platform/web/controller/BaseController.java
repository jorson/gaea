package gaea.platform.web.controller;

import gaea.foundation.core.config.SystemConfig;
import gaea.foundation.core.utils.StringUtils;
import gaea.platform.web.controller.support.ControllerInfo;
import gaea.platform.web.controller.support.ControllerUtils;
import gaea.platform.web.exception.ControllerExceptionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 请在这里输入说明
 *
 * @author bifeng.liu
 */
public abstract class BaseController {
    /**
     * LOGGER对象
     */
    private static Log logger = LogFactory.getLog(BaseController.class);

    /**
     * Action空间
     */
    private String namespace;
    /**
     * View文件根目录
     */
    private String rootPath;
    //-------------------------------------------------------------------------
    // 功能
    //-------------------------------------------------------------------------


    //-------------------------------------------------------------------------
    // Getter/Settter
    //-------------------------------------------------------------------------

    /**
     * 是否为DEBUG状态
     */
    public boolean isDebug() {
        return SystemConfig.Instance.isDevelopment();
    }

    /**
     * 取得模块名称
     *
     * @return
     */
    public String getNamespace() {
        if (namespace == null) {
            putControllerInfo();
        }
        return namespace;
    }

    /**
     * 取得View所在的根目录
     *
     * @return
     */
    public String getRootPath() {
        if (rootPath == null) {
            this.getNamespace();
            if (rootPath == null) {
                rootPath = this.getNamespace();
            }
        }
        return rootPath;
    }

    //-------------------------------------------------------------------------
    //内部方法
    //-------------------------------------------------------------------------

    /**
     * 连接页面路径
     *
     * @param actionName Action名称
     * @return
     */
    protected String concatPagepath(final String actionName) {
        return ControllerUtils.concatViewPagePath(getRootPath(), actionName);
    }

    /**
     * 发生错误
     *
     * @param message
     * @return
     */
    protected void happenError(String message, Exception ex) throws Exception {
        logger.error(message, ex);
        if (ex != null) {
            throw ControllerExceptionFactory.createControlException(message);
        } else {
            throw ControllerExceptionFactory.wrapControllerException(message, ex);
        }
    }

    /**
     * 设置Controller的信息
     * <p/>
     * 如果在Controller上有注解ControllerInfo，则从ControllerInfo里面取namespace，rootpath，
     * 否则判断是否在Controller上有注解RequestMapping，则直接从RequestMapping取得第一个路径作为namespace、rootpath
     */
    private void putControllerInfo() {
        this.namespace = "";
        this.rootPath = "";
        Class actionClass = this.getClass();
        // 如果有设置ControllerInfo，则从ControllerInfo里面取namespace，rootpath
        if (actionClass.isAnnotationPresent(ControllerInfo.class)) {
            ControllerInfo anno = (ControllerInfo) actionClass.getAnnotation(ControllerInfo.class);
            this.namespace = anno.namespace();
            this.rootPath = anno.rootPath();
            if (StringUtils.isEmpty(this.rootPath)) {
                this.rootPath = this.namespace;
            }
        } else if (actionClass.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping anno = (RequestMapping) actionClass.getAnnotation(RequestMapping.class);
            String[] values = anno.value();
            if (values.length == 0 && "/".equals(values[0])) {
                return;
            }
            this.rootPath = values[0].substring(1);
            this.namespace = this.rootPath.replace("/", "_");
        }
    }
}
