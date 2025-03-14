package com.example.mynews.pojo.DTO;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 新闻数据传输对象
 * 用于在不同层之间传递新闻相关数据
 */
@Data
public class NewsDTO {
    /** 新闻ID */
    private Integer id;

    /** 新闻标题，不能为空 */
    @NotBlank(message = "新闻标题不能为空")
    private String title;

    /** 新闻完整内容，不能为空 */
    @NotBlank(message = "新闻正文不能为空")
    private String contentTxt;

    /** 新闻AI生成的摘要内容 */
    private String simpleTxt;

    /** 新闻来源，不能为空 */
    @NotBlank(message = "新闻来源不能为空")
    private String source;

    /** 新闻发布时间，不能为空 */
    @NotNull(message = "发布日期不能为空")
    private LocalDateTime date;
}