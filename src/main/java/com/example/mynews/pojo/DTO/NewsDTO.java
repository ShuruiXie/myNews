package com.example.mynews.pojo.DTO;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 新闻数据传输对象（Data Transfer Object）
 * 用于在不同层之间传递新闻数据的封装类
 */
@Data
public class NewsDTO {

    private Integer id;
    /** 新闻标题 */
    @NotBlank(message = "新闻标题不能为空")
    private String title;
    /** 新闻完整内容 */
    @NotBlank(message = "新闻正文不能为空")
    private String contentTxt;
    /** 新闻简要内容 */
    private String simpleTxt;
    /** 新闻来源 */
    @NotBlank(message = "新闻来源不能为空")
    private String source;
    /** 新闻发布日期 */
    @NotNull(message = "发布日期不能为空")
    private LocalDateTime date;
}