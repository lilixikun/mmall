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
}
