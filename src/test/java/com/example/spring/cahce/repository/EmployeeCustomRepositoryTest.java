package com.example.spring.cahce.repository;

import com.example.spring.cahce.BaseTest;
import com.example.spring.cahce.model.Employee;
import org.hibernate.stat.Statistics;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
@RunWith(SpringRunner.class)
public class EmployeeCustomRepositoryTest extends BaseTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private Statistics hibernateStats;

    @Before
    public void beforeTest() {
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

    @Test
    public void testFindAllByFirstName() {
        // GIVEN
        final String firtName = UUID.randomUUID().toString();
        Employee employee = new Employee();
        employee.setFirstName(firtName);
        employee.setLastName(UUID.randomUUID().toString());

        Employee anotherEmployee = new Employee();
        anotherEmployee.setFirstName(firtName);
        anotherEmployee.setLastName(UUID.randomUUID().toString());

        final List<Employee> savedEmployees = employeeRepository.saveAll(Arrays.asList(anotherEmployee, employee));

        // WHEN
        final List<Employee> employeesWithFirstName = employeeRepository.findAllByFirstName(firtName);

        // this call will fetch data from cache
        employeeRepository.findAllByFirstName(firtName);

        // THEN
        assertEquals(savedEmployees.size(), employeesWithFirstName.size());
        assertEquals(1, hibernateStats.getQueryExecutionCount());
    }
}