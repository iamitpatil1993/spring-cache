package com.example.spring.cahce.configuration.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author amit
 */
@Configuration
@EnableCaching
@Import(value = {RedisCacheProviderConfiguration.class})
public class SpringCacheConfiguration {

    /**
     * Conditionally declares bean in application context.
     */
    @Bean
    @Conditional(value = ConcurrentMapCacheProviderCondition.class)
    public CacheManager concurrentMapCacheManager() {
        return new ConcurrentMapCacheManager();
    }
}
