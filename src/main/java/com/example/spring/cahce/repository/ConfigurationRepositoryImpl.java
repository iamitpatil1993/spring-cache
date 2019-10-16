package com.example.spring.cahce.repository;

import com.example.spring.cahce.configuration.ConfigurationCachePut;
import com.example.spring.cahce.model.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ConfigurationRepositoryImpl implements CustomConfigurationRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    @ConfigurationCachePut // using custom cache annotation, we can override key, but not cacheNames
    public Configuration customSave(Configuration configuration) {
        entityManager.persist(configuration);
        return configuration;
    }
}
