package com.example.spring.cahce.configuration.cache;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static com.example.spring.cahce.EnvironmentVariableConstant.ENV_VAR_CACHE_PROVIDER;

/**
 * Since spring CacheManager has multiple implementations, this class defines {@link Condition} for when to use/declare
 * {@link org.springframework.cache.support.SimpleCacheManager} as a Cache backend.
 */
public class SimpleCacheProviderCondition implements Condition {

    public static final String SIMPLE_CACHE_PROVIDER = "simpleCache";

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        final String cacheProvider = context.getEnvironment().getProperty(ENV_VAR_CACHE_PROVIDER);
        return SIMPLE_CACHE_PROVIDER.equalsIgnoreCase(cacheProvider);
    }
}
