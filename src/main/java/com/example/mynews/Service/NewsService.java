package com.example.mynews.Service;
import com.example.mynews.pojo.DTO.NewsDTO;
public interface NewsService {

    NewsDTO getNewsById(int id);

    NewsDTO getNewsByTitle(String title);

    /**
     * 插入新闻
     * @param newsDTO 新闻
     * @return 插入成功的信息
     */
    NewsDTO insertNews(NewsDTO newsDTO);

    NewsDTO insertSimpleNews(NewsDTO newsDTO);

}
