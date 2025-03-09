package com.example.mynews.Controller;

import com.example.mynews.Service.UserService;
import com.example.mynews.pojo.DTO.UsersDTO;
import com.example.mynews.response.JsonResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录接口
     *
     * @param usersDTO 包含用户信息的对象
     * @return 登录结果
     */
    @PostMapping("/login")
    public JsonResult login(@RequestBody UsersDTO usersDTO, HttpSession session) {
        UsersDTO user = userService.login(usersDTO);

        session.setAttribute("user", user);
        return JsonResult.ok(user);
    }

    /**
     * 用户注册接口
     *
     * @param usersDTO 包含用户信息的对象
     * @return 注册结果
     */
    @PostMapping("/reg")
    public JsonResult register(@RequestBody UsersDTO usersDTO) {
        UsersDTO user = userService.register(usersDTO);
        return JsonResult.ok(user);
    }

    /**
     * 用户修改接口
     *
     * @param usersDTO 包含用户信息的对象
     * @return 修改结果
     */
    @PostMapping("/updateUser")
    public JsonResult updateUser(@RequestBody UsersDTO usersDTO) {
        UsersDTO user = userService.updateUser(usersDTO);
        return JsonResult.ok(user);
    }

}


