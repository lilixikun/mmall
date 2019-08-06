package com.mmall.exceptionHandle;

import com.mmall.common.ServerResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandle {
    @ExceptionHandler(value = MmallException.class)
    @ResponseBody
    public ServerResponse handle(Exception e) {
        if (e instanceof MmallException) {
            MmallException myException = (MmallException) e;
            return ServerResponse.createByErrorCodeMessage(myException.getCode(),myException.getMessage());
        }
       return ServerResponse.createByErrorCodeMessage(-10000,"系统错误");
    }
}
