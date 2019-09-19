package com.mmall.controller;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.entity.User;
import com.mmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user/manager")
public class UserManagerController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ServerResponse<User> login(@RequestParam("userName") String userName,@RequestParam("password") String password, HttpSession session){
        ServerResponse<User> response=userService.login(userName,password);
        if (response.isSuccess()){
            User user=response.getData();
            if (user.getRole()== Const.Role.ROLE_ADMIN){
                session.setAttribute(Const.CURRENT_USER,user);
                return response;
            }else {
                return ServerResponse.createByErrorMessage("当前不是管理员");
            }
        }
        return response;
    }
}
