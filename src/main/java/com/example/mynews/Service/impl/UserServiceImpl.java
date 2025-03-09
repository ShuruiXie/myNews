package com.example.mynews.Service.impl;

import com.example.mynews.pojo.DTO.UsersDTO;
import com.example.mynews.exception.BusinessException;
import com.example.mynews.exception.ValidationException;
import com.example.mynews.mapper.UsersMapper;
import com.example.mynews.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.mynews.response.*;

import java.util.Date;

/**
 * 用户服务实现类
 * 实现用户相关的业务操作
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public UsersDTO login(UsersDTO usersDTO) {
        Integer account=usersDTO.getAccount();
        String password=usersDTO.getPassword();

        // 参数校验
        if (account == null || password == null || password.trim().isEmpty()) {
            throw new ValidationException(StatusCode.PARAM_ERROR, "账号或密码不能为空");
        }

        // 查询用户是否存在
        UsersDTO user = usersMapper.getUserByAccount(account);
        if (user == null) {
            throw new BusinessException(StatusCode.USER_NOT_FOUND);
        }

        // 验证密码
        if (!password.equals(user.getPassword())) {
            throw new BusinessException(StatusCode.PASSWORD_ERROR);
        }

        // 返回用户信息（不包含密码）
        user.setPassword(null);
        return user;
    }



    @Override
    public UsersDTO register(UsersDTO usersDTO) {
        Integer account=usersDTO.getAccount();
        String password=usersDTO.getPassword();
        if(account == null || password == null){
            throw new ValidationException(StatusCode.PARAM_ERROR, "账号或密码不能为空");
        }

        //查询用户是否已存在
        UsersDTO user = usersMapper.getUserByAccount(account);
        if(user != null){
            //已存在
            throw new BusinessException(StatusCode.USERNAME_EXISTS);
        }

        UsersDTO users = new UsersDTO();
        users.setPassword(password);
        users.setAccount(account);
        users.setCreateTime(new Date());
        usersMapper.insertUser(users);
        return users;
    }

    @Override
    public UsersDTO insertUser(UsersDTO usersDTO) {
        UsersDTO user = new UsersDTO();
        if(usersDTO.getAccount() != null && usersDTO.getPassword() != null){
            user.setAccount(usersDTO.getAccount());
            user.setPassword(usersDTO.getPassword());
        }
        usersMapper.insertUser(user);
        return user;
    };

    @Override
    public UsersDTO updateUser(UsersDTO usersDTO) {
        Integer account=usersDTO.getAccount();
        String password=usersDTO.getPassword();
        String username = usersDTO.getUsername();
        UsersDTO user = usersMapper.getUserByAccount(account);
        if(username!=null){
            user.setUsername(username);
        }
        if(password!=null){
            user.setPassword(password);
        }
        user.setAccount(account);
        usersMapper.updateUser(user);
        return user;
    };
}