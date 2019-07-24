package com.example.spring.cahce.service;

import com.example.spring.cahce.model.Employee;
import com.example.spring.cahce.stragegy.ReadOperation;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CacheStrategyEmployeeService {

    public Optional<Employee> getEmployeeByIdWithCache(final Long id,
                                                       final ReadOperation<Employee, Long> readOperationStrategy) {
        return readOperationStrategy.read(id);
    }
}
