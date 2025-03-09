package com.example.mynews.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import com.example.mynews.util.RedisUtil;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/test")
@Slf4j
public class TestController {
    
    @Autowired
    private RedisUtil redisUtil;
    
    @GetMapping("/redis")
    public String testRedis() {
        String key = "test:key";
        String value = "Hello Redis!";
        redisUtil.set(key, value, 5, TimeUnit.MINUTES);
        return redisUtil.get(key);
    }
} 