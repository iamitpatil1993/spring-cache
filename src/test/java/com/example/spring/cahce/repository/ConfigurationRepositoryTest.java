package com.example.spring.cahce.repository;

import com.example.spring.cahce.BaseTest;
import com.example.spring.cahce.model.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
public class ConfigurationRepositoryTest extends BaseTest {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private CacheManager cacheManager;
    private Cache configurationCache;

    @Before
    public void before() {
        configurationCache = cacheManager.getCache("configuration");
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
}