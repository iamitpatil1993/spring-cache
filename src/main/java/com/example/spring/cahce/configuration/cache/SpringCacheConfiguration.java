package com.example.spring.cahce.configuration.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * @author amit
 */
@Configuration
@EnableCaching
@Import(value = {RedisCacheProviderConfiguration.class, EhcacheCacheProviderConfiguration.class})
public class SpringCacheConfiguration {

    /**
     * Conditionally declares bean in application context.
     */
    @Bean
    @Conditional(value = ConcurrentMapCacheProviderCondition.class)
    public CacheManager concurrentMapCacheManager() {
        return new ConcurrentMapCacheManager();
    }

    /**
     * {@link SimpleCacheManager} as implementation of spring {@link CacheManager}. This cacheManager takes list of
     * caches and uses them for caching.
     * We can use java 8 Optional to declare dependency as a Optional dependency
     *
     * @return simpleCacheManager as IMPL of spring {@link CacheManager}
     */
    @Bean
    @Conditional(value = SimpleCacheProviderCondition.class)
    public CacheManager simpleCacheManager(final Optional<EhCacheManagerFactoryBean> cacheManagerFactoryBean) {
        final SimpleCacheManager simpleCacheManager = new SimpleCacheManager();

        // create list of caches (this list can contain any implementation if org.springframework.cache.Cache interface)
        final Collection<Cache> caches = new ArrayList<>(2);
        caches.add(new ConcurrentMapCache("EmployeeCache")); // Normal Map based cache

        // adds ehcache as a cache
       /* final Ehcache employeeCache = cacheManagerFactoryBean.getObject().getEhcache("anyOtherCache");
        caches.add(new EhCacheCache(employeeCache));*/

        // set caches to be used on SimpleCacheManager
        simpleCacheManager.setCaches(caches);

        return simpleCacheManager;
    }

    /**
     * Spring provides easy way to disable cache. {@link NoOpCacheManager} implementation of {@link CacheManager}
     * can be used as a cache provider to disable cache, without single line code change in application.
     *
     * @return org.springframework.cache.support.NoOpCacheManager as IMPL of spring {@link CacheManager}
     */
    @Bean
    @Conditional(value = NoOpCacheConditon.class)
    public CacheManager noOpCacheManager() {
        return new NoOpCacheManager();
    }
}
