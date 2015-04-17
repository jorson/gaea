package com.nd.gaea.web.support;


import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.web.servlet.config.*;

/**
 * <p></p>
 *
 * @author bifeng.liu
 * @since 2014/11/19
 */
public class MvcNamespaceHandler extends org.springframework.web.servlet.config.MvcNamespaceHandler {
    @Override
    public void init() {
        super.init();
        System.out.printf("ccc--------------------------");
    }
}
