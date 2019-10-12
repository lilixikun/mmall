package com.mmall.AspectHand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface LoginRequired {
    /**
     * 是否需要登录
     * @return
     */
    boolean loginRequired() default true;
}
