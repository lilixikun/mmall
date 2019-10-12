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
    public ServerResponse login(@RequestParam("userName") String userName, @RequestParam("password") String password, HttpSession session, HttpServletResponse servletResponse) {
        ServerResponse<User> response = userService.login(userName, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                //登录成功把userId写入session
                session.setAttribute("token", user.getId());
                //设置永不过期
                session.setMaxInactiveInterval(-1);
                String token = UUID.randomUUID().toString();
                //存入缓存redis
                servletResponse.addHeader("token", token);
                redisUtil.set(token, JSONObject.toJSONString(response.getData()), 7 * 60 * 60 * 60);
                return ServerResponse.createBySuccess(token);
            } else {
                return ServerResponse.createByErrorMessage("当前不是管理员");
            }
        }
        return response;
    }
}
