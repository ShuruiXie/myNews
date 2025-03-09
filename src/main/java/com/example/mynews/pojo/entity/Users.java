package com.example.mynews.pojo.entity;
import java.util.Date;
import lombok.Data;
@Data
public class Users {
    private Integer account;
    private String username;
    private String password;
    private Date createTime;
}
