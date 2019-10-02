package com.example.spring.cahce.configuration.data.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines persistence unit propeties for {@link org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean}
 */
@Component
@Profile(value = {"int, prod"})
public class ContainerManagedPersistenceUnitPropertySource implements PersistenceUnitPropertySource {

    @Autowired
    private Environment environment;

    @Override
    public Map<String, Object> getPropertyMap() {
        final Map<String, Object> properties = new HashMap<>(1);

        final String generateStats = environment.getProperty("generate_statistic");
        if (generateStats != null && !generateStats.isEmpty()) {
            properties.put("hibernate.generate_statistic", "true");
        }
        return properties;
    }
}
