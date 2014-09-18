package gaea.foundation.core.config;

import gaea.foundation.core.utils.PropertiesLoaderUtils;
import gaea.foundation.core.utils.StringUtils;
import gaea.foundation.core.utils.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;

/**
 * 系统配置管理
 */
public final class SystemConfig {
    /**
     * Logger对象
     */
    private static final Log logger = LogFactory.getLog(SystemConfig.class);

    /**
     * 用于处理于锁
     */
    private Object syncObject = new Object();
    /**
     * 系统的配置文件
     */
    private Properties properties;
    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 是否处理开发状态
     */
    private Boolean isDevelopment;

    /**
     * 系统标题
     */
    private String systemTitle;

    public static SystemConfig Instance = new SystemConfig();

    private SystemConfig() {

    }

    /**
     * 载入项目的配置文件，将会覆盖system.properies
     * 如果在system.properties有配置，项目名称(project.name)，则所对应项目配置文件会被载入
     * 文件名称为project.XXXXXXX.properties,XXXXXXX为配置的项目名称
     * 默认载入project.properties
     *
     * @return
     */
    private Properties getProjectProperties() {
        Properties projectProperties = new Properties();
        try {
            projectProperties = PropertiesLoaderUtils.loadProperties(ConfigConstant.CONFIG_PROJECT_PROPERTIES_FILE);
        } catch (Exception ex) {
            logger.info("载入项目配置文件(" + ConfigConstant.CONFIG_PROJECT_PROPERTIES_FILE + ")失败！", ex);
        }
        // 载入具体项目的配置文件
        try {
            this.projectName = this.getProperty(ConfigConstant.PROJECT_NAME);
            if (!StringUtils.isEmpty(this.projectName)) {
                PropertiesLoaderUtils.merge(projectProperties,
                        PropertiesLoaderUtils.loadProperties(MessageFormat.format(ConfigConstant.CONFIG_PROJECT_CUSTOM_PROPERTIES_FILE, this.projectName)));
            }
        } catch (Exception ex) {
            logger.info("载入项目配置文件(" + ConfigConstant.CONFIG_PROJECT_CUSTOM_PROPERTIES_FILE + ")失败！", ex);
        }
        return projectProperties;
    }

    /**
     * 取得配置文件
     *
     * @return
     */
    public Properties getProperties() {
        if (this.properties == null) {
            synchronized (syncObject) {
                try {
                    this.properties = PropertiesLoaderUtils.loadProperties(ConfigConstant.CONFIG_SYSTEM_PROPERTIES_FILE);
                    PropertiesLoaderUtils.merge(this.properties, this.getProjectProperties());
                } catch (Exception ex) {
                    logger.warn("载入系统配置文件(" + ConfigConstant.CONFIG_SYSTEM_PROPERTIES_FILE + ")出错！" + ex.getMessage(), ex);
                }
            }
        }
        return this.properties;
    }

    public void setProperties(Properties systemProperties) {
        this.properties = systemProperties;
    }

    public String getProperty(String key) {
        return getProperties().getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        String value = getProperties().getProperty(key);
        if (StringUtils.isEmpty(value)) {
            value = defaultValue;
        }
        return value;
    }

    public boolean getBooleanProperty(String key) {
        String value = getProperties().getProperty(key);
        return Utils.toBoolean(value);
    }

    public int getIntProperty(String key) {
        return getIntProperty(key, 0);
    }

    public int getIntProperty(String key, int defaultValue) {
        String value = getProperties().getProperty(key);
        try {
            DecimalFormat df = new DecimalFormat();
            return df.parse(value).intValue();
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public String getProjectName() {
        return projectName;
    }

    public Boolean isDevelopment() {
        if (this.isDevelopment == null) {
            this.isDevelopment = getBooleanProperty(ConfigConstant.SYSTEM_MODEL_DEVELOPMENT);
        }
        return this.isDevelopment;
    }

    public String getSystemTitle() {
        if (this.systemTitle == null) {
            this.systemTitle = getProperty(ConfigConstant.SYSTEM_TITLE);
        }
        return this.systemTitle;
    }

    /**
     * 重新初始化，清空所有缓存
     */
    public static void reset() {
        Instance = new SystemConfig();
    }
}
