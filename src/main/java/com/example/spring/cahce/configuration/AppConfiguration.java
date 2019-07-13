package com.example.spring.cahce.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.springframework.context.annotation.ComponentScan.Filter;

/**
 * Top level java configuration class for application
 *
 * @author amit
 */
@Configuration
@ComponentScan(basePackages = {"com.example.spring.cahce"}, excludeFilters = {@Filter(value = Configuration.class)})
public class AppConfiguration {
    // Nothing to configure here for now
}
