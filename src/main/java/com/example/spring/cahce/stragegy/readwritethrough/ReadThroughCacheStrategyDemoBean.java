package com.example.spring.cahce.stragegy.readwritethrough;

import com.example.spring.cahce.model.Employee;
import com.example.spring.cahce.stragegy.ReadOperation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.cache.Cache;
import javax.cache.CacheManager;
import java.util.Optional;

/**
 * @author amit
 * <p>
 * ReadOperation implementation that reads data from SOR using Read through cache strategy.
 */
@Component
@Qualifier("read-through")
public class ReadThroughCacheStrategyDemoBean implements InitializingBean, ReadOperation<Employee, Long> {

    private CacheManager cacheManager;

    private Cache<Long, Employee> employeeCache;

    @Autowired
    public ReadThroughCacheStrategyDemoBean(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * In case of read-through cache strategy, we directly read data from cache and not from repository, cache checks
     * data found in cache or not, if found, it returns data from cache, otherwise uses CacheLoaderWriter implementation
     * internally to load data from database and puts data into cache and returns data.
     */
    @Override
    public Optional<Employee> read(Long id) {
        // note, here we are reading data directly from cache.
        return Optional.ofNullable(employeeCache.get(id));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(cacheManager, "cacheManager injected null");

        employeeCache = cacheManager.getCache("employeeEntityCache", Long.class, Employee.class);

        Assert.notNull(employeeCache, "employeeCache not found in cahce manager");
    }
}
