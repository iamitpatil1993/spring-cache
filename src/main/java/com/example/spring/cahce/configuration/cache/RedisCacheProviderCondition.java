package com.example.spring.cahce.configuration.cache;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static com.example.spring.cahce.EnvironmentVariableConstant.ENV_VAR_CACHE_PROVIDER;

/**
 * Defines condition for, when to declare RedisCacheManager as a cache provider.
 *
 * @author amit
 */
public class RedisCacheProviderCondition implements Condition {

    private static final String ENV_VALUE = "redis";

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return ENV_VALUE.equalsIgnoreCase(conditionContext.getEnvironment().getProperty(ENV_VAR_CACHE_PROVIDER));
    }
}
