package com.example.spring.cahce.jcache;

import com.example.spring.cahce.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class JcacheOperationsExampleBeanTest extends BaseTest {

    @Autowired
    private JcacheOperationsExampleBean jcacheOps;

    @Test
    public void testSaveToCache() {
        // GIVEN
        final String key = UUID.randomUUID().toString();
        final String value = UUID.randomUUID().toString();

        // WHEN
        jcacheOps.saveToCache(key, value);

        // THEN
        assertNotNull(jcacheOps.getFromCache(key));
    }

    @Test
    public void testDeleteFromCache() {
        // GIVEN
        final String key = UUID.randomUUID().toString();
        final String value = UUID.randomUUID().toString();
        jcacheOps.saveToCache(key, value);

        // WHEN
        jcacheOps.deleteFromCache(key);

        // THEN
        assertFalse(jcacheOps.containsKey(key));
    }

    @Test
    public void testClearCache() {
        // GIVEN
        final Set<String> keys = new HashSet<>(10);
        for (int i = 0; i < 10; i++) {
            final String key = UUID.randomUUID().toString();
            final String value = UUID.randomUUID().toString();
            jcacheOps.saveToCache(key, value);
            keys.add(key);
        }

        // WHEN
        jcacheOps.clearCache();

        // THEN
        keys.stream().forEach(key -> {
            assertFalse(jcacheOps.containsKey(key));
        });
    }

    @Test
    public void testUpdate() {
        // GIVEN
        final String key = UUID.randomUUID().toString();
        final String value = UUID.randomUUID().toString();
        final String updatedValue = UUID.randomUUID().toString();
        jcacheOps.saveToCache(key, value);

        // WHEN
        jcacheOps.update(key, updatedValue);

        // THEN
        assertEquals(updatedValue, jcacheOps.getFromCache(key).get());

    }
}