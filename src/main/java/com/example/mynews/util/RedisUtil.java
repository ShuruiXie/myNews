package com.example.mynews.util;

import com.example.mynews.exception.RedisException;
import com.example.mynews.response.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 */
@Slf4j
@Component
public class RedisUtil {
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    /**
     * 设置缓存
     */
    public void set(String key, String value, long timeout, TimeUnit unit) {
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
    public String get(String key) {
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
    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Redis删除缓存失败: key={}", key, e);
            throw new RedisException(StatusCode.REDIS_OPERATION_ERROR);
        }
    }
    
    /**
     * 判断key是否存在
     */
    public boolean hasKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("Redis判断key是否存在失败: key={}", key, e);
            throw new RedisException(StatusCode.REDIS_OPERATION_ERROR);
        }
    }
    
    /**
     * 设置过期时间
     */
    public boolean expire(String key, long timeout, TimeUnit unit) {
        try {
            return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, unit));
        } catch (Exception e) {
            log.error("Redis设置过期时间失败: key={}", key, e);
            throw new RedisException(StatusCode.REDIS_OPERATION_ERROR);
        }
    }
} 