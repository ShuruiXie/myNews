package com.example.mynews.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis健康检查指示器
 */
@Component
public class RedisHealthIndicator implements HealthIndicator {
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Override
    public Health health() {
        try {
            RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
            connection.ping();
            connection.close();
            return Health.up().withDetail("redis", "Redis is running").build();
        } catch (Exception e) {
            return Health.down().withDetail("redis", "Redis is down").withException(e).build();
        }
    }
} 