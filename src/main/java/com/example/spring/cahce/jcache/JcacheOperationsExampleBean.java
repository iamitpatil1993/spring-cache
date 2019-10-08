package com.example.spring.cahce.jcache;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.cache.CacheManager;
import java.util.Optional;

@Component
public class JcacheOperationsExampleBean implements InitializingBean {

    private CacheManager cacheManager;
    private javax.cache.Cache<String, String> basicTestCache;

    @Autowired
    public JcacheOperationsExampleBean(CacheManager cacheManager) {
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
        Assert.notNull(cacheManager, "No Jcache CacheManager found");
        this.basicTestCache = cacheManager.getCache("basicTestCache");
        Assert.notNull(basicTestCache, "basicTestCache not found in cache");
    }
}
