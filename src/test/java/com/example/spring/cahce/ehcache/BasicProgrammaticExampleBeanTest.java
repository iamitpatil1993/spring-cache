package com.example.spring.cahce.ehcache;

import com.example.spring.cahce.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import javax.cache.Cache;
import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Test basic CRUD operations on Ehcache
 *
 * @author amit
 */
@RunWith(SpringRunner.class)
public class BasicProgrammaticExampleBeanTest extends BaseTest {

    @Autowired
    private BasicProgrammaticExampleBean bean;

    /**
     * Wiring java spec cache manager
     */
    @Autowired
    private javax.cache.CacheManager cacheManager;

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

    /**
     * Cache operations using java spec cache manager (Independent of implementation)
     * As per employee cache configuration in ehcache-jsr107-config.xml, heap entries count is only 5, and we
     * are storing 1K entries into cache, and all values getting stored successfuly, which means that offHeap configuration
     * works and entries are getting stored on RAM after limit of 5 entries on heap exceeded.
     */
    @Test
    public void testEmpployeeSave() {

        // given
        final Cache<String, Employee> employeeCache = cacheManager.getCache("employee", String.class, Employee.class);
        final List<String> ids = new ArrayList<>(1000);

        // when
        for (int i = 0; i < 1000; i++) {
            final String id = UUID.randomUUID().toString();
            ids.add(id);
            Employee employee = new Employee(id, UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(), "member of Technical staff", Calendar.getInstance());
            employeeCache.put(employee.getId(), employee);
        }

        // then
        ids.stream().forEach(id -> {
            assertTrue(employeeCache.containsKey(id));
        });
    }
}