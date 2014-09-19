package gaea.access.hibernate.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import javax.sql.DataSource;

/**
 * Hibernate Session Factory的工具类
 */
public class HibernateSessionFactoryUtils {
    private static final Log logger = LogFactory.getLog(HibernateSessionFactoryUtils.class);

    /**
     * Create an appropriate SQLExceptionTranslator for the given SessionFactory.
     * If a DataSource is found, a SQLErrorCodeSQLExceptionTranslator for the DataSource
     * is created; else, a SQLStateSQLExceptionTranslator as fallback.
     *
     * @param sessionFactory the SessionFactory to create the translator for
     * @return the SQLExceptionTranslator
     * @see org.springframework.orm.hibernate4.SessionFactoryUtils#getDataSource
     * @see org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator
     * @see org.springframework.jdbc.support.SQLStateSQLExceptionTranslator
     */
    public static SQLExceptionTranslator newJdbcExceptionTranslator(SessionFactory sessionFactory) {
        DataSource ds = SessionFactoryUtils.getDataSource(sessionFactory);
        if (ds != null) {
            return new SQLErrorCodeSQLExceptionTranslator(ds);
        }
        return new SQLStateSQLExceptionTranslator();
    }

    /**
     * Apply the current transaction timeout, if any, to the given
     * Hibernate Query object.
     *
     * @param query          the Hibernate Query object
     * @param sessionFactory Hibernate SessionFactory that the Query was created for
     *                       (may be {@code null})
     * @see org.hibernate.Query#setTimeout
     */
    public static void applyTransactionTimeout(Query query, SessionFactory sessionFactory) {
        Assert.notNull(query, "No Query object specified");
        if (sessionFactory != null) {
            SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
            if (sessionHolder != null && sessionHolder.hasTimeout()) {
                query.setTimeout(sessionHolder.getTimeToLiveInSeconds());
            }
        }
    }

    /**
     * Apply the current transaction timeout, if any, to the given
     * Hibernate Criteria object.
     *
     * @param criteria       the Hibernate Criteria object
     * @param sessionFactory Hibernate SessionFactory that the Criteria was created for
     * @see org.hibernate.Criteria#setTimeout
     */
    public static void applyTransactionTimeout(Criteria criteria, SessionFactory sessionFactory) {
        Assert.notNull(criteria, "No Criteria object specified");
        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
        if (sessionHolder != null && sessionHolder.hasTimeout()) {
            criteria.setTimeout(sessionHolder.getTimeToLiveInSeconds());
        }
    }

}
