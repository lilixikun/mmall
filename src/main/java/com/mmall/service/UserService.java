package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.entity.User;

public interface UserService {

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    ServerResponse<User> login(String userName,String password);

    /**
     * 注册用户
     * @param user
     * @return
     */
    ServerResponse<String> register (User user);

    /**
     * 校验用户名和邮箱是否正确
     * @param str
     * @param type
     * @return
     */
    ServerResponse<String> checkVaild(String str,String type);

    /**
     * 根据密码用户查询问题
     * @param username
     * @return
     */
    ServerResponse<String> foegetByQuestion(String username);

    /**
     * 验证密码问题是否正确
     * @param username
     * @param password
     * @param answer
     * @return
     */
    ServerResponse<String> forgetCheckAnswer(String username,String password,String answer);

    /**
     * 忘记密码重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken);

    /**
     * 登录状态下重置密码
     * @param password
     * @param passwordNew
     * @return
     */
    ServerResponse<String> resetPasswor(String password,String passwordNew,User user);

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    ServerResponse<String> updateInfo(User user);
}
