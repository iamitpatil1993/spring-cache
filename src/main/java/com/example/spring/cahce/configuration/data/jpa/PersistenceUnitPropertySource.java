package com.example.spring.cahce.configuration.data.jpa;

import java.util.Map;

/**
 * Defines persistence unit properties to be set/overriden
 *
 * @author amit
 */
public interface PersistenceUnitPropertySource {

    Map<String, Object> getPropertyMap();

}
