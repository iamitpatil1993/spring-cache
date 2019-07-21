package com.example.spring.cahce.repository;

import com.example.spring.cahce.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring repository for Employee. Spring will generate implementation for this.
 *
 * @author amit
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // No Query methods for now.
}
