package com.example.spring.cahce.ehcache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * This bean demonstrates the simple caching operations on cache using EhCache specific CacheNManager.
 *
 * @author amit
 */
@Component
public class BasicProgrammaticExampleBean implements InitializingBean {

    private CacheManager cacheManager;
    private Cache<String, String> basicTestCache;

    @Autowired
    public BasicProgrammaticExampleBean(@Qualifier("programmatic") CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void saveToCache(final String key, final String value) {
        basicTestCache.put(key, value);
    }

    public Optional<String> getFromCache(final String key) {
        return Optional.ofNullable(basicTestCache.get(key));
    }

    public void deleteFromCache(final String key) {
        basicTestCache.remove(key);
    }

    public void clearCache() {
        basicTestCache.clear();
    }

    public Boolean containsKey(final String key) {
        return basicTestCache.containsKey(key);
    }

    public void update(final String key, final String newValue) {
        basicTestCache.replace(key, newValue);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(cacheManager, "Ehcache native Cachemanager injected null");

        basicTestCache = cacheManager.getCache("basicTestCache", String.class, String.class);
        Assert.notNull(cacheManager, "No cache found by name 'basicTestCache' in CacheManager!");
    }


}


