package com.mmall.common;

public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "userName";

    public interface Role {
        Integer ROLE_CUSTOMER = 0; //普通用户
        Integer ROLE_ADMIN = 1;//管理员
    }
}
