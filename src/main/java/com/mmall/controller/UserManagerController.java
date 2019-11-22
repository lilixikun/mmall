package com.mmall.controller;

import com.alibaba.fastjson.JSONObject;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.entity.User;
import com.mmall.service.UserService;
import com.mmall.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@RestController
@RequestMapping("/user/manager")
public class UserManagerController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/login")
    public ServerResponse login(@RequestParam("userName") String userName, @RequestParam("password") String password, HttpServletResponse servletResponse) {
        ServerResponse<User> response = userService.login(userName, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                Integer userId = response.getData().getId();
                //存入缓存redis
                servletResponse.addIntHeader("token", userId);
                redisUtil.set(userId.toString(), JSONObject.toJSONString(response.getData()), 7 * 60 * 60 * 60);
                return ServerResponse.createBySuccess(userId);
            } else {
                return ServerResponse.createByErrorMessage("当前不是管理员");
            }
        }
        return response;
    }
}
