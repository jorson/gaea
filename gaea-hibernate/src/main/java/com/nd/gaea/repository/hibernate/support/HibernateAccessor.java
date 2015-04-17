package com.nd.gaea.repository.hibernate.support;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.SessionFactory;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.orm.hibernate4.SessionFactoryUtils;

import java.sql.SQLException;

/**
 * 定义Hibernate入口的通用方法
 *
 * @author bifeng.liu
 */
public class HibernateAccessor implements InitializingBean {

    private SessionFactory sessionFactory;


    /**
     * 设置SessionFactory
     *
     * @param sessionFactory
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 返回SessionFactory，必须定义
     */
    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    /**
     * 实例化对象完成后处理操作，验证SessionFactory是否赋值,
     * 如果没有赋值则直接报错
     */
    public void afterPropertiesSet() {
        if (getSessionFactory() == null) {
            throw new IllegalArgumentException("Property 'sessionFactory' is required");
        }
    }
}
