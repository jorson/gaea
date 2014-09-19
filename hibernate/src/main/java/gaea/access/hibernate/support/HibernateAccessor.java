package gaea.access.hibernate.support;

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
 * @author wuhy
 */
public class HibernateAccessor implements InitializingBean {

    private SessionFactory sessionFactory;

    private SQLExceptionTranslator jdbcExceptionTranslator;

    private SQLExceptionTranslator defaultJdbcExceptionTranslator;

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


    /**
     * 把HibernateException转换成DataAccessException
     *
     * @param ex 发生的HibernateException
     * @return 对应的DataAccessException
     * @see org.springframework.orm.hibernate4.SessionFactoryUtils#convertHibernateAccessException
     * @see #setJdbcExceptionTranslator
     */
    public DataAccessException convertHibernateAccessException(HibernateException ex) {
        if (getJdbcExceptionTranslator() != null && ex instanceof JDBCException) {
            return convertJdbcAccessException((JDBCException) ex, getJdbcExceptionTranslator());
        } else if (GenericJDBCException.class.equals(ex.getClass())) {
            return convertJdbcAccessException((GenericJDBCException) ex, getDefaultJdbcExceptionTranslator());
        }
        return SessionFactoryUtils.convertHibernateAccessException(ex);
    }

    /**
     * 通过SQLExceptionTranslator，转换 Hibernate JDBCException
     *
     * @param ex         发生的HibernateException
     * @param translator 使用的SQLExceptionTranslator
     * @return 对应的DataAccessException
     */
    protected DataAccessException convertJdbcAccessException(JDBCException ex, SQLExceptionTranslator translator) {
        return translator.translate("Hibernate operation: " + ex.getMessage(), ex.getSQL(), ex.getSQLException());
    }

    /**
     * 通过默认的SQLExceptionTranslator，转换 Hibernate JDBCException
     *
     * @param ex 发生的HibernateException
     * @return 对应的DataAccessException
     */
    protected DataAccessException convertJdbcAccessException(SQLException ex) {
        SQLExceptionTranslator translator = getJdbcExceptionTranslator();
        if (translator == null) {
            translator = getDefaultJdbcExceptionTranslator();
        }
        return translator.translate("Hibernate-related JDBC operation", null, ex);
    }

    /**
     * 取得默认的JDBC Exception的转换类实例
     * {@link org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator}
     */
    protected synchronized SQLExceptionTranslator getDefaultJdbcExceptionTranslator() {
        if (this.defaultJdbcExceptionTranslator == null) {
            this.defaultJdbcExceptionTranslator = HibernateSessionFactoryUtils.newJdbcExceptionTranslator(getSessionFactory());
        }
        return this.defaultJdbcExceptionTranslator;
    }

    public void setJdbcExceptionTranslator(SQLExceptionTranslator jdbcExceptionTranslator) {
        this.jdbcExceptionTranslator = jdbcExceptionTranslator;
    }

    /**
     * 返回JDBC Exception的转换类实例。
     */
    public SQLExceptionTranslator getJdbcExceptionTranslator() {
        return this.jdbcExceptionTranslator;
    }

}
