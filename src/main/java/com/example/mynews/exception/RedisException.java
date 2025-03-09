package com.example.mynews.exception;

import com.example.mynews.response.StatusCode;

public class RedisException extends BusinessException {
    public RedisException(StatusCode statusCode) {
        super(statusCode);
    }
    
    public RedisException(StatusCode statusCode, String message) {
        super(statusCode, message);
    }
} 