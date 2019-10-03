package com.example.spring.cahce.repository;

import com.example.spring.cahce.model.Configuration;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

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
    @Cacheable(cacheNames = "configuration", unless = "#result == null", condition = "#configKey != null")
    Configuration findByConfigKey(final String configKey);

    /**
     * {@link CacheEvict} can be used to remove entry from cache if  already exists.
     */
    @Override
    @CacheEvict(cacheNames = "configuration")
    void deleteById(Long id);

    /**
     * This method will remove all configuration entries from configuration cache.
     * {@link CacheEvict} provides addition attribute allEntries to delete all entries from cache. Which is useful when,
     * 1. We want to delete all records of that type from database/dataSource and from cache
     * 2. We update all entries of that type from database/dataSource and from cache.
     */
    @Override
    @CacheEvict(cacheNames = "configuration", allEntries = true)
    void deleteAll();
}
