package com.example.spring.cahce;

import com.example.spring.cahce.configuration.AppConfiguration;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author amit
 */
@ContextConfiguration(classes = {AppConfiguration.class}, loader = AnnotationConfigContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ExampleServiceTests extends TestCase {

    private ExampleService service = new ExampleService();

    @Test
    public void testReadOnce() throws Exception {
        assertEquals("Hello world!", service.getMessage());
    }

}
