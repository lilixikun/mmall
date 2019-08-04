package com.mmall.controller;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.entity.User;
import com.mmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user/manager/")
public class UserManagerController {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    @ResponseBody
    public ServerResponse<User> login(String username, String paddword, HttpSession session){
        ServerResponse<User> response=userService.login(username,paddword);
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
