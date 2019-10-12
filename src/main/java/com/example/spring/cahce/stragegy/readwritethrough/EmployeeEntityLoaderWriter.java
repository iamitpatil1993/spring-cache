package com.example.spring.cahce.stragegy.readwritethrough;

import com.example.spring.cahce.model.Employee;
import com.example.spring.cahce.repository.EmployeeRepository;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.cache.integration.CacheLoaderException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This is cache loader class used to implement read-through cache strategy.
 * <p>
 * <p>
 * This class implement EhCache SPI to plugin logic to read data from SOR (database).
 * * Ehcache will use this class when data not found in cache, and will use methods to get data from datastore
 * * and put it into cache.
 * </p>
 * <p>
 * NOTE: Ehcache new version have merged CacheLoader and Cachewriter into this single interface CacheLoaderWriter
 *
 * @author amit
 */
@Component
public class EmployeeEntityLoaderWriter implements CacheLoaderWriter<Long, Employee>, InitializingBean {

    private EmployeeRepository employeeRepository;

    public EmployeeEntityLoaderWriter() {
    }

    @Autowired
    public EmployeeEntityLoaderWriter(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * This load will be called by cache internally if data for key is not found in cache. We should implement this and
     * provide logic to load data from data store, for which here are using spring repository.
     */
    @Override
    public Employee load(Long id) throws CacheLoaderException {
        final Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            return employee.get();
        }
        return null;
    }

    /**
     * Same here
     */
    @Override
    public Map<Long, Employee> loadAll(Iterable<? extends Long> keys) throws CacheLoaderException {
        Set<Long> ids = new HashSet<>();
        keys.forEach(ids::add);

        final List<Employee> allById = employeeRepository.findAllById(ids);
        return allById.stream().collect(Collectors.toMap(Employee::getId, employee -> employee));
    }

    /**
     * In order to use write-through/write-behind we must have key available before saving data in database, which is not possible
     * if we are using database generated primary keys (like IDENTITY, Sequence), so write-through/write-behind is not much
     * feasible strategy.
     *
     */
    @Override
    public void write(Long key, Employee value) throws Exception {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void delete(Long key) throws Exception {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void writeAll(Iterable<? extends Map.Entry<? extends Long, ? extends Employee>> entries) throws BulkCacheWritingException, Exception {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteAll(Iterable<? extends Long> keys) throws BulkCacheWritingException, Exception {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(employeeRepository, "employeeRepository injected null");
    }
}
