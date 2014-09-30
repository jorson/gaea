package gaea.platform.web.startup.support;

import gaea.foundation.core.startup.support.SpringContextClassLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * 取得Spring的Web ApplicationContext
 *
 * @author bifeng.liu
 */
public class SpringWebContextClassLoader extends SpringContextClassLoader {

    private static final String SPRING_WEBCONTEXT_CLASS_KEY = "spring.webcontext.class";

    /**
     * 取得WebApplication的Spring ApplicationContext
     * <p>根据Key取得从配置中取得值，如果没有值，使用Context Class，如果初始化ApplicationContext失败，则直接返回XmlWebApplicationContext</p>
     *
     * @param contextClass
     * @return
     * @see #createApplicationContext(String, String)
     */
    public static ApplicationContext createWebApplicationContext(String contextClass) {
        Assert.hasText(contextClass);
        ApplicationContext context = createApplicationContext(SPRING_WEBCONTEXT_CLASS_KEY, contextClass);
        if (context != null) {
            return context;
        } else {
            logger.error("return default ClassPathXmlApplicationContext");
            //WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(((HttpServletRequest) BssUserContext.getRequest()).getSession().getServletContext());
            return new XmlWebApplicationContext();
        }
    }
}
