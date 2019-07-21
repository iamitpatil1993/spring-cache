package com.example.spring.cahce.repository;

import com.example.spring.cahce.BaseTest;
import com.example.spring.cahce.model.Employee;
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
}