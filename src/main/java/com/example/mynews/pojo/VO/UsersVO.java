package com.example.mynews.pojo.VO;

import lombok.Data;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用户视图对象（View Object）
 * 用于前端展示的用户数据封装类，相比实体类增加了格式化后的创建时间字段
 */
@Data
public class UsersVO {
    /** 用户账号 */
    private Integer account;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 创建时间 */
    private Date createTime;
    /** 格式化后的创建时间字符串，格式：yyyy-MM-dd HH:mm:ss */
    private String formattedCreateTime;

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
        this.formattedCreateTime = formatDate(createTime);
    }

    /**
     * 将Date类型的日期转换为格式化的字符串
     * @param date 待格式化的日期
     * @return 格式化后的日期字符串，如果date为null则返回空字符串
     */
    private String formatDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}