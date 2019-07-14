package com.example.spring.cahce.configuration.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

/**
 * @author amit
 */
@Configuration
@Conditional(RedisCacheProviderCondition.class)
@PropertySource(value = "classpath:redis.properties")
public class RedisCacheProviderConfiguration {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private Integer port;

    @Value("${redis.database}")
    private Integer database;

    /**
     * Define connection factory to connect redis using Lettuce.
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory redisConnectionFactory = new LettuceConnectionFactory(host, port);
        redisConnectionFactory.setDatabase(database);
        return redisConnectionFactory;
    }

    /**
     * Configuration her for redis as a cache abstraction is different that, one used for normal RedisTemplate.
     */
    @Bean
    public CacheManager redisCacheManager(final RedisConnectionFactory redisConnectionFactory) {
        // RedisCacheConfiguration is used to customize redis cache behaviour, for now will use defaults.
        // just setting value serializer because u one is binary
        RedisCacheConfiguration cacheConfiguration = getRedisCacheConfiguration();

        // create actual RedisCacheManager using RedisConnectionFactory and configuration.
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }

    private RedisCacheConfiguration getRedisCacheConfiguration() {
        // this class is immutable, so we need to cache return value every time we set configuration.
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();

        // we can restrict from saving null value to cache. Will throw Exception for null value.
        cacheConfiguration = cacheConfiguration.disableCachingNullValues();

        // we can add prefix to cache keys based on cacheName. Here I am adding no prefix
        //cacheConfiguration = cacheConfiguration.computePrefixWith(cacheName -> "");

        /*
         By default, spring add cacheName as a prefix to all keys stored in that cacheName, we can disable all
         cache prefix by setting this configuration
        */
        //cacheConfiguration.disableKeyPrefix();

        // we can set expiry to cache entry. But it will be applied to all cacheNames.
        cacheConfiguration = cacheConfiguration.entryTtl(Duration.ofMinutes(5));

        // Set value serializer
        cacheConfiguration = cacheConfiguration.serializeValuesWith(RedisSerializationContext
                .SerializationPair.fromSerializer(RedisSerializer.json())
        );
        return cacheConfiguration;
    }
}

