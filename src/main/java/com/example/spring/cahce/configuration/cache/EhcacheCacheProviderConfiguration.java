package com.example.spring.cahce.configuration.cache;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author amit
 */
@Configuration
public class EhcacheCacheProviderConfiguration {

    @Bean
    @Qualifier("programmatic")
    public CacheManager ehcacheNativeProgrammaticCacheManager() {
        // create cache manager
        final CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);

        // create configuration for 'basicTestCache' cache
        CacheConfigurationBuilder<String, String> cacheConfiguration = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(100));

        // create cache programmatically.
        cacheManager.createCache("basicTestCache", cacheConfiguration);

        return cacheManager;
    }
}
