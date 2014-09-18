/**
 *
 */
package gaea.foundation.core.startup.support;

import gaea.foundation.core.startup.StartupConfigLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

/**
 * Application Spring Context Loader
 *
 * @author wuhy
 */

public class SpringContextLoaderForApplication {

    private static Log logger = LogFactory.getLog(SpringContextLoaderForApplication.class);

    private static final Object MONITOR = new Object();

    private static ApplicationContext springContext;

    private String[] configLocations;
    /**
     * 系统是否启动
     */
    private boolean started = false;
    /**
     * 配置是否载入
     */
    private boolean configStarted = false;

    /**
     * Spring上下文是否载入
     */
    private boolean contextStarted = false;

    /**
     * Reference to the JVM shutdown hook, if registered
     */
    private Thread shutdownHook;

    public boolean isStarted() {
        synchronized (MONITOR) {
            return started;
        }
    }

    /**
     * 启动,处理初始化操作
     * <p/>
     * 如果已经启动则不再次处理启动
     *
     * @throws Exception
     */
    public void startup() throws Exception {
        if (!started) {
            synchronized (MONITOR) {
                if (!started) {
                    startupConfig();
                    startupSpringContext();
                    registerShutdownHook();
                    started = true;
                }
            }
        }
    }

    /**
     * 启动配置载入处理，如果已经载入则不载入
     *
     * @throws Exception
     */
    public void startupConfig() throws Exception {
        if (!configStarted) {
            synchronized (MONITOR) {
                if (!configStarted) {
                    StartupConfigLoader.startup();
                    configStarted = true;
                }
            }
        }
    }

    /**
     * 启动上下文
     *
     * @throws Exception
     */
    public void startupSpringContext() throws Exception {
        if (!contextStarted) {
            synchronized (MONITOR) {
                if (!contextStarted) {
                    ClassPathXmlApplicationContext context = (ClassPathXmlApplicationContext)
                            SpringContextClassLoader.createAppApplicationContext(ClassPathXmlApplicationContext.class.getName());
                    context.setConfigLocations(configLocations);
                    context.refresh();
                    springContext = context;
                    contextStarted = true;
                }
            }
        }
    }

    public void setConfigLocations(String[] configLocations) {
        Assert.noNullElements(configLocations);
        if (!contextStarted) {
            synchronized (MONITOR) {
                if (!contextStarted) {
                    this.configLocations = configLocations;
                } else {
                    throw new ContextAlreadyStartupException();
                }
            }
        }
    }

    public ApplicationContext getApplicationContext() {
        return springContext;
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
        if (isStarted()) {
            logger.info("Closing " + this);
            if (springContext != null && springContext instanceof ConfigurableApplicationContext) {
                ((ConfigurableApplicationContext) springContext).close();
            }
            synchronized (MONITOR) {
                this.started = false;
            }
        }
    }
}
