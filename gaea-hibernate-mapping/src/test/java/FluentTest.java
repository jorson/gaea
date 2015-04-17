import com.nd.gaea.repository.hibernate.config.FluentConfiguration;
import com.nd.gaea.repository.hibernate.config.FluentConfigurationException;
import com.nd.gaea.repository.hibernate.config.Fluently;
import com.nd.gaea.repository.hibernate.config.MappingConfiguration;
import entry.User;
import map.UserMap;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author jorson.WHY
 * @package com.nd.demo
 * @since 2015-04-09
 */
public class FluentTest {

    private static SessionFactory sessionFactory;

    @BeforeClass
    public static void setup() throws FluentConfigurationException {
        Configuration hbmConfig = new Configuration();
        hbmConfig.configure();

        sessionFactory = Fluently.configure(hbmConfig).mappings(new FluentConfiguration.BuildMappingConfiguration() {
            @Override
            public void build(MappingConfiguration configuration) {
                configuration.getFluentMappingsContainer().addClass(new Class[]{UserMap.class});
            }
        }).buildSessionFactory();

        Assert.assertNotNull(sessionFactory);
    }

    @Test
    public void doSomeJob() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = new User();
        user.setName("AAA");
        user.setAge(12);
        session.save(user);
        session.getTransaction().commit();
    }

    @AfterClass
    public static void clear() {
        if(sessionFactory != null) {
            sessionFactory.close();;
            sessionFactory = null;
        }
    }
}
