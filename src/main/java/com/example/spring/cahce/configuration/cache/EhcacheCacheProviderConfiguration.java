package com.example.spring.cahce.configuration.cache;

import com.example.spring.cahce.ehcache.Employee;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.xml.XmlConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.net.URISyntaxException;
import java.net.URL;

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

    /**
     * Xml based configuration for CacheManager and Cache used in application.
     * This cache manager can be used in exactly same way as we used 'prorammatically' configured cache manager.
     *
     * @return
     */
    @Bean
    @Qualifier(value = "xml")
    @Primary
    public CacheManager ehcachNativeXmlCacheManager() {
        final URL myUrl = getClass().getResource("/ehcache.xml");
        final XmlConfiguration xmlConfig = new XmlConfiguration(myUrl);
        final CacheManager cacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        cacheManager.init();
        return cacheManager;
    }

    /**
     * Configure javax.cache.CacheManager implementation.
     *
     * @return javax CacheManager, which can be used in application code, which will remove dependency on implementation
     * specific APIs in application code.
     */
    @Bean
    public javax.cache.CacheManager jCacheCacheManager() {
        /*
         This scans the classpath for Jcache implantation, and returns. So, must have exactly one cache provider in
         classpath
         CacheProvider is used to get CacheManager implementation of cache provider, Default classloader and Default URI
        */
        CachingProvider cachingProvider = Caching.getCachingProvider();
        try {
            javax.cache.CacheManager manager = cachingProvider.getCacheManager(
                    getClass().getResource("/ehcache-jsr107-config.xml").toURI(),
                    getClass().getClassLoader());
            return manager;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }


}
