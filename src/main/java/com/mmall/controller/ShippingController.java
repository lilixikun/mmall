package com.mmall.controller;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dto.ShippingDTO;
import com.mmall.entity.User;
import com.mmall.exceptionHandle.MmallException;
import com.mmall.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.Serializable;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    protected ShippingService shippingService;

    @GetMapping("/list")
    public ServerResponse list(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NO_LOGIN.getCode(), "用户未登录,请登录");
        }
        return shippingService.list(user.getId());
    }

    @PostMapping("/shipSave")
    public ServerResponse shipSave(@RequestBody @Valid ShippingDTO shipping,BindingResult bindingResult, HttpSession session) throws MmallException {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NO_LOGIN.getCode(), "用户未登录,请登录");
        }
        if (bindingResult.hasErrors()) {
            throw new MmallException(ResponseCode.FORM_ERR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        return shippingService.shipSave(user.getId(), shipping);
    }

    @GetMapping("/one/{id}")
    public ServerResponse one(@PathVariable("id") Integer id) {
        return ServerResponse.createBySuccess();
    }


    @GetMapping("/settingDef/{id}")
    public Serializable settingDef(@PathVariable("id") Integer id,HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NO_LOGIN.getCode(), "用户未登录,请登录");
        }
        return shippingService.settingDef(id,user.getId());
    }

    @DeleteMapping("/delete/{id}")
    public ServerResponse delete(HttpSession session, @PathVariable("id") Integer id) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NO_LOGIN.getCode(), "用户未登录,请登录");
        }
        return shippingService.del(id);
    }
}
