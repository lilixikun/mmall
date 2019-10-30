package com.mmall.controller;

import com.mmall.AspectHand.AdminLoginRequired;
import com.mmall.AspectHand.LoginRequired;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.Serializable;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    protected ShippingService shippingService;

    @GetMapping("/list")
    @LoginRequired
    public ServerResponse list(HttpServletRequest request) {
        Integer userId = request.getIntHeader("token");
        return shippingService.list(userId);
    }

    @PostMapping("/shipSave")
    @LoginRequired
    public ServerResponse shipSave(@RequestBody @Valid ShippingDTO shipping, BindingResult bindingResult, HttpServletRequest request) throws MmallException {
        if (bindingResult.hasErrors()) {
            throw new MmallException(ResponseCode.FORM_ERR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        Integer userId = request.getIntHeader("token");
        return shippingService.shipSave(userId, shipping);
    }

    @GetMapping("/one/{id}")
    public ServerResponse one(@PathVariable("id") Integer id) {
        return ServerResponse.createBySuccess();
    }


    @GetMapping("/settingDef/{id}")
    public Serializable settingDef(@PathVariable("id") Integer id, HttpServletRequest request) {
        Integer userId = request.getIntHeader("token");
        return shippingService.settingDef(id, userId);
    }

    @DeleteMapping("/delete/{id}")
    @LoginRequired
    public ServerResponse delete(@PathVariable("id") Integer id) {

        return shippingService.del(id);
    }
}
