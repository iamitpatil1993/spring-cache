package com.example.spring.cahce.configuration;

import org.springframework.cache.annotation.CachePut;

import java.lang.annotation.*;

/**
 * Custom Cache annotation for 'configuration' cache to abstract common cache configurations.
 * Read <a href="https://docs.spring.io/spring/docs/5.2.0.RELEASE/spring-framework-reference/core.html#beans-meta-annotations">here</a> and
 * <a href="https://docs.spring.io/spring/docs/5.2.0.RELEASE/spring-framework-reference/integration.html#cache-annotation-stereotype">here</a>
 * for more details on custom annotation creation in spring.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@CachePut(cacheNames = "configuration")
public @interface ConfigurationCachePut {

    /**
     * ALl these attributes are allowed to override at usage, any missing attribute here will be invisible from {@link ConfigurationCachePut}
     * client so, won't be able to use.
     * So, {@link CachePut#cacheNames()} and {@link CachePut#value()} attributes of {@link CachePut} are missing here,
     * so these two attributes won't be accessible to client. All other attributes redeclare here will be accessible.
     */

    // we can define default value as well, so since it is cache put, I am defining custom value to be id of result.
    // this attribute still can be overridden at usage
    String key() default "#result.id";

    String keyGenerator() default "";

    String cacheManager() default "";

    String cacheResolver() default "";

    String condition() default "";

    String unless() default "";
}
