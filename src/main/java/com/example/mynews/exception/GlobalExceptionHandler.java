package com.example.mynews.exception;

import com.example.mynews.response.JsonResult;
import com.example.mynews.response.StatusCode;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 全局异常处理器
 * 统一处理系统中的各类异常，返回标准格式的响应
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final HttpServletRequest request;  // 注入HttpServletRequest

    public GlobalExceptionHandler(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public JsonResult handleBusinessException(BusinessException e) {
        logger.warn("[业务异常] URI:{}, Method:{}, Code:{}, Message:{}", 
            request.getRequestURI(),
            request.getMethod(), 
            e.getCode(),
            e.getMessage()
        );
        return new JsonResult(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数格式异常
     */
    @ExceptionHandler(NumberFormatException.class)
    public JsonResult handleNumberFormatException(NumberFormatException e) {
        logger.warn("参数格式异常 - URL: {}, 参数: {}, 错误信息: {}", 
            request.getRequestURI(), request.getQueryString(), e.getMessage());
        return new JsonResult(StatusCode.PARAM_ERROR.getCode(),
                      StatusCode.PARAM_ERROR.getMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public JsonResult handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("参数校验异常 - URL: {}, 参数: {}, 错误信息: {}", 
            request.getRequestURI(), request.getQueryString(), e.getMessage());
        return new JsonResult(StatusCode.PARAM_ERROR.getCode(), e.getMessage());
    }


    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public JsonResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.warn("请求方法不支持 - URL: {}, 方法: {}", request.getRequestURI(), e.getMethod());
        return new JsonResult(StatusCode.METHOD_NOT_ALLOWED.getCode(), 
            String.format("不支持%s请求方法", e.getMethod()));
    }

    /**
     * 处理请求参数绑定异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.warn("参数绑定异常 - URL: {}, 错误信息: {}", request.getRequestURI(), e.getMessage());
        return new JsonResult(StatusCode.PARAM_ERROR.getCode(), 
            e.getBindingResult().getFieldError() != null ? 
            e.getBindingResult().getFieldError().getDefaultMessage() : 
            StatusCode.PARAM_ERROR.getMessage());
    }

    /**
     * 处理请求体解析异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public JsonResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.warn("请求体解析异常 - URL: {}, 错误信息: {}", request.getRequestURI(), e.getMessage());
        return new JsonResult(StatusCode.PARAM_ERROR.getCode(), "请求体格式错误");
    }

    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public JsonResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        logger.warn("参数类型不匹配 - URL: {}, 参数名: {}, 错误信息: {}", 
            request.getRequestURI(), e.getName(), e.getMessage());
        return new JsonResult(StatusCode.PARAM_ERROR.getCode(), 
            String.format("参数'%s'类型错误", e.getName()));
    }

    /**
     * 处理验证异常
     */
    @ExceptionHandler(ValidationException.class)
    public JsonResult handleValidationException(ValidationException e) {
        logger.warn("[参数校验异常] URI:{}, Method:{}, Code:{}, Message:{}, Data:{}", 
            request.getRequestURI(),
            request.getMethod(),
            e.getCode(),
            e.getMessage(),
            e.getData()
        );
        return new JsonResult(e.getCode(), e.getMessage(), e.getData());
    }

    /**
     * 处理所有未处理的异常
     */
    @ExceptionHandler(Exception.class)
    public JsonResult handleException(Exception e) {
        logger.error("[系统异常] URI:{}, Method:{}, Exception:{}", 
            request.getRequestURI(),
            request.getMethod(),
            e.getClass().getSimpleName(),
            e  // 这里传入完整异常对象，让日志框架记录堆栈信息
        );
        return new JsonResult(StatusCode.INTERNAL_SERVER_ERROR.getCode(),
                      StatusCode.INTERNAL_SERVER_ERROR.getMessage());
    }
}
