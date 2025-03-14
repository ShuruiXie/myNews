package com.example.mynews.Controller;

import com.example.mynews.Service.NewsService;
import com.example.mynews.Service.AIService;
import com.example.mynews.exception.ValidationException;
import com.example.mynews.pojo.DTO.NewsDTO;
import com.example.mynews.response.JsonResult;
import com.example.mynews.response.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
@Slf4j
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private AIService aiService;

    /**
     * 根据ID获取新闻
     * @param id 新闻ID
     * @return 新闻信息
     */
    @GetMapping("/{id}")
    public JsonResult getNewsById(@PathVariable int id) {
        if (id <= 0) {
            throw new ValidationException(StatusCode.PARAM_ERROR, "新闻ID必须大于0");
        }
        NewsDTO news = newsService.getNewsById(id);
        return JsonResult.ok(news);
    }

    /**
     * 根据标题获取新闻
     * @param title 新闻标题
     * @return 新闻信息
     */
    @GetMapping("/title/{title}")
    public JsonResult getNewsByTitle(@PathVariable String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException(StatusCode.PARAM_ERROR, "新闻标题不能为空");
        }
        NewsDTO news = newsService.getNewsByTitle(title);
        return JsonResult.ok(news);
    }

    /**
     * 创建新闻
     * @param newsDTO 新闻信息
     * @return 创建成功的新闻
     */
    @PostMapping("/create")
    public JsonResult createNews(@RequestBody NewsDTO newsDTO) {
        log.info("创建新闻请求: {}", newsDTO);
        
        // 基础参数校验
        validateNewsParams(newsDTO);
        
        // 调用服务创建新闻
        NewsDTO createdNews = newsService.insertNews(newsDTO);
        
        log.info("新闻创建成功: {}", createdNews.getId());
        return JsonResult.ok(createdNews);
    }
    
    /**
     * 校验新闻参数
     */
    private void validateNewsParams(NewsDTO newsDTO) {
        if (newsDTO.getTitle() == null || newsDTO.getTitle().trim().isEmpty()) {
            throw new ValidationException(StatusCode.PARAM_ERROR, "新闻标题不能为空");
        }
        if (newsDTO.getContentTxt() == null || newsDTO.getContentTxt().trim().isEmpty()) {
            throw new ValidationException(StatusCode.PARAM_ERROR, "新闻正文不能为空");
        }
        if (newsDTO.getSource() == null || newsDTO.getSource().trim().isEmpty()) {
            throw new ValidationException(StatusCode.PARAM_ERROR, "新闻来源不能为空");
        }
        if (newsDTO.getDate() == null) {
            throw new ValidationException(StatusCode.PARAM_ERROR, "发布日期不能为空");
        }
    }

    /**
     * 测试接口
     */
    @GetMapping("/test")
    public JsonResult test() {
        return JsonResult.ok("测试成功");
    }

    /**
     * 测试AI文本摘要功能
     * @param request 包含content字段的请求体
     * @return AI分析结果
     */
    @PostMapping("/test/ai-summary")
    public JsonResult testAISummary(@RequestBody Map<String, String> request) {
        log.info("收到AI摘要分析请求: request={}", request);  // 打印整个请求体
        
        if (request == null) {
            throw new ValidationException(StatusCode.PARAM_ERROR, "请求体不能为空");
        }
        
        String content = request.get("content");
        log.info("解析的content内容: {}", content);  // 打印解析出的content
        
        if (content == null || content.trim().isEmpty()) {
            throw new ValidationException(StatusCode.PARAM_ERROR, "测试文本不能为空");
        }
        
        try {
            log.info("开始调用AI服务...");
            String summary = aiService.summarizeContent(content);
            log.info("AI摘要分析成功: summary={}", summary);
            return JsonResult.ok(summary);
        } catch (Exception e) {
            log.error("AI摘要分析失败", e);
            return new JsonResult(StatusCode.INTERNAL_SERVER_ERROR, "AI分析失败: " + e.getMessage());
        }
    }
}
