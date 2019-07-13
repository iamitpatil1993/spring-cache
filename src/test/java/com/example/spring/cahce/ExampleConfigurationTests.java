package com.example.spring.cahce;

import com.example.spring.cahce.configuration.AppConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertNotNull;

/**
 * @author amit
 */
@ContextConfiguration(classes = {AppConfiguration.class}, loader = AnnotationConfigContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ExampleConfigurationTests {

    @Autowired
    private Service service;

    @Test
    public void testSimpleProperties() throws Exception {
        assertNotNull(service);
    }

}
