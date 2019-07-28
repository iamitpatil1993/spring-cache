package com.example.spring.cahce.configuration.data.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines persistence unit properties for container managed persistence unit. (Programmatically configured PU)
 *
 * @author amit
 */
@Component
@Profile(value = {"int", "prod"})
public class ContainerManagedPersistenceUnitPropertySource implements PersistenceUnitPropertySource {

    private Environment environment;

    @Autowired
    public ContainerManagedPersistenceUnitPropertySource(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Map<String, Object> getPropertyMap() {
        final Map<String, Object> persistenceUnitProperties = new HashMap<>(10);

        persistenceUnitProperties.put("hibernate.cache.use_second_level_cache", "true");
        persistenceUnitProperties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.jcache.JCacheRegionFactory");
        persistenceUnitProperties.put("hibernate.javax.cache.provider", "org.ehcache.jsr107.EhcacheCachingProvider");
        persistenceUnitProperties.put("hibernate.generate_statistics", "true");

        // we can set shared cache mode using property as well.
        persistenceUnitProperties.put("javax.persistence.sharedCache.mode", "ENABLE_SELECTIVE");

        return persistenceUnitProperties;
    }
}
