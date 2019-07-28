package com.example.spring.cahce.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author amit
 */
@Entity
@Table(name = "employee")
@Getter
@Setter
@Cacheable // default value is true
public class Employee extends BaseEntity {

    @Basic
    @Column(name = "first_name")
    private String firstName;

    @Basic
    @Column(name = "last_name")
    private String lastName;
}
