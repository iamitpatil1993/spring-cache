package com.example.spring.cahce.configuration.data.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines/Overrides persistence unit properties for application managed EntityManagerFactory
 */
@Component
@Profile("dev")
public class AppManagedPersistenceUnitPropertySource implements PersistenceUnitPropertySource {

    @Autowired
    private Environment environment;

    /**
     * Provides persistence unit properties, which will be provided from external sources since,
     * we do not want to hard code in persistence.xml
     *
     * @return Map of persistence properties to be set/override.
     */
    @Override
    public Map<String, Object> getPropertyMap() {
        final Map<String, Object> properties = new HashMap<>(10);

        properties.put("javax.persistence.jdbc.url", environment.getProperty("datasource.url"));
        properties.put("javax.persistence.jdbc.user", environment.getProperty("DB_USERNAME"));
        properties.put("javax.persistence.jdbc.password", environment.getProperty("DB_PASSWORD"));

        return properties;
    }
}
