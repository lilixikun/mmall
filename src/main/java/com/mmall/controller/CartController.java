package com.mmall.controller;

import com.alibaba.fastjson.JSONObject;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.entity.User;
import com.mmall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/list")
    public ServerResponse list(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NO_LOGIN.getCode(), "用户未登录,请登录");
        }
        return cartService.list(user.getId());
    }

    @PostMapping("/add")
    public ServerResponse add(@RequestBody JSONObject jsonObject, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return cartService.addCart(user.getId(), jsonObject.getInteger("count"), jsonObject.getInteger("productId"));
    }

    @PostMapping("/updateCart")
    public ServerResponse updateCart(@RequestBody JSONObject jsonObject, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return cartService.updateCart(user.getId(), jsonObject.getInteger("count"), jsonObject.getInteger("productId"));
    }

    @DeleteMapping("/delete/{prodtcuId}")
    public ServerResponse delete(HttpSession session, @PathVariable("prodtcuId") String prodtcuId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return cartService.deleteCart(user.getId(), prodtcuId);
    }

    @PostMapping("/selectOrUnSelect")
    public ServerResponse sellectOne(@RequestBody JSONObject jsonObject, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return cartService.selectOrUnSelect(user.getId(), jsonObject.getInteger("productId"), jsonObject.getInteger("checked"));
    }

    @GetMapping("/getProductCount")
    public ServerResponse getProductCount(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createBySuccess(0);
        }
        return cartService.getCartProductCount(user.getId());
    }
}
