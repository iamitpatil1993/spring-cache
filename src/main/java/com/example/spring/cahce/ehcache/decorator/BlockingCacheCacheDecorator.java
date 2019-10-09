package com.example.spring.cahce.ehcache.decorator;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.constructs.CacheDecoratorFactory;
import net.sf.ehcache.constructs.blocking.BlockingCache;

import java.util.Properties;

/**
 * This is Ehcache CacheDecoratorFactory implementation to provide BlockingCache, to avoid 'Thundering herd' problem
 * using read-through/write-through cache stragegies
 */
public class BlockingCacheCacheDecorator extends CacheDecoratorFactory {

    @Override
    public Ehcache createDecoratedEhcache(Ehcache cache, Properties properties) {
        return new BlockingCache(cache); // Wraps Ehcache into BlockingCache in order to block concurrent load operation
    }

    @Override
    public Ehcache createDefaultDecoratedEhcache(Ehcache cache, Properties properties) {
        return new BlockingCache(cache);
    }
}
