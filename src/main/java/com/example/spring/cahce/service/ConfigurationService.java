package com.example.spring.cahce.service;

import com.example.spring.cahce.model.Configuration;
import com.example.spring.cahce.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author amit
 */
@Service
public class ConfigurationService {

    private ConfigurationRepository configurationRepository;

    @Autowired
    public ConfigurationService(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Optional<Configuration> findByConfigKey(final String configKey) {
        return configurationRepository.findByConfigKey(configKey);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Optional<Configuration> findById(final Long id) {
        return configurationRepository.findById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Configuration update(final Configuration configuration) {
        return configurationRepository.save(configuration);
    }

}
