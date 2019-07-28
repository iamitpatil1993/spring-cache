package com.example.spring.cahce.repository;

import com.example.spring.cahce.BaseTest;
import com.example.spring.cahce.model.Employee;
import com.example.spring.cahce.service.EmployeeSecondLevelCacheDemoService;
import org.hamcrest.Matchers;
import org.hibernate.stat.Statistics;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
public class EmployeeCustomRepositoryTest extends BaseTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeSecondLevelCacheDemoService cacheDemoService;

    @Autowired
    private Statistics hibernateStats;

    @Before
    @After
    @Override
    public void beforeTest() {
        super.beforeTest();

        // we can reset statistics when we want to, so resetting the statistics in order to have assertions on these statistics
        hibernateStats.clear();
    }

    @Test
    public void testPersist() {
        Employee employee = new Employee();
        employee.setFirstName("AMit");
        employee.setLastName("Patil");

        // when
        final Employee savedEmployee = employeeRepository.save(employee);

        //then
        Optional<Employee> findResult = employeeRepository.findById(savedEmployee.getId());
        assertTrue(findResult.isPresent());
    }

    /**
     * Test second level caching of Employee cache
     */
    @Test
    public void testGetEmployeeWithSecondLevelCache() {
        // GIVEN
        Employee employee = new Employee();
        employee.setFirstName("AMit");
        employee.setLastName("Patil");
        final Employee savedEmployee = employeeRepository.save(employee);

        // WHEN
        // call get entity by id, this time second level cache is has no entry for employee so, this will fetch
        // employee from database. Since transaction is at service level method, persistence context ends after this call.
        cacheDemoService.findEmployeeByIdWithSecondLevelCache(employee.getId());

        // THEN
        assertThat(hibernateStats.getSecondLevelCacheHitCount(), Matchers.is(Matchers.equalTo(0l)));
        assertThat(hibernateStats.getSecondLevelCacheMissCount(), Matchers.is(Matchers.equalTo(1l)));
        assertThat(hibernateStats.getSecondLevelCachePutCount(), Matchers.is(Matchers.equalTo(1l)));

        // WHEN
        // this time get will fetch entity from second level cache, NOTE: this time transaction is different so,
        // entity is not present in first level cache (persistence context)
        cacheDemoService.findEmployeeByIdWithSecondLevelCache(employee.getId());

        // THEN
        assertThat(hibernateStats.getSecondLevelCacheHitCount(), Matchers.is(Matchers.equalTo(1l)));
        assertThat(hibernateStats.getSecondLevelCacheMissCount(), Matchers.is(Matchers.equalTo(1l)));
        assertThat(hibernateStats.getSecondLevelCachePutCount(), Matchers.is(Matchers.equalTo(1l)));
    }
}