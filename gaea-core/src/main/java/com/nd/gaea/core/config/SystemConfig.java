package com.nd.gaea.core.config;

import com.nd.gaea.core.utils.PropertiesLoaderUtils;
import com.nd.gaea.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * 系统配置管理
 */
public final class SystemConfig {
    /**
     * Logger对象
     */
    private static final Log LOGGER = LogFactory.getLog(SystemConfig.class);

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
    /**
     *
     */
    public static SystemConfig instance = new SystemConfig();

    /**
     * 私有化构造函数
     */
    private SystemConfig() {
    }

    /**
     * 载入项目的配置文件，将会覆盖system.properies，
     * 如果在system.properties有配置，项目名称(project.name)，则所对应项目配置文件会被载入，
     * 文件名称为project.XXXXXXX.properties,XXXXXXX为配置的项目名称，
     * 默认载入project.properties
     *
     * @return 合并后的配置文件
     */
    private Properties loadProjectProperties() {
        Properties projectProperties = new Properties();
        try {
            projectProperties = loadProperties(ConfigConstant.CONFIG_PROJECT_PROPERTIES_FILE);
        } catch (Exception ex) {
            LOGGER.info("载入项目配置文件(" + ConfigConstant.CONFIG_PROJECT_PROPERTIES_FILE + ")失败！", ex);
        }
        // 载入具体项目的配置文件
        try {
            this.projectName = this.getProperty(ConfigConstant.PROJECT_NAME);
            if (!StringUtils.isEmpty(this.projectName)) {
                merge(projectProperties,
                        loadProperties(MessageFormat.format(ConfigConstant.CONFIG_PROJECT_CUSTOM_PROPERTIES_FILE, this.projectName)));
            }
        } catch (Exception ex) {
            LOGGER.info("载入项目配置文件(" + ConfigConstant.CONFIG_PROJECT_CUSTOM_PROPERTIES_FILE + ")失败！", ex);
        }
        return projectProperties;
    }

    /**
     * 载入自定义的项目的配置文件，将会覆盖system.properies和project.properties，
     * 如果在system.properties或者project.properties有配置，自定义的配置(custom.properties)，则所对应配置文件会被载入，
     * 当多个配置时，其中一个配置文件未载入成功不会影响其他的配置文件。
     *
     * @return 合并后的配置文件
     */
    private Properties loadCustomProperties() {
        Properties properties = new Properties();
        String value = this.getProperty(ConfigConstant.CUSTOM_PROPERTIES_FILES);
        if (StringUtils.isNotEmpty(value)) {
            String[] values = value.split(",");
            for (int i = 0; i < values.length; i++) {
                // 载入自定义的配置文件
                String v = values[i];
                if (StringUtils.isNotEmpty(StringUtils.trimWhitespace(v))) {
                    try {
                        merge(properties, loadProperties(v));
                    } catch (Exception ex) {
                        LOGGER.warn("载入自定义配置文件(" + v + ")失败！", ex);
                    }
                }
            }
        }
        return properties;
    }

    /**
     * 载入系统的配置文件，配置文件路径在config/system.properties
     */
    public Properties loadSystemProperties() {
        Properties properties = new Properties();
        try {
            properties = loadProperties(ConfigConstant.CONFIG_SYSTEM_PROPERTIES_FILE);
        } catch (IOException ex) {
            LOGGER.warn("载入自定义配置文件(" + ConfigConstant.CONFIG_SYSTEM_PROPERTIES_FILE + ")失败！", ex);
        }
        return properties;
    }

    /**
     * 取得配置文件
     *
     * @return
     */
    public Properties getProperties() {
        if (this.properties == null) {
            init();
        }
        return this.properties;
    }

    /**
     * 初始化所有配置信息
     */
    public void init() {
        synchronized (syncObject) {
            this.properties = loadSystemProperties();
            merge(this.properties, this.loadProjectProperties()); //合并项目配置
            merge(this.properties, this.loadCustomProperties()); //合并自定义配置
        }
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
        return toBoolean(value);
    }

    /**
     * 把字符转换成Boolean，
     * <p/>
     * 当为true/yes/ok/1字符串时，返回true,否则返回false，不区分大小写
     *
     * @param s
     * @return
     */
    private static boolean toBoolean(String s) {
        if (s == null || s.length() == 0) return false;
        return s.compareToIgnoreCase("TRUE") == 0 || s.compareToIgnoreCase("YES") == 0
                || s.compareToIgnoreCase("OK") == 0 || s.compareToIgnoreCase("1") == 0;
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

    private static void merge(Properties target, Properties source) {
        PropertiesLoaderUtils.merge(target, source);
    }

    private static Properties loadProperties(String path) throws IOException {
        return PropertiesLoaderUtils.loadProperties(path);
    }

    /**
     * 重新初始化，清空所有配置
     */
    public static void reset() {
        synchronized (instance) {
            instance = new SystemConfig();
        }
    }
}
