package com.example.spring.cahce.configuration.cache;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.xml.XmlConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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
}
