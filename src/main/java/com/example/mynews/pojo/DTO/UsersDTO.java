package com.example.mynews.pojo.DTO;

import lombok.Data;
import java.util.Date;

/**
 * 用户数据传输对象（Data Transfer Object）
 * 用于在不同层之间传输用户数据的封装类
 */
@Data
public class UsersDTO {
    /** 用户账号 */
    private Integer account;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 创建时间 */
    private Date createTime;
}