package com.example.mynews.mapper;

import com.example.mynews.pojo.DTO.UsersDTO;
import org.apache.ibatis.annotations.*;

/**
 * 用户数据访问接口
 * 处理与用户相关的数据库操作
 */
@Mapper
public interface UsersMapper {
    /**
     * 插入新用户
     * @param usersDTO 用户信息
     * @return 影响的行数
     */
    @Insert("INSERT INTO users (account, username, password, create_time) VALUES (#{account}, #{username}, #{password}, #{createTime})")
    int insertUser(UsersDTO usersDTO);

    /**
     * 根据账号查询用户
     * @param account 用户账号
     * @return 用户信息
     */
    @Select("SELECT * FROM users WHERE account = #{account}")
    UsersDTO getUserByAccount(int account);

    /**
     * 更新用户信息
     * @param usersDTO 用户信息
     * @return 影响的行数
     */
    @Update("UPDATE users SET username = #{username}, password = #{password} WHERE account = #{account}")
    int updateUser(UsersDTO usersDTO);
}