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
        final RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair.fromSerializer(RedisSerializer.json())
                );

        // create actual RedisCacheManager using RedisConnectionFactory and configuration.
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }
}

