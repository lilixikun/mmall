package com.mmall.mapper;

import com.mmall.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername (String username);

    int checkEmail (String email);

    User selectLogin(@Param("username")String username,@Param("password") String password);

    String selectQuestionByUserName(@Param("username") String username);

    int forgetCheckAnswer(@Param("username")String username,@Param("password") String password,@Param("answer") String answer);

    int checkPassword(@Param("password") String password,@Param("id") int id);

    int checkEmailByUserId (@Param("email") String email,@Param("id")int id);
}