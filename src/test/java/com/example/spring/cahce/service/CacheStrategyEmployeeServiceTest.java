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
}