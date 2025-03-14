package com.example.mynews.Service;

public interface AIService {
    /**
     * 对内容进行AI摘要分析
     * @param content 需要分析的内容
     * @return 分析后的摘要结果
     */
    String summarizeContent(String content);
}