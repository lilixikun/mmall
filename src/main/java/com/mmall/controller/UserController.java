package com.mmall.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mmall.AspectHand.LoginRequired;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dto.UserDTO;
import com.mmall.entity.User;
import com.mmall.exceptionHandle.MmallException;
import com.mmall.service.UserService;
import com.mmall.utils.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 登录
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public ServerResponse login(@RequestBody User user, HttpSession session, HttpServletResponse response) {
        ServerResponse<User> serverResponse = userService.login(user.getUserName(), user.getPassword());
        //是否登录成功
        if (serverResponse.isSuccess()) {
            String token = UUID.randomUUID().toString();

            //把userId存入session
            session.setAttribute("token", serverResponse.getData().getId());
            //设置永不过期
            session.setMaxInactiveInterval(-1);
            //存入缓存redis
            response.addHeader("token", token);
            redisUtil.set(token, JSONObject.toJSONString(serverResponse.getData()), 7 * 60 * 60 * 60);

            return ServerResponse.createBySuccess(token);
        }
        return serverResponse;
    }

    @GetMapping("/logout")
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccessMessage("登出成功");
    }

    @PostMapping("/regist")
    public ServerResponse<String> regist(@RequestBody @Valid UserDTO userDTO,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.FORM_ERR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return userService.register(user);
    }

    @GetMapping("/checkVaild")
    public ServerResponse<String> checkVaild(@RequestParam("str") String str, @RequestParam("type") String type) {
        return userService.checkVaild(str, type);
    }

    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    @GetMapping("/getUserInfo")
    @LoginRequired
    public ServerResponse<User> getUserInfo(HttpServletRequest request) {
        String redisKey = request.getHeader("token") + "";
        String result = redisUtil.get(redisKey);
        if (null == result || "" == result) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆");
        }
        User user = JSONObject.parseObject(result, User.class);
        return ServerResponse.createBySuccess(user);
    }

    @GetMapping("/foegetByQuestion")
    public ServerResponse<String> foegetByQuestion(@RequestParam("userName") String userName) {
        return userService.foegetByQuestion(userName);
    }

    @PostMapping("/forgetCheckAnswer")
    public ServerResponse<String> forgetCheckAnswer(@RequestBody UserDTO userDTO) {
        return userService.forgetCheckAnswer(userDTO.getUserName(), userDTO.getQuestion(), userDTO.getAnswer());
    }

    @PostMapping("/forgetResetPassword")
    public ServerResponse<String> forgetResetPassword(@RequestBody UserDTO userDTO) {
        return userService.forgetResetPassword(userDTO.getUserName(), userDTO.getPassword());
    }

    @PostMapping("/resetPassword")
    @LoginRequired
    public ServerResponse<String> resetPassword(String password, String passwordNew, HttpServletRequest request) {
        ServerResponse serverResponse = this.getUserInfo(request);
        if (serverResponse.isSuccess()) {
            User user = (User) serverResponse.getData();
            return userService.resetPasswor(password, passwordNew, user);
        }
        return null;
    }

    @PostMapping("/updateInfo")
    @LoginRequired
    public ServerResponse<String> updateInfo(@RequestBody User user, HttpSession session) throws MmallException {
        ServerResponse response = userService.updateInfo(user);
        //更新成功刷新缓存
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        } else {
            return ServerResponse.createBySuccessMessage("更新失败");
        }
        return ServerResponse.createBySuccess();
    }
}
