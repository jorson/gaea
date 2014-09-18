package gaea.foundation.core.startup.support;

import gaea.foundation.core.utils.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

/**
 * 取得Spring的ApplicationContext
 *
 * @author wuhy
 */
public abstract class SpringContextClassLoader {

    protected static Log logger = LogFactory.getLog(SpringContextClassLoader.class);

    private static final String SPRING_APPCONTEXT_CLASS_KEY = "spring.appcontext.class";

    /**
     * 取得Application的Spring ApplicationContext
     * <p>根据Key取得从配置中取得值，如果没有值，使用Context Class，如果初始化ApplicationContext失败，则直接返回ClassPathXmlApplicationContext</p>
     *
     * @param contextClass
     * @return
     * @see #createApplicationContext(String, String)
     */
    public static ApplicationContext createAppApplicationContext(String contextClass) {
        Assert.hasText(contextClass);
        ApplicationContext context = createApplicationContext(SPRING_APPCONTEXT_CLASS_KEY, contextClass);
        if (context != null) {
            return context;
        } else {
            logger.error("return default ClassPathXmlApplicationContext");
            return new ClassPathXmlApplicationContext();
        }
    }

    /**
     * 取得Spring ApplicationContext
     * <p>根据Key取得从配置中取得值，如果有值，则使用配置的值初始化，否则使用Context Class来初始化</p>
     *
     * @param contextKey
     * @param contextClass
     * @return
     */
    protected static ApplicationContext createApplicationContext(String contextKey, String contextClass) {
        Assert.hasText(contextClass);
        Assert.hasText(contextKey);
        String configContextClass = System.getProperty(contextKey, contextClass);
        try {
            return (ApplicationContext) ClassUtils.createNewInstance(configContextClass);
        } catch (Exception e) {
            logger.error("error while load ContextClass[" + configContextClass + "]", e);
            return null;
        }
    }
}
