package com.example.spring.cahce.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author amit
 */
@Entity
@Table(name = "configuration")
@Getter
@Setter
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY) // this will override cache concurrency set globally at PU level.
// cache with READ_ONLY strategy will restrict update operation on entity.
public class Configuration extends BaseEntity {

    private String configKey;

    private String configValue;

}
