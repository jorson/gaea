package com.nd.gaea.web.startup;

import com.nd.gaea.web.model.ViewModelScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 初始化Spring MVC容器
 */
public class SpringDispatcherServlet extends DispatcherServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringDispatcherServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //调用ViewModel的扫描器
        executeViewModelScanner(config.getServletContext());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    /**
     * 执行对ViewModel的扫描
     *
     * @param servletContext Servlet上下文
     */
    private void executeViewModelScanner(ServletContext servletContext) {
        try {
            WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            ViewModelScanner bean = (ViewModelScanner) context.getBean("viewModelScanner");
            if (bean != null) {
                bean.scanPackage();
            }
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Scan view model happen error!", e);
            }
        }
    }
}
