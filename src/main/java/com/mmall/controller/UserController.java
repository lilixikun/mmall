package com.mmall.controller;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.entity.User;
import com.mmall.exceptionHandle.MmallException;
import com.mmall.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @PostMapping("/login")
    public ServerResponse login(String username,String password, HttpSession session) {
        ServerResponse<User> serverResponse = userService.login(username, password);
        //是否登录成功
        if (serverResponse.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, serverResponse.getData());
        }
        return serverResponse;
    }

    @GetMapping("/logout")
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccessMessage("登出成功");
    }

    @PostMapping("/regist")
    public ServerResponse<String> regist(User user) {
        return userService.register(user);
    }

    @PostMapping("/checkVaild")
    public ServerResponse<String> checkVaild(String str, String type) {
        return userService.checkVaild(str, type);
    }

    /**
     * 获取用户信息
     * @param session
     * @return
     */
    @GetMapping("/getUserInfo")
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，需要强制登陆");
    }

    @GetMapping("/foegetByQuestion")
    public ServerResponse<String> foegetByQuestion(@RequestParam("username") String username){
        return userService.foegetByQuestion(username);
    }

    @PostMapping("/forgetCheckAnswer")
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String password,String answer){
        return userService.forgetCheckAnswer(username,password,answer);
    }

    @PostMapping("/forgetResetPassword")
    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        return userService.forgetResetPassword(username,passwordNew,forgetToken);
    }

    @PostMapping("/resetPassword")
    public ServerResponse<String> resetPassword(String password,String passwordNew,HttpSession session){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NO_LOGIN.getCode(),ResponseCode.NO_LOGIN.getDesc());
        }
        return userService.resetPasswor(password,passwordNew,user);
    }

    @PostMapping("/updateInfo")
    public ServerResponse<String> updateInfo(User user,HttpSession session) throws MmallException {
        User user1=(User) session.getAttribute(Const.CURRENT_USER);
        if (user1==null){
           throw new MmallException(ResponseCode.NO_LOGIN);
        }
        return userService.updateInfo(user);
    }
}
