import com.nd.gaea.repository.hibernate.config.FluentConfiguration;
import com.nd.gaea.repository.hibernate.config.FluentConfigurationException;
import com.nd.gaea.repository.hibernate.config.Fluently;
import com.nd.gaea.repository.hibernate.config.MappingConfiguration;
import map.UserMap;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author jorson.WHY
 * @package com.nd.demo
 * @since 2015-04-17
 */
public class ConfigurationTest {

    @Test
    public void configTest() throws FluentConfigurationException {
        Configuration hbmConfig = new Configuration();
        hbmConfig.configure();

        SessionFactory sessionFactory = Fluently.configure(hbmConfig).mappings(new FluentConfiguration.BuildMappingConfiguration() {
            @Override
            public void build(MappingConfiguration configuration) {
                configuration.getFluentMappingsContainer().addClass(new Class[]{UserMap.class});
            }
        }).buildSessionFactory();

        Assert.assertNotNull(sessionFactory);
    }
}
