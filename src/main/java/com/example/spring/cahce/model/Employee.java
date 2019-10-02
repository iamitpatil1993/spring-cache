package com.example.spring.cahce.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author amit
 */
@Entity
@Table(name = "employee")
@Getter
@Setter
public class Employee extends BaseEntity implements Serializable {

    @Basic
    @Column(name = "first_name")
    private String firstName;

    @Basic
    @Column(name = "last_name")
    private String lastName;
}
