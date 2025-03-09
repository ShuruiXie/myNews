package com.example.mynews.Service;

import com.example.mynews.pojo.DTO.UsersDTO;

/**
 * 用户服务接口
 * 定义用户相关的业务操作
 */
public interface UserService {
    /**
     * 用户登录
     * 
     * @param usersDTO 用户实体
     * @return 登录成功的用户信息
     */
    UsersDTO login(UsersDTO usersDTO);

    /**
     * 用户注册
     *
     * @param usersDTO 用户实体
     * @return 注册结果
     */
    UsersDTO register(UsersDTO usersDTO);

    /**
     * 新增用户
     *
     * @param usersDTO 用户实体
     * @return 新增成功的信息
     */
    UsersDTO insertUser(UsersDTO usersDTO);

    /**
     * 修改用户信息
     *
     * @param usersDTO 用户实体
     * @return 修改成功的信息
     */
    UsersDTO updateUser(UsersDTO usersDTO);

}