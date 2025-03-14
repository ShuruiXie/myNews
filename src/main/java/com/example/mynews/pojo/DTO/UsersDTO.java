package com.example.mynews.pojo.DTO;

import lombok.Data;
import java.util.Date;

/**
 * 用户数据传输对象
 * 用于在不同层之间传递用户相关数据
 */
@Data
public class UsersDTO {
    /** 用户账号，作为唯一标识 */
    private Integer account;

    /** 用户名，用于显示和登录 */
    private String username;

    /** 用户密码，存储加密后的密码 */
    private String password;

    /** 用户创建时间 */
    private Date createTime;
}