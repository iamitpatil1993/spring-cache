package com.example.spring.cahce.configuration.cache;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static com.example.spring.cahce.EnvironmentVariableConstant.ENV_VAR_CACHE_PROVIDER;

/**
 * Condition to decide when to use ConcurrentMapCacheManager as a CacheManager (Cache provider).
 *
 * @author amit
 */
public class ConcurrentMapCacheProviderCondition implements Condition {

    private static final String ENV_VALUE = "concurrentMap";
    ;

    /**
     * Use ConcurrentMapCacheManager as a cache provider when, either <br>
     * 1. ENV_VAR_CACHE_PROVIDER environment variable set to 'concurrentMap'<br>
     * 2. No value set for ENV_VAR_CACHE_PROVIDER environment variable.<br>
     * 3. Empty value set for ENV_VAR_CACHE_PROVIDER environment variable.<br>
     */
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String cacheProvider = conditionContext.getEnvironment().getProperty(ENV_VAR_CACHE_PROVIDER);
        return ENV_VALUE.equalsIgnoreCase(cacheProvider) || cacheProvider == null || cacheProvider.isEmpty();
    }
}
