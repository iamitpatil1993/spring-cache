package com.example.spring.cahce.repository;

import com.example.spring.cahce.BaseTest;
import com.example.spring.cahce.model.Configuration;
import org.hibernate.stat.Statistics;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
public class ConfigurationRepositoryTest extends BaseTest {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private Statistics hibernateStats;

    private Cache configurationCache;

    @Before
    public void before() {
        configurationCache = cacheManager.getCache("configuration");
        hibernateStats.clear();
        super.beforeTest();
    }

    @Test
    public void testSave() {

        // GIVEN
        final String key = UUID.randomUUID().toString();
        final String value = UUID.randomUUID().toString();

        final Configuration configuration = new Configuration();
        configuration.setConfigKey(key);
        configuration.setConfigValue(value);

        // WHEN
        final Configuration savedConfiguration = configurationRepository.save(configuration);

        // THEN
        // since we have used @CachePut, data will get cached when save method completes
        assertNotNull(configurationCache.get(savedConfiguration.getId(), Configuration.class));
    }

    @Test
    public void testFindByConfigKeyWithConfigKeyNotExists() {
        // GIVEN
        final String configKey = UUID.randomUUID().toString();

        // WHEN
        // since caching of null value is disable, this method execution will not cache null value to cache
        configurationRepository.findByConfigKey(configKey);

        // this will again go to database since data is not cached
        configurationRepository.findByConfigKey(configKey);

        // THEN
        assertEquals(2, hibernateStats.getQueryExecutionCount());
    }

    @Test
    public void testFindByConfigKeyWithNullCacheKey() {
        // GIVEN
        final String configKey = null;

        // WHEN
        // since cache will not be checked if passed argument is null, this call skip the cache and will got to database
        configurationRepository.findByConfigKey(configKey);

        // this will again go to database since data is not cached
        configurationRepository.findByConfigKey(configKey);

        // THEN
        assertEquals(2, hibernateStats.getQueryExecutionCount());
    }
}