package com.example.mynews.mapper;

import com.example.mynews.pojo.DTO.NewsDTO;
import org.apache.ibatis.annotations.*;

/**
 * 新闻数据访问接口
 * 处理与新闻相关的数据库操作
 */
@Mapper
public interface NewsMapper {
    /**
     * 插入新闻
     */
    @Insert("INSERT INTO news (title, contentTxt, simpleTxt, source, date) " +
            "VALUES (#{title}, #{contentTxt}, #{simpleTxt}, #{source}, #{date})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertNews(NewsDTO newsDTO);

    /**
     * 根据ID查询新闻
     * @param id 新闻ID
     * @return 新闻信息
     */
    @Select("SELECT * FROM news WHERE id = #{id}")
    NewsDTO getNewsById(int id);

    /**
     * 更新新闻的simpleTxt
     * @param newsDTO 包含ID和simpleTxt的新闻对象
     * @return 影响的行数
     */
    @Update("UPDATE news SET simpleTxt = #{simpleTxt} WHERE id = #{id}")
    int updateNewsSimpleTxt(NewsDTO newsDTO);

    /**
     * 根据标题查询新闻
     * @param title 新闻标题
     * @return 新闻信息
     */
    @Select("SELECT * FROM news WHERE title = #{title}")
    NewsDTO getNewsByTitle(String title);
}