package com.example.spring.cahce.configuration.cache;

import com.example.spring.cahce.ehcache.Employee;
import com.example.spring.cahce.stragegy.readwritethrough.EmployeeEntityLoaderWriter;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.ehcache.xml.XmlConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author amit
 */
@Configuration
public class EhcacheCacheProviderConfiguration {

    @Autowired
    private EmployeeEntityLoaderWriter employeeEntityLoaderWriter;


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
     * <p>
     * NoTE: We can set CacheLoaderWriter in xml configuration file but, ehcache will create instance of that class
     * and hence we need to provide default constructor in that CacheLoaderWriter implementation, and also we can not
     * have spring DI there, because instance is created by ehcache.
     * So, in order to set full spring initialized bean to ehcache, we need to override the configuration set in XML
     * file, which are doing here.
     * NOTE: This is called 'Configuration Derivation', which is new feature added in ehcache 3.8.0
     * refer
     * <a href = "https://groups.google.com/forum/#!category-topic/ehcache-users/ehcache-jcache-provider/6V6uoMdHtgs">Configuration Derivation</a> <br>
     */
    @Bean
    @Qualifier("jcachexml")
    public javax.cache.CacheManager jCacheCacheManager() {
        /*
         This scans the classpath for Jcache implantation, and returns. So, must have exactly one cache provider in
         classpath
         CacheProvider is used to get CacheManager implementation of cache provider, Default classloader and Default URI
        */
        // if there are multiple cache provider implementations in classpath, we can provider fully qualified class
        // name of cache provider implementation class. In this case 'org.ehcache.jsr107.EhcacheCachingProvider'
        final XmlConfiguration xmlConfiguration = new XmlConfiguration(getClass().getResource("/ehcache-jsr107-config.xml"));
        CacheConfiguration modifiedEmployeeCacheConfiguration = xmlConfiguration.getCacheConfigurations().get("employeeEntityCache");

        // override cache configuration (specific to employee cache) from xml (Set  CacheLoderWriter)
        modifiedEmployeeCacheConfiguration = modifiedEmployeeCacheConfiguration
                .derive()
                .withLoaderWriter(employeeEntityLoaderWriter).build();

        // get updated configuration (at CacheManager level)
        org.ehcache.config.Configuration modifiedConfiguration = xmlConfiguration
                .derive()
                .withCache("employeeEntityCache", modifiedEmployeeCacheConfiguration)
                .build();

        // get CacheManager (Jcache) with above updated configuration
        final EhcacheCachingProvider ehcacheCachingProvider = (EhcacheCachingProvider) Caching.getCachingProvider();
        final javax.cache.CacheManager manager;
        try {
            manager = ehcacheCachingProvider.getCacheManager(getClass().getResource("/ehcache-jsr107-config.xml").toURI(), modifiedConfiguration);
            Cache<Long, com.example.spring.cahce.model.Employee> employeeEntityCache = manager
                    .getCache("employeeEntityCache", Long.class, com.example.spring.cahce.model.Employee.class);

            return manager;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    //@Bean
    //@Qualifier("jcacheprogrammatic")
    public javax.cache.CacheManager programmaticJcacheCacheManager() {
        // we can provided fullyQualifiedClass name of CachingProvider implementation
        final CachingProvider cachingProvider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");

        // get cache manager using default ClassLoader, URI
        javax.cache.CacheManager cacheManager = null;
        try {
            cacheManager = cachingProvider.getCacheManager(
                    getClass().getResource("/ehcache-jsr107-config-1.xml").toURI(),
                    getClass().getClassLoader());

            // create configuration for cache
            // NOTE: jcache spec do not define way to configure onheap, offheap or disk entries or size related configuration.
            // In order to configure those details we will have to use provider specific APIs and configurations.
            final MutableConfiguration<String, Employee> employeeCacheConfiguration = new MutableConfiguration<String, Employee>()
                    .setTypes(String.class, Employee.class)
                    .setStoreByValue(false) // Store values into cache by reference instead of value (which is default). I could not found XML counterpart of this
                    .setManagementEnabled(true)
                    .setStatisticsEnabled(true)
                    .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));

            // create cache
            cacheManager.createCache("employeeCache", employeeCacheConfiguration);
            return cacheManager;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
