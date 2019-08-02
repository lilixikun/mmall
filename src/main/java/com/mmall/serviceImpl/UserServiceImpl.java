package com.mmall.serviceImpl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.entity.User;
import com.mmall.mapper.UserMapper;
import com.mmall.service.UserService;
import com.mmall.utils.MD5Util;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String userName, String password) {
        int resultCount = userMapper.checkUsername(userName);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        //md5后的密码去查询
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(userName, md5Password);

        if (user == null) {
            return ServerResponse.createByErrorMessage("账户或密码错误");
        }

        user.setPassword("");
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {

        ServerResponse validResponse = checkVaild(user.getUsername(), Const.USERNAME);
        if (validResponse.isSuccess()) {
            return validResponse;
        }

        validResponse = checkVaild(user.getEmail(), Const.EMAIL);
        if (validResponse.isSuccess()) {
            return validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        //md5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insertSelective(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功!");
    }

    @Override
    public ServerResponse<String> checkVaild(String str, String type) {

        //检验
        if (!StringUtils.isEmpty(type)) {
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                //检测用户名是否存在
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }

            if (Const.EMAIL.equals(type)) {
                //判断邮箱是否已经注册
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("该邮箱已注册");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }
}
