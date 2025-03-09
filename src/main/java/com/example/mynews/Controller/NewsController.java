package com.example.mynews.Controller;

import com.example.mynews.Service.NewsService;
import com.example.mynews.exception.ValidationException;
import com.example.mynews.pojo.DTO.NewsDTO;
import com.example.mynews.response.JsonResult;
import com.example.mynews.response.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

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
     * 插入完整新闻
     * @param newsDTO 新闻信息
     * @return 插入的新闻信息
     */
    @PostMapping("/insert")
    public JsonResult insertNews(@RequestBody NewsDTO newsDTO) {
        if (newsDTO == null) {
            throw new ValidationException(StatusCode.PARAM_ERROR, "新闻信息不能为空");
        }
        NewsDTO news = newsService.insertNews(newsDTO);
        return JsonResult.ok(news);
    }

    /**
     * 插入简单新闻
     * @param newsDTO 新闻信息
     * @return 插入的新闻信息
     */
    @PostMapping("/insertSimple")
    public JsonResult insertSimpleNews(@RequestBody NewsDTO newsDTO) {
        if (newsDTO == null) {
            throw new ValidationException(StatusCode.PARAM_ERROR, "新闻信息不能为空");
        }
        NewsDTO news = newsService.insertSimpleNews(newsDTO);
        return JsonResult.ok(news);
    }
}
