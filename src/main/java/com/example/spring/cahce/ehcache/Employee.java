package com.example.spring.cahce.ehcache;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Calendar;

@Getter
@Setter
/* In order to enable offHeap storage, this class must be Serializable, because on heap, data by default get stored
//by-value (which we can disable and change to by-reference) but on RAM, data will get stored in serialized state.class
Which will in add sone serialization and deserialization latency to caching operations,as compared to heap operations.
*/
public class Employee implements Serializable {

    private String id;

    private String firstName;

    private String lastName;

    private String designation;

    private Calendar dataOfJoining;

    public Employee(String id, String firstName, String lastName, String designation, Calendar dataOfJoining) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.designation = designation;
        this.dataOfJoining = dataOfJoining;
    }
}
