package com.mmall.controller;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dto.UserDTO;
import com.mmall.entity.User;
import com.mmall.exceptionHandle.MmallException;
import com.mmall.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public ServerResponse login(@RequestBody User user) {
        ServerResponse<User> serverResponse = userService.login(user.getUserName(), user.getPassword());
        //是否登录成功
        if (serverResponse.isSuccess()) {
            String token= UUID.randomUUID().toString();
            //存入缓存redis
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
        User user=new User();
        BeanUtils.copyProperties(userDTO,user);
        return userService.register(user);
    }

    @GetMapping("/checkVaild")
    public ServerResponse<String> checkVaild(@RequestParam("str") String str,@RequestParam("type") String type) {
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
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆");
    }

    @GetMapping("/foegetByQuestion")
    public ServerResponse<String> foegetByQuestion(@RequestParam("userName") String userName){
        return userService.foegetByQuestion(userName);
    }

    @PostMapping("/forgetCheckAnswer")
    public ServerResponse<String> forgetCheckAnswer(@RequestBody UserDTO userDTO){
        return userService.forgetCheckAnswer(userDTO.getUserName(),userDTO.getQuestion(),userDTO.getAnswer());
    }

    @PostMapping("/forgetResetPassword")
    public ServerResponse<String> forgetResetPassword(@RequestBody UserDTO userDTO){
        return userService.forgetResetPassword(userDTO.getUserName(),userDTO.getPassword());
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
