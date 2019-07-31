package com.example.spring.cahce.repository;

import com.example.spring.cahce.model.Configuration;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author amit
 */
public interface ConfigurationRepository extends CrudRepository<Configuration, Long> {

    Optional<Configuration> findByConfigKey(final String configKey);
}

