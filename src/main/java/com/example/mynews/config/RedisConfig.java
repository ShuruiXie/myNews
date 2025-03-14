package com.example.mynews.config;

import com.example.mynews.exception.RedisException;
import com.example.mynews.response.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.TimeUnit;

/**
 * Redis配置类
 * 提供Redis连接配置和基础操作方法
 */
@Slf4j
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
    
    /**
     * 设置缓存
     */
    public void set(StringRedisTemplate redisTemplate, String key, String value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
        } catch (Exception e) {
            log.error("Redis设置缓存失败: key={}", key, e);
            throw new RedisException(StatusCode.REDIS_OPERATION_ERROR);
        }
    }
    
    /**
     * 获取缓存
     */
    public String get(StringRedisTemplate redisTemplate, String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Redis获取缓存失败: key={}", key, e);
            throw new RedisException(StatusCode.REDIS_OPERATION_ERROR);
        }
    }
    
    /**
     * 删除缓存
     */
    public void delete(StringRedisTemplate redisTemplate, String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Redis删除缓存失败: key={}", key, e);
            throw new RedisException(StatusCode.REDIS_OPERATION_ERROR);
        }
    }
} 