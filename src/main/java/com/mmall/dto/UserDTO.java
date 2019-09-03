package com.mmall.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class UserDTO implements Serializable {

    @NotNull
    @Length(min = 2,max = 20,message = "用户名在2-20个字段之间")
    private String userName;

    @NotNull
    @Length(min = 6,max = 16,message = "密码长度6-16")
    private String password;

    @NotNull
    @Pattern(regexp = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$",message = "请输入正确的邮箱")
    private String email;

    @NotNull
    @Pattern(regexp ="^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$",message = "请输入有效的手机号码")
    private String phone;

    @NotNull
    private String question;

    @NotNull
    private String answer;

}
