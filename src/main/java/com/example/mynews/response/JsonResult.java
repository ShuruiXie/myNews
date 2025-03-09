package com.example.mynews.response;

import lombok.Data;

import java.util.Map;

@Data
public class JsonResult {
    private int code;
    private String message;
    private Object data;

    public JsonResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public JsonResult(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.message = statusCode.getMessage();
    }

    public JsonResult(StatusCode statusCode, Object data) {
        this.code = statusCode.getCode();
        this.message = statusCode.getMessage();
        this.data = data;
    }

    //成功的情况下
    private JsonResult(Object data) {
        this.code = StatusCode.SUCCESS.getCode();
        this.message = StatusCode.SUCCESS.getMessage();
        this.data = data;
    }

    public JsonResult(int code, String message, Map<String, Object> data) {
        this.code = code;
        this.message = message;
        this.data = data;

    }

    public static JsonResult ok(Object data) {

        return new JsonResult(data);
    }
    public static JsonResult ok(){

        return new JsonResult();
    }

    private JsonResult() {
        this.code = StatusCode.SUCCESS.getCode();
        this.message = StatusCode.SUCCESS.getMessage();
    }

}
