package gaea.access.hibernate.support;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.support.DaoSupport;
import org.springframework.orm.hibernate4.SessionFactoryUtils;

/**
 * Hibernate的Dao支持类
 *
 * @author wuhy
 */
public abstract class HibernateDaoSupport extends DaoSupport {

    @Autowired(required = false)
    private HibernateTemplate hibernateTemplate;

    @Override
    protected final void checkDaoConfig() {
        if (this.hibernateTemplate == null) {
            throw new IllegalArgumentException("'sessionFactory' or 'hibernateTemplate' is required");
        }
    }

    protected final Session getSession() throws DataAccessResourceFailureException, IllegalStateException {
        return this.hibernateTemplate.getSession();
    }

    protected final DataAccessException convertHibernateAccessException(HibernateException ex) {
        return this.hibernateTemplate.convertHibernateAccessException(ex);
    }

    protected final void releaseSession(Session session) {
        SessionFactoryUtils.closeSession(session);
    }

    public final void setSessionFactory(SessionFactory sessionFactory) {
        if (this.hibernateTemplate == null || sessionFactory != this.hibernateTemplate.getSessionFactory()) {
            this.hibernateTemplate = createHibernateTemplate(sessionFactory);
        }
    }

    protected HibernateTemplate createHibernateTemplate(SessionFactory sessionFactory) {
        return new HibernateTemplate(sessionFactory);
    }


    public final SessionFactory getSessionFactory() {
        return (this.hibernateTemplate != null ? this.hibernateTemplate.getSessionFactory() : null);
    }


    public final void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }


    public final HibernateTemplate getHibernateTemplate() {
        return this.hibernateTemplate;
    }

}

