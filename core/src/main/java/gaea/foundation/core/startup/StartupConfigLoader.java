package gaea.foundation.core.startup;

import gaea.foundation.core.config.ConfigConstant;
import gaea.foundation.core.config.SystemConfig;
import gaea.foundation.core.startup.support.ContextStartupException;
import gaea.foundation.core.utils.PropertiesLoaderUtils;
import gaea.foundation.core.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * 系统配置载入器，载入系统中使用到的配置数据
 *
 * @author wuhy
 */
public abstract class StartupConfigLoader {

    /**
     * Logger对象
     */
    private static final Log logger = LogFactory.getLog(StartupConfigLoader.class);

    public static void startup() throws Exception {
        startupProperties();
    }

    public static void startupProperties() throws Exception {
        startupSystemProperties();
        startupProjectProperties();
        startupDefaultConfigProperties();
    }

    public static void startupSystemProperties() throws IOException {
        try {
            Properties properties = loadProperties(ConfigConstant.CONFIG_SYSTEM_PROPERTIES_FILE);
            SystemConfig.Instance.setProperties(properties);
            //可用于Spring的占位
            merge(System.getProperties(), properties);
        } catch (IOException ex) {
            throw new ContextStartupException("载入系统配置文件(" + ConfigConstant.CONFIG_SYSTEM_PROPERTIES_FILE + ")失败！", ex);
        }
    }

    /**
     * 载入项目自定义的配置文件
     *
     * @throws java.io.IOException
     */
    public static void startupProjectProperties() throws IOException {
        String projectConfigPath = ConfigConstant.CONFIG_PROJECT_PROPERTIES_FILE;
        try {
            Properties projectProperties = new Properties();//loadProperties(ConfigConstant.CONFIG_PROJECT_PROPERTIES_FILE);
            // 载入具体项目的配置文件
            String projectName = projectProperties.getProperty(ConfigConstant.PROJECT_NAME);
            if (!StringUtils.isEmpty(projectName)) {
                projectConfigPath = MessageFormat.format(ConfigConstant.CONFIG_PROJECT_CUSTOM_PROPERTIES_FILE, projectName);
                merge(projectProperties, loadProperties(projectConfigPath));
            }
            merge(SystemConfig.Instance.getProperties(), projectProperties);
            //可用于Spring的占位
            merge(System.getProperties(), projectProperties);
        } catch (IOException ex) {
            // 项目中如果没有自定义的配置，不抛出异常
            logger.warn("载入项目配置文件(" + projectConfigPath + ")失败！", ex);
        }
    }

    /**
     * 载入数据库的操作
     *
     * @throws Exception
     */
    public static void startupDefaultConfigProperties() throws Exception {
        for (String configFile : ConfigConstant.CONFIG_DATABASE_PROPERTIES_FILES) {
            Properties configProps = loadProperties(configFile);
            merge(System.getProperties(), configProps);
        }
    }

    private static void merge(Properties target, Properties source) {
        PropertiesLoaderUtils.merge(target, source);
    }

    private static Properties loadProperties(String path) throws IOException {
        return PropertiesLoaderUtils.loadProperties(path);
    }
}
