package com.example.spring.cahce.service;

import com.example.spring.cahce.BaseTest;
import com.example.spring.cahce.model.Employee;
import com.example.spring.cahce.repository.EmployeeRepository;
import com.example.spring.cahce.stragegy.ReadOperation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
public class CacheStrategyEmployeeServiceTest extends BaseTest {

    @Autowired
    private CacheStrategyEmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    @Qualifier("read-around")
    private ReadOperation<Employee, Long> readAroundEmployeeReadOperation;

    @Autowired
    @Qualifier("read-through")
    private ReadOperation<Employee, Long> readThroughEmployeeReadOperation;


    @Test
    public void getEmployeeByIdWithCache() {
        // GIVEN
        final Employee employee = new Employee();
        employee.setFirstName("Jon");
        employee.setLastName("snow");

        employeeRepository.save(employee);

        // WHEN
        // this time it will hit database
        Optional<Employee> employeeGetResult = employeeService.getEmployeeByIdWithCache(employee.getId(), readAroundEmployeeReadOperation);

        // this time it will return data from cache
        employeeGetResult = employeeService.getEmployeeByIdWithCache(employee.getId(), readAroundEmployeeReadOperation);

        // THEN
        Assert.assertEquals(true, employeeGetResult.isPresent());
    }

    /**
     * test demonstrates READ-THROUGH cache strategy, it passes readThroughEmployeeREadOperation to service as a
     * strategy
     */
    @Test
    public void getEmployeeByIdWithReadThroughCacheStrategy() {
        // GIVEN
        final Employee employee = new Employee();
        employee.setFirstName("Jon");
        employee.setLastName("snow");

        employeeRepository.save(employee);

        // WHEN
        // this time it will hit database
        Optional<Employee> employeeGetResult = employeeService.getEmployeeByIdWithCache(employee.getId(), readThroughEmployeeReadOperation);

        // this time it will return data from cache
        employeeGetResult = employeeService.getEmployeeByIdWithCache(employee.getId(), readThroughEmployeeReadOperation);

        // THEN
        Assert.assertEquals(true, employeeGetResult.isPresent());
    }

    /**
     * Spring/Ehcache cache do not cach null value. If LoaderWriter returns null value it do not cache value as a NULL for
     * that key. So, for any key, we do not find any data in SOR (database) then we can simply return null from
     * {@link com.example.spring.cahce.stragegy.readwritethrough.EmployeeEntityLoaderWriter#load(Long)}, spring/ehcache
     * will not cache value as a null value
     */
    @Test
    public void getEmployeeByIdWithReadThroughCacheStrategyAndNullValueFromSor() {
        // GIVEN
        final Long empId = -1l;
        // WHEN
        // this time it will hit database, since database do not has emp with id -1, data will not be cached
        Optional<Employee> employeeGetResult = employeeService.getEmployeeByIdWithCache(empId, readThroughEmployeeReadOperation);

        // this will also go to database since last call result was null and was not cached
        employeeGetResult = employeeService.getEmployeeByIdWithCache(empId, readThroughEmployeeReadOperation);

        // THEN
        Assert.assertFalse(employeeGetResult.isPresent());
    }
}