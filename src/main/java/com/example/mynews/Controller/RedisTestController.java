package com.example.mynews.Controller;

import com.example.mynews.config.RedisConfig;
import com.example.mynews.response.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis测试接口
 * 用于测试Redis的基本操作功能
 */
@Slf4j
@RestController
@RequestMapping("/api/redis/test")
public class RedisTestController {

    @Autowired
    private RedisConfig redisConfig;
    
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 测试Redis写入和读取
     * @param key 键
     * @param value 值
     * @param timeout 过期时间(秒)
     * @return 操作结果
     */
    @PostMapping("/set")
    public JsonResult testSet(
            @RequestParam String key,
            @RequestParam String value,
            @RequestParam(defaultValue = "60") long timeout) {
        
        log.info("测试Redis写入: key={}, value={}, timeout={}s", key, value, timeout);
        
        // 写入Redis，设置过期时间
        redisConfig.set(redisTemplate, key, value, timeout, TimeUnit.SECONDS);
        
        // 读取Redis验证
        String savedValue = redisConfig.get(redisTemplate, key);
        
        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("setValue", value);
        result.put("getValue", savedValue);
        result.put("timeout", timeout);
        
        return JsonResult.ok(result);
    }

    /**
     * 获取Redis中的值
     * @param key 键
     * @return 值
     */
    @GetMapping("/get/{key}")
    public JsonResult testGet(@PathVariable String key) {
        log.info("测试Redis读取: key={}", key);
        
        String value = redisConfig.get(redisTemplate, key);
        
        Map<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("value", value);
        
        return JsonResult.ok(result);
    }

    /**
     * 删除Redis中的值
     * @param key 键
     * @return 操作结果
     */
    @DeleteMapping("/delete/{key}")
    public JsonResult testDelete(@PathVariable String key) {
        log.info("测试Redis删除: key={}", key);
        
        // 先获取值
        String value = redisConfig.get(redisTemplate, key);
        
        // 删除键
        redisConfig.delete(redisTemplate, key);
        
        Map<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("deletedValue", value);
        
        return JsonResult.ok(result);
    }
} 