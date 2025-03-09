package com.example.mynews.pojo.entity;

import lombok.Data;
import java.util.Date;
@Data
public class News {
    private int id;
    private String title;
    private String content;
    private String simple;
    private String source;
    private Date time;
}
