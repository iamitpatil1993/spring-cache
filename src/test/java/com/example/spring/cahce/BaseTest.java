/**
 *
 */
package com.example.spring.cahce;

import com.example.spring.cahce.configuration.AppConfiguration;
import com.example.spring.cahce.ehcache.BasicProgrammaticExampleBean;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author amit
 *
 */
@ContextConfiguration(classes = {AppConfiguration.class})
public class BaseTest {
    // Nothing to do her for now

    @Autowired
    private BasicProgrammaticExampleBean bean;

    @Before
    @After
    public void beforeTest() {
        bean.clearCache();
    }
}
