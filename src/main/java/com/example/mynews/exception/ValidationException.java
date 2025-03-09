package com.example.mynews.exception;

import com.example.mynews.response.StatusCode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 参数校验异常类
 * 用于处理数据验证失败的异常情况
 */
@Getter
public class ValidationException extends RuntimeException {
    private final StatusCode statusCode;
    private final Map<String, Object> data;

    // 基础构造函数
    public ValidationException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.statusCode = statusCode;
        this.data = new HashMap<>();
    }

    // 自定义消息构造函数
    public ValidationException(StatusCode statusCode, String customMessage) {
        super(customMessage);
        this.statusCode = statusCode;
        this.data = new HashMap<>();
    }

    // 带数据的构造函数
    public ValidationException(StatusCode statusCode, Map<String, Object> data) {
        super(statusCode.getMessage());
        this.statusCode = statusCode;
        this.data = data;
    }

    public int getCode() {
        return statusCode.getCode();
    }

}