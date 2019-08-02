package com.mmall.controller;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.entity.User;
import com.mmall.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     *
     * @param userName
     * @param password
     * @param session
     * @return
     */
    @PostMapping("login")
    public ServerResponse login(String userName,String password, HttpSession session) {
        ServerResponse<User> serverResponse = userService.login(userName, password);
        //是否登录成功
        if (serverResponse.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, serverResponse.getData());
        }
        return serverResponse;
    }

    @GetMapping("logout")
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccessMessage("登出成功");
    }

    @PostMapping("regist")
    public ServerResponse<String> regist(User user) {
        return userService.register(user);
    }

    @PostMapping("checkVaild")
    public ServerResponse<String> checkVaild(String str, String type) {
        return userService.checkVaild(str, type);
    }
}
