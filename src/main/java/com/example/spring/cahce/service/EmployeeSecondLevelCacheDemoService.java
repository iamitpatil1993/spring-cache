package com.example.spring.cahce.service;

import com.example.spring.cahce.model.Employee;
import com.example.spring.cahce.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service to demonstrate second level caching
 */
@Service
public class EmployeeSecondLevelCacheDemoService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeSecondLevelCacheDemoService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Optional<Employee> findEmployeeByIdWithSecondLevelCache(final Long id) {
        return employeeRepository.findById(id);
    }

}
