package com.example.spring.cahce.configuration.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

/**
 * @author amit
 * This is custom Error handler for cache, we can do any random operation on cache operation failure.
 * I tried, stoping cache server (redis) in the middle of applicationn, then spring calls {@link DefaultCacheErrorHandler#handleCacheGetError(RuntimeException, Cache, Object)}
 * Then I again started cache server (redis), so spring connected to cache.
 * So, these handlers can be very usefult to handle errors from cache (even connectivity as well). So, in production,
 * if cache server goes down, then in order to handle cache errors we should provide this class and handle error, so that
 * cache failure should not fail application.
 * <p>
 * NOTE: If we do not throw anything from these methods then spring will continue to call annoteted method. So, in our case
 * we are just logging/raising alert so annoteted methods will get called even cache operation fails.
 */
public class DefaultCacheErrorHandler implements CacheErrorHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(DefaultCacheErrorHandler.class);

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        LOGGER.error("Cache Get error for cache :: {}, key :: {}, error", new Object[]{cache.getName(), key});
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {

    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {

    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {

    }
}
