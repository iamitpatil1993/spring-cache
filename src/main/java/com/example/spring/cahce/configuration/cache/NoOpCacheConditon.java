package com.example.spring.cahce.configuration.cache;

import com.example.spring.cahce.EnvironmentVariableConstant;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Defines conditon for {@link org.springframework.cache.CacheManager} implementation bean which can be used to disable cache.
 */
public class NoOpCacheConditon implements Condition {

    public static final String NO_OP_CACHE_PROVIDER = "noOp";

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        final String cacheProvider = context.getEnvironment().getProperty(EnvironmentVariableConstant.ENV_VAR_CACHE_PROVIDER);
        return NO_OP_CACHE_PROVIDER.equalsIgnoreCase(cacheProvider);
    }
}
