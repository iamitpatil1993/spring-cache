package com.example.spring.cahce.ehcache;

import com.example.spring.cahce.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Test basic CRUD operations on Ehcache
 *
 * @author amit
 */
@RunWith(SpringRunner.class)
public class BasicProgrammaticExampleBeanTest extends BaseTest {

    @Autowired
    private BasicProgrammaticExampleBean bean;

    @Test
    public void saveToCache() {
        // given
        final String key = "foo";
        final String value = "fooBar";

        // when
        bean.saveToCache(key, value);

        // then
        Optional<String> fromCache = bean.getFromCache(key); // this call will fetch data from cache
        assertThat(fromCache.isPresent(), is(true));
        assertThat(fromCache.get(), is(equalTo(value)));
    }

    @Test
    public void deleteFromCache() {
        // given
        final String key = "foo";
        final String value = "fooBar";
        bean.saveToCache(key, value);

        // when
        bean.deleteFromCache(key);

        // then
        assertThat(bean.containsKey(key), is(false));
    }

    @Test
    public void testUpdate() {
        // given
        final String key = "foo";
        final String value = "fooBar";
        final String newValue = UUID.randomUUID().toString();
        bean.saveToCache(key, value);

        // when
        bean.update(key, newValue);

        // then
        Optional<String> updatedValue = bean.getFromCache(key);
        assertThat(updatedValue.isPresent(), is(true));
        assertEquals(newValue, updatedValue.get());
    }
}