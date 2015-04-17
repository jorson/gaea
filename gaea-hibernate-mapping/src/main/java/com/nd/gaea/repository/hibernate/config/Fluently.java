package com.nd.gaea.repository.hibernate.config;

import org.hibernate.cfg.Configuration;

/**
 *
 * @author jorson.WHY
 * @package com.nd.demo.config
 * @since 2015-04-09
 */
public class Fluently {

    public static FluentConfiguration configure() {
        return new FluentConfiguration();
    }

    public static FluentConfiguration configure(Configuration cfg) {
        return new FluentConfiguration(cfg);
    }
}
