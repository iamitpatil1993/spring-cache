package com.example.spring.cahce.repository;

import com.example.spring.cahce.model.Employee;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.cache.annotation.CacheResult;
import java.util.List;

/**
 * Spring repository for Employee. Spring will generate implementation for this.
 *
 * @author amit
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Cacheable(cacheNames = "EmployeeCache")
    List<Employee> findAllByFirstName(final String fistName);

}
