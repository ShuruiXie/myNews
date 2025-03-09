package com.example.mynews.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类
 * 用于配置Redis连接和序列化方式
 */
@Configuration
public class RedisConfig {
    
    /**
     * 配置StringRedisTemplate
     * 使用StringRedisSerializer作为序列化器
     * 这样可以确保Redis中的数据是可读的字符串格式
     * 
     * @param connectionFactory Redis连接工厂，由Spring Boot自动配置
     * @return 配置好的StringRedisTemplate实例
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        // 设置Redis连接工厂
        template.setConnectionFactory(connectionFactory);
        
        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(stringSerializer);
        
        // 使用StringRedisSerializer来序列化和反序列化hash结构的key和value
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(stringSerializer);
        
        // 初始化RedisTemplate
        template.afterPropertiesSet();
        return template;
    }
} 