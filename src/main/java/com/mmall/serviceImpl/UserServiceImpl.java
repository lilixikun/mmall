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
        String md5Password = MD5Util.md5(password);
        System.out.print(md5Password);
        User user = userMapper.selectLogin(userName, md5Password);

        if (user == null) {
            return ServerResponse.createByErrorMessage("账户或密码错误");
        }
        user.setPassword("");
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse<String> register(User user) {

        //校验用户名
        ServerResponse response = checkVaild1(user.getUserName(), Const.USERNAME);
        if (!response.isSuccess()) {
            return response;
        }
        //校验邮箱
        response = checkVaild1(user.getEmail(), Const.EMAIL);
        if (!response.isSuccess()) {
            return response;
        }
        //默认设置成用户角色
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //md5加密
        user.setPassword(MD5Util.md5(user.getPassword()));

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
                if (resultCount == 0) {
                    return ServerResponse.createByErrorMessage("用户名不存在");
                }
            }

            if (Const.EMAIL.equals(type)) {
                //判断邮箱是否已经注册
                int resultCount = userMapper.checkEmail(str);
                if (resultCount == 0) {
                    return ServerResponse.createByErrorMessage("该邮箱未注册");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    public ServerResponse<String> checkVaild1(String str, String type) {
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

    @Override
    public ServerResponse<String> foegetByQuestion(String username) {

        ServerResponse validResponse = checkVaild1(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            //用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUserName(username);
        if (!question.isEmpty()) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码为空");
    }

    @Override
    public ServerResponse<String> forgetCheckAnswer(String userName, String question, String answer) {
        int resuleCount = userMapper.forgetCheckAnswer(userName, question, answer);
        if (resuleCount > 0) {
            //返回token 给前端
            return ServerResponse.createBySuccessMessage("忘记密码验证成功");
        }
        return ServerResponse.createByErrorMessage("忘记密码验证失败");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String userName, String passwordNew) {
//        if (forgetToken.isEmpty()){
//            return ServerResponse.createByErrorMessage("获取token 失败");
//        }
        ServerResponse validResponse = checkVaild1(userName, Const.USERNAME);
        if (validResponse.isSuccess()) {
            //用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        int rowCount = userMapper.updatePasswordByUsername(userName, passwordNew);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("重置密码成功");
        } else {
            return ServerResponse.createByErrorMessage("重置密码失败!");
        }
    }

    @Override
    public ServerResponse<String> resetPasswor(String password, String passwordNew, User user) {
        //防止横向越权，一定要指定用户下的password，如果用户密码相同就会误改
        int resuleCount = userMapper.checkPassword(password, user.getId());
        if (resuleCount < 1) {
            return ServerResponse.createByErrorMessage("未找到该用户信息");
        }

        user.setPassword(MD5Util.md5(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("密码已重置");
        }

        return ServerResponse.createByErrorMessage("重置密码失败");
    }

    @Override
    public ServerResponse<String> updateInfo(User user) {
        //校验email 是否存在,并且如果email 存在的话不能是当前用户的
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("email已存在，请更换email");
        }

        User newUser = new User();
        newUser.setAnswer(user.getAnswer());
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhone());
        newUser.setQuestion(user.getQuestion());
        int updateCount = userMapper.updateByPrimaryKeySelective(newUser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("更新信息c成功");
        }
        return ServerResponse.createByErrorMessage("更新信息失败");
    }

    @Override
    public boolean checkAdminRole(User user) {
        if (user.getRole().equals(Const.Role.ROLE_ADMIN)) {
            return true;
        }
        return false;
    }
}
