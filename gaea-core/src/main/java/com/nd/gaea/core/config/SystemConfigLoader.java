package com.nd.gaea.core.config;

import com.nd.gaea.core.utils.PropertiesLoaderUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Properties;

/**
 * 系统配置载入器，载入系统中使用到的配置数据
 *
 * @author bifeng.liu
 */
public abstract class SystemConfigLoader {

    /**
     * Logger对象
     */
    private static final Log LOGGER = LogFactory.getLog(SystemConfigLoader.class);

    public static void startup() {
        startupProperties();
    }

    public static void startupProperties() {
        LOGGER.debug("开始载入系统配置信息...");
        SystemConfig.reset();
        Properties properties = SystemConfig.instance.getProperties();
        merge(System.getProperties(), properties);
        LOGGER.debug("系统配置信息载入完成。");
    }

    private static void merge(Properties target, Properties source) {
        PropertiesLoaderUtils.merge(target, source);
    }

}
