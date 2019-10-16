package com.example.spring.cahce.repository;

import com.example.spring.cahce.model.Configuration;

public interface CustomConfigurationRepository {

    Configuration customSave(final Configuration configuration);
}
