package gaea.platform.web.startup;

import gaea.foundation.core.config.SystemConfig;
import gaea.foundation.core.startup.StartupConfigLoader;
import gaea.foundation.core.utils.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;
import java.net.URL;

/**
 * Spring上下文监听
 * <p/>
 * 继承自<code>ContextLoaderListener</code>，处理在Spring初始化之前载入本框架使用到的相关配置
 *
 * @author bifeng.liu
 * @see org.springframework.web.context.ContextLoaderListener
 */
public class SpringContextLoaderListener extends ContextLoaderListener {
    /**
     * Logger对象
     */
    private static final Log logger = LogFactory.getLog(SpringContextLoaderListener.class);

    /**
     * Reference to the JVM shutdown hook, if registered
     */
    private Thread shutdownHook;

    public void contextInitialized(ServletContextEvent event) {
        // 载入所有需要的配置文件
        try {
            startupConfig();
            startupLogConfig();
            postStartup();
            registerShutdownHook();
        } catch (Exception ex) {
            logger.error("Failed to initialize configuration," + ex.getMessage(), ex);
        }
        super.contextInitialized(event);
    }

    /**
     * 当为调试时，载入log4j_dev.xml
     */
    public void startupLogConfig() {
        String path = "log4j.xml";
        if (SystemConfig.Instance.isDevelopment()) {
            path = "/log4j_dev.xml";
        }
        URL configFile = SpringContextLoaderListener.class.getResource(path);
        if (configFile != null) {
            DOMConfigurator.configure(configFile);
        }
    }


    public void startupConfig() throws Exception {
        StartupConfigLoader.startup();
    }

    protected void postStartup() {
        logger.info("startup success " + this);
        println("Web服务器启动成功");
    }


    public void registerShutdownHook() {
        if (this.shutdownHook == null) {
            // No shutdown hook registered yet.
            this.shutdownHook = new Thread() {
                public void run() {
                    doClose();
                }
            };
            Runtime.getRuntime().addShutdownHook(this.shutdownHook);
        }
    }

    protected void doClose() {
        logger.info("Closing " + this);
        println("Web服务器停止成功");
    }

    public void println(String message) {
        System.out.println(DateUtils.format() + " : " + message);
    }
}
