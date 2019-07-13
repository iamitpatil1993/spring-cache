package com.example.spring.cahce.configuration;

import com.example.spring.cahce.configuration.cache.SpringCacheConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.springframework.context.annotation.ComponentScan.Filter;

/**
 * Top level java configuration class for application
 *
 * @author amit
 */
@Configuration
@ComponentScan(basePackages = {"com.example.spring.cahce"}, excludeFilters = {@Filter(value = Configuration.class)})
@Import(value = {SpringCacheConfiguration.class})
public class AppConfiguration {
    // Nothing to configure here for now
}
