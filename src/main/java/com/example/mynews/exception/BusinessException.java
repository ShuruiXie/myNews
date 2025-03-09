package com.example.mynews.exception;

import com.example.mynews.response.StatusCode;
import lombok.Getter;

/**
 * 业务异常
 * 用于处理业务逻辑异常，与StatusCode枚举配合使用
 */
@Getter
public class BusinessException extends RuntimeException {
    private final StatusCode error;

    public BusinessException(StatusCode error) {
        super(error.getMessage());
        this.error = error;
    }

    public BusinessException(StatusCode error, String customMessage) {
        super(customMessage);
        this.error = error;
    }

    /**
     * 获取错误码
     */
    public int getCode() {
        return error.getCode();
    }

    /**
     * 获取错误消息，如果有自定义消息则返回自定义消息，否则返回错误码对应的默认消息
     */
    @Override
    public String getMessage() {
        String customMessage = super.getMessage();
        return customMessage != null && !customMessage.equals(error.getMessage())
            ? customMessage
            : error.getMessage();
    }
}
