package com.example.spring.cahce.service;

import com.example.spring.cahce.BaseTest;
import com.example.spring.cahce.model.Configuration;
import com.example.spring.cahce.repository.ConfigurationRepository;
import org.hamcrest.Matchers;
import org.hibernate.stat.Statistics;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author amit
 */
@RunWith(SpringRunner.class)
public class ConfigurationServiceTest extends BaseTest {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private Statistics hibernateStats;

    @Before
    @After
    @Override
    public void beforeTest() {
        super.beforeTest();

        // we can reset statistics when we want to, so resetting the statistics in order to have assertions on these statistics
        hibernateStats.clear();
    }


    //@Test
    public void testFindById() {
        // GIVEN
        final Configuration configuration = new Configuration();
        final String cacheKey = UUID.randomUUID().toString();
        configuration.setConfigKey(cacheKey);
        configuration.setConfigValue("false");

        // entity with READ_ONLY cache strategy is allowed to be inserted by not update
        configurationRepository.save(configuration);

        // WHEN
        Optional<Configuration> configByConfigId = configurationService.findById(configuration.getId());

        // THEN
        assertThat(configByConfigId.isPresent(), is(true));
        assertThat(hibernateStats.getSecondLevelCacheMissCount(), is(1l));
        assertThat(hibernateStats.getSecondLevelCachePutCount(), is(1l));
    }

    /**
     * entity with READ_ONLY cache strategy is allowed to be inserted by not update
     */
    @Test(expected = TransactionSystemException.class)
    public void testUpdate() {
        // GIVEN
        final Configuration configuration = new Configuration();
        final String cacheKey = UUID.randomUUID().toString();
        configuration.setConfigKey(cacheKey);
        configuration.setConfigValue("false");
        configurationRepository.save(configuration);

        // WHEN
        // update operation will fail since entity is cached with READ_ONLY strategy
        configuration.setConfigValue(UUID.randomUUID().toString());
        configurationService.update(configuration);
    }

    /**
     * Only entity get by Id calls check for cache.
     */
    @Test
    public void testFindByConfigKey() {
        // GIVEN
        final Configuration configuration = new Configuration();
        final String cacheKey = UUID.randomUUID().toString();
        configuration.setConfigKey(cacheKey);
        configuration.setConfigValue("false");

        // entity with READ_ONLY cache strategy is allowed to be inserted by not update
        configurationRepository.save(configuration);

        // WHEN
        // this fetch operation by config key, will not check for second level cache, only fetch by PK check for cache entry
        Optional<Configuration> configByConfigKey = configurationService.findByConfigKey(configuration.getConfigKey());

        // THEN
        assertThat(configByConfigKey.isPresent(), is(true));
        assertThat(hibernateStats.getSecondLevelCacheMissCount(), is(0l));
        assertThat(hibernateStats.getSecondLevelCachePutCount(), is(1l)); // but cache miss is cached.
    }
}