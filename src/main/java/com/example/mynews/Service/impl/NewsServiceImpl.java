package com.example.mynews.Service.impl;

import com.example.mynews.exception.ValidationException;
import com.example.mynews.mapper.NewsMapper;
import com.example.mynews.pojo.DTO.NewsDTO;
import com.example.mynews.response.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.mynews.Service.NewsService;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import com.example.mynews.exception.BusinessException;
import org.springframework.transaction.annotation.Transactional;
import com.example.mynews.Service.AIService;

@Service
@Slf4j
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsMapper newsMapper;
    
    @Autowired
    private AIService aiService;

    @Override
    @Transactional
    public NewsDTO insertNews(NewsDTO newsDTO) {
        try {
            // 检查标题是否已存在
            NewsDTO existingNews = newsMapper.getNewsByTitle(newsDTO.getTitle());
            if (existingNews != null) {
                log.warn("新闻标题已存在: {}", newsDTO.getTitle());
                throw new BusinessException(StatusCode.NEWS_ALREADY_EXISTS);
            }
            
            // 使用 AI 生成文章摘要
            String simpleTxt = aiService.summarizeContent(newsDTO.getContentTxt());
            newsDTO.setSimpleTxt(simpleTxt);
            
            // 插入新闻
            int rows = newsMapper.insertNews(newsDTO);
            if (rows != 1) {
                log.error("新闻插入失败: {}", newsDTO);
                throw new BusinessException(StatusCode.INTERNAL_SERVER_ERROR);
            }
            
            // 重新查询获取完整信息
            NewsDTO savedNews = newsMapper.getNewsById(newsDTO.getId());
            if (savedNews == null) {
                log.error("新闻插入后未找到: {}", newsDTO.getId());
                throw new BusinessException(StatusCode.INTERNAL_SERVER_ERROR);
            }
            
            return savedNews;
            
        } catch (Exception e) {
            log.error("新闻创建异常", e);
            throw new BusinessException(StatusCode.INTERNAL_SERVER_ERROR, "新闻创建失败: " + e.getMessage());
        }
    }

    @Override
    public NewsDTO getNewsById(int id) {
        log.debug("查询新闻: id={}", id);
        NewsDTO news = newsMapper.getNewsById(id);
        if (news == null) {
            log.info("新闻不存在: id={}", id);
            throw new BusinessException(StatusCode.NEWS_NOT_FOUND);
        }
        return news;
    }

    @Override
    public NewsDTO getNewsByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException(StatusCode.PARAM_ERROR, "新闻标题不能为空");
        }
        NewsDTO news = newsMapper.getNewsByTitle(title);
        if (news == null) {
            throw new ValidationException(StatusCode.NEWS_NOT_FOUND);
        }
        return news;
    }

    @Override
    public NewsDTO insertSimpleNews(NewsDTO newsDTO) {
        if (newsDTO == null || newsDTO.getTitle() == null || newsDTO.getSimpleTxt() == null) {
            throw new ValidationException(StatusCode.PARAM_ERROR, "新闻标题和简要内容不能为空");
        }

        if (newsMapper.getNewsById(newsDTO.getId()) != null) {
            throw new ValidationException(StatusCode.NEWS_ALREADY_EXISTS);
        }

        newsMapper.insertNews(newsDTO);
        return newsDTO;
    }
}
