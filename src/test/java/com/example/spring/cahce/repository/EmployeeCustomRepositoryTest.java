package com.example.spring.cahce.repository;

import com.example.spring.cahce.BaseTest;
import com.example.spring.cahce.model.Employee;
import com.example.spring.cahce.service.EmployeeSecondLevelCacheDemoService;
import org.hibernate.stat.Statistics;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
        assertThat(hibernateStats.getSecondLevelCacheHitCount(), is(equalTo(0l)));
        assertThat(hibernateStats.getSecondLevelCacheMissCount(), is(equalTo(1l)));
        assertThat(hibernateStats.getSecondLevelCachePutCount(), is(equalTo(1l)));

        // WHEN
        // this time get will fetch entity from second level cache, NOTE: this time transaction is different so,
        // entity is not present in first level cache (persistence context)
        cacheDemoService.findEmployeeByIdWithSecondLevelCache(employee.getId());

        // THEN
        assertThat(hibernateStats.getSecondLevelCacheHitCount(), is(equalTo(1l)));
        assertThat(hibernateStats.getSecondLevelCacheMissCount(), is(equalTo(1l)));
        assertThat(hibernateStats.getSecondLevelCachePutCount(), is(equalTo(1l)));
    }

    /**
     * Entity update operation updates second level cache as well.
     * Entity update operation fetches entity from cache, if available.
     */
    @Test
    public void testUpdateEntityWithSecondLevelCache() {
        // GIVEN
        Employee employee = new Employee();
        employee.setFirstName("AMit");
        employee.setLastName("Patil");
        // this will persist entity to database, so no cache operations
        final Employee savedEmployee = cacheDemoService.updateEmployeeWithSecondLevelCache(employee);

        // this will load entity into second level cache, 1 cache miss, 1 cache put, 0 cache hit
        cacheDemoService.findEmployeeByIdWithSecondLevelCache(savedEmployee.getId());
        assertThat(hibernateStats.getSecondLevelCacheMissCount(), is(equalTo(1l)));
        assertThat(hibernateStats.getSecondLevelCachePutCount(), is(equalTo(1l)));
        assertThat(hibernateStats.getSecondLevelCacheHitCount(), is(equalTo(0l)));

        // update employee, which should update employee into second level cache as well.
        final String updatedFirstName = UUID.randomUUID().toString();
        savedEmployee.setFirstName(updatedFirstName);
        cacheDemoService.updateEmployeeWithSecondLevelCache(savedEmployee);
        assertThat(hibernateStats.getSecondLevelCacheHitCount(), is(equalTo(1l))); // hit increased by 1 since entity loaded from cache
        assertThat(hibernateStats.getSecondLevelCachePutCount(), is(equalTo(2l))); // put increased by 1 due to update
        assertThat(hibernateStats.getSecondLevelCacheMissCount(), is(equalTo(1l)));

        // get employee, which will directly fetch from cache, since it is already cached
        Optional<Employee> updatedEmployeeFromCache = cacheDemoService.findEmployeeByIdWithSecondLevelCache(savedEmployee.getId());
        assertThat(hibernateStats.getSecondLevelCacheHitCount(), is(equalTo(2l))); // hit increased by 1 since entity still in cache after update
        assertThat(hibernateStats.getSecondLevelCachePutCount(), is(equalTo(2l)));
        assertThat(hibernateStats.getSecondLevelCacheMissCount(), is(equalTo(1l)));

        // assertion shows that, we received entity from second level cache, which was updated after entity update
        assertThat(updatedEmployeeFromCache.isPresent(), is(true));
        assertThat(updatedEmployeeFromCache.get().getFirstName(), is(equalTo(updatedFirstName)));
    }
}