package com.example.mynews.Service.impl;

import com.example.mynews.exception.ValidationException;
import com.example.mynews.mapper.NewsMapper;
import com.example.mynews.pojo.DTO.NewsDTO;
import com.example.mynews.response.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.mynews.Service.NewsService;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.mynews.exception.BusinessException;

@Service
public class NewsServiceImpl implements NewsService {
    private static final Logger logger = LoggerFactory.getLogger(NewsServiceImpl.class);

    @Autowired
    private NewsMapper newsMapper;


    @Override
    public NewsDTO insertNews(NewsDTO newsDTO) {
        try {
            // 业务规则检查
            if(newsMapper.getNewsById(newsDTO.getId()) != null) {
                logger.info("[新闻已存在] id:{}, title:{}", newsDTO.getId(), newsDTO.getTitle());
                throw new BusinessException(
                    StatusCode.NEWS_ALREADY_EXISTS,
                    String.format("新闻[id=%d]已存在", newsDTO.getId())
                );
            }

            logger.info("[开始插入新闻] id:{}, title:{}", newsDTO.getId(), newsDTO.getTitle());
            newsMapper.insertNews(newsDTO);
            logger.info("[新闻插入成功] id:{}", newsDTO.getId());
            return newsDTO;
        } catch (DataIntegrityViolationException e) {
            logger.error("[数据库操作异常] newsDTO:{}", newsDTO, e);
            throw new BusinessException(StatusCode.INTERNAL_SERVER_ERROR, "数据库操作异常");
        }
    }

    @Override
    public NewsDTO getNewsById(int id) {
        logger.debug("[查询新闻] id:{}", id);
        NewsDTO news = newsMapper.getNewsById(id);
        if (news == null) {
            logger.info("[新闻不存在] id:{}", id);
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
