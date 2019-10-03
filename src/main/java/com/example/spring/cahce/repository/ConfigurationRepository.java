package com.example.spring.cahce.repository;

import com.example.spring.cahce.model.Configuration;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {

    /**
     * we can override method declaration in {@link JpaRepository} and enable caching for that method.
     * Since configurations will be master data, we can cache this data while saving. So, we can avoid cache miss
     * at first time access of configuration.
     * NOTE: We can use SpEL to customize they key for cache, since while saving cache, method argument do not have id,
     * rather return value has id/
     * @CachePut can be used to load data in cache in advance before it's use.
     */
    @Override
    @CachePut(cacheNames = "configuration", key = "#result.id")
    Configuration save(final Configuration entity);

    /**
     * we can use unless to disable caching of data if SpEL evaluates to true. Here we are caching data unless it is null.
     * We can use same attribute in similar way with @{@link CachePut}
     */
    @Cacheable(cacheNames = "configuration", unless = "#result == null")
    Configuration findByConfigKey(final String configKey);
}
