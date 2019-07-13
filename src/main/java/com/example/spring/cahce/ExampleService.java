package com.example.spring.cahce;

import org.springframework.stereotype.Component;


/**
 * @author amit
 * {@link Service} with hard-coded input data.
 */
@Component
public class ExampleService implements Service {

    /**
     * Reads next record from input
     */
    public String getMessage() {
        return "Hello world!";
    }

}
