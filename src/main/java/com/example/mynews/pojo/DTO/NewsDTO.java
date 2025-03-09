package com.example.mynews.pojo.DTO;

import lombok.Data;


import java.util.Date;

/**
 * 新闻数据传输对象（Data Transfer Object）
 * 用于在不同层之间传递新闻数据的封装类
 */
@Data
public class NewsDTO {

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
}