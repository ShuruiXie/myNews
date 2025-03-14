package com.example.mynews.response;

import lombok.Getter;

/**
 * 系统状态码枚举
 * 定义了所有可能的业务状态码和对应的描述信息
 */
@Getter
public enum StatusCode {
   // 成功码
   SUCCESS(200, "操作成功"),
    
   // 客户端错误 (4xx)
   BAD_REQUEST(400, "请求参数错误"),
   UNAUTHORIZED(401, "未经授权"),
   FORBIDDEN(403, "访问被拒绝"),
   NOT_FOUND(404, "资源不存在"),
   METHOD_NOT_ALLOWED(405, "请求方法不允许"),
   UNSUPPORTED_MEDIA_TYPE(415, "不支持的媒体类型"),
   
   // 服务端错误 (5xx)
   INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
   SERVICE_UNAVAILABLE(503, "服务不可用"),
   
   // 业务错误 (1000-1999)
   PARAM_ERROR(1000, "参数错误"),
   
   // 用户相关错误 (2000-2999)
   USER_NOT_FOUND(2000, "用户不存在"),
   USERNAME_EXISTS(2001, "用户名已存在"),
   PASSWORD_ERROR(2002, "密码错误"),
   ACCOUNT_LOCKED(2003, "账号已被锁定"),
   
   // 新闻相关错误 (3000-3999)
   NEWS_NOT_FOUND(3000, "新闻不存在"),
   NEWS_ALREADY_EXISTS(3001, "新闻已存在"),
   NEWS_EXPIRED(3002, "新闻已过期"),
   
   // Redis相关错误 (4000-4999)
   REDIS_ERROR(4000, "Redis服务异常"),
   REDIS_CONNECTION_ERROR(4001, "Redis连接失败"),
   REDIS_OPERATION_ERROR(4002, "Redis操作失败"),
   
   // 爬虫相关错误 (5000-5999)
   CRAWLER_ERROR(5000, "爬虫异常"),
   CRAWLER_TIMEOUT(5001, "爬取超时"),
   CRAWLER_NETWORK_ERROR(5002, "网络请求失败"),
   CRAWLER_PARSE_ERROR(5003, "内容解析失败");

   /** 状态码 */
   private final int code;
   
   /** 状态描述 */
   private final String message;

   /**
    * 构造函数
    * @param code 状态码
    * @param message 状态描述
    */
   StatusCode(int code, String message) {
       this.code = code;
       this.message = message;
   }
}
