package com.example.mynews.pojo.VO;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 新闻视图对象（View Object）
 * 用于前端展示的新闻数据封装类，相比实体类增加了格式化后的日期字段
 */
@Data
public class NewsVO {
    /** 新闻ID */
    private int id;
    /** 新闻标题 */
    private String title;
    /** 新闻完整内容 */
    private String contentTxt;
    /** 新闻简要内容 */
    private String simpleTxt;
    /** 新闻来源 */
    private String source;
    /** 新闻发布日期 */
    private Date date;
    /** 格式化后的日期字符串，格式：yyyy-MM-dd HH:mm:ss */
    private String formattedDate;

    public void setDate(Date date) {
        this.date = date;
        this.formattedDate = formatDate(date);
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