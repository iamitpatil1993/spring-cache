package com.example.spring.cahce.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Defines base entity for all JPA entities
 *
 * @author amit
 */
@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    @Basic
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // this is spring annotation and not hibernate (Jpa do not have annotation for this we need to use entity listeners)
    @Basic
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on")
    private Calendar createdOn;

    @Basic
    @LastModifiedDate // prefer using spring annotation over hibernate to remove dependency on persistence provider
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_on")
    private Calendar updatedOn;

    @Basic
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
