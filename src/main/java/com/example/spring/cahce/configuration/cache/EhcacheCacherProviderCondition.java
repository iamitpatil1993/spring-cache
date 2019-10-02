package com.example.spring.cahce.configuration.cache;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static com.example.spring.cahce.EnvironmentVariableConstant.ENV_VAR_CACHE_PROVIDER;

/**
 * Since spring CacheManager has multiple implementations, this class defines {@link Condition} for when to use/declare ehcache
 * as a Cache backend. ({@link org.springframework.cache.ehcache.EhCacheCacheManager})
 */
public class EhcacheCacherProviderCondition implements Condition {

    public static final String EHCACHE = "ehcache";

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        final Environment environment = context.getEnvironment();
        final String cacheProviderConfig = environment.getProperty(ENV_VAR_CACHE_PROVIDER);
        return EHCACHE.equals(cacheProviderConfig);
    }
}
