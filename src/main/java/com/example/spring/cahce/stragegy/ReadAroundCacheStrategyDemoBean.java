package com.example.spring.cahce.stragegy;

import com.example.spring.cahce.model.Employee;
import com.example.spring.cahce.repository.EmployeeRepository;
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
 * ReadOperation implementation that reads data from SOR using Read around cache strategy.
 */
@Component
@Qualifier("read-around")
public class ReadAroundCacheStrategyDemoBean implements InitializingBean, ReadOperation<Employee, Long> {

    private EmployeeRepository employeeRepository;

    private CacheManager cacheManager;

    private Cache<Long, Employee> employeeCache;

    @Autowired
    public ReadAroundCacheStrategyDemoBean(EmployeeRepository employeeRepository, CacheManager cacheManager) {
        this.employeeRepository = employeeRepository;
        this.cacheManager = cacheManager;
    }

    /**
     * uses read-around cache strategy to fetch data.
     */
    @Override
    public Optional<Employee> read(Long id) {
        // check employee already exists in cache
        final Employee employeeFromCache = employeeCache.get(id);

        // return if exists in cache
        if (employeeFromCache != null) {
            return Optional.of(employeeFromCache);
        }

        // get employee from SOR (database)
        Optional<Employee> employeeFromSor = employeeRepository.findById(id);

        /*
         we should not cache null value, because currently identity for employee is 100 and id passed to this method
         is 200 then, if we cache null for 200, then when employee with id 200 created, it will never get cached,
         and this method will always return employee with 200 id from cache as a null value (unless we have set eviction).
        */
        if (!employeeFromSor.isPresent()) {
            return Optional.empty();
        }
        // cache employee for future use
        employeeCache.put(employeeFromSor.get().getId(), employeeFromSor.get());
        return employeeFromSor;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(employeeRepository, "employeeRepository injected null");
        Assert.notNull(cacheManager, "cacheManager injected null");

        employeeCache = cacheManager.getCache("employeeEntityCache", Long.class, Employee.class);
    }
}
