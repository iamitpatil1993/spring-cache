package com.example.spring.cahce.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "configuration")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Configuration extends BaseEntity implements Serializable {

    @Basic
    @Column(name = "config_key")
    private String configKey;

    @Basic
    @Column(name = "config_value")
    private String configValue;
}
