package gaea.foundation.core.config;

/**
 * 定义系统的常量
 *
 * @author wuhy
 */
public final class ConfigConstant {
    /**
     * 默认多语言的路径
     */
    public static String PROJECT_I18N_RESOURCES = "**/*-messages*.properties";

    /**
     * 数据库的配置路径，包括Hibernate和JDBC
     */
    public static final String[] CONFIG_DATABASE_PROPERTIES_FILES = new String[]{"config/jdbc.properties", "config/hibernate.properties"};
    /**
     * 系统的配置路径
     */
    public static final String CONFIG_SYSTEM_PROPERTIES_FILE = "config/system.properties";
    /**
     * 项目(Project)的配置路径
     */
    public static final String CONFIG_PROJECT_PROPERTIES_FILE = "config/project.properties";
    /**
     * 自定义项目(Project)的配置路径
     */
    public static final String CONFIG_PROJECT_CUSTOM_PROPERTIES_FILE = "config/project.{0}.properties";
    /**
     * 项目名称
     */
    public static final String PROJECT_NAME = "project.name";
    /**
     * 系统是否处于开发状态KEY
     */
    public static final String SYSTEM_MODEL_DEVELOPMENT = "system.model.development";
    /**
     * 系统名称
     */
    public static String SYSTEM_TITLE = "system.title";
    /**
     * 用户登录后默认跳转URL的KEY
     */
    public static String HOME_PAGE = "home.page";
}
