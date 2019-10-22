package com.mmall.controller;

import com.alibaba.fastjson.JSONObject;
import com.mmall.AspectHand.LoginRequired;
import com.mmall.common.ServerResponse;
import com.mmall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/list")
    @LoginRequired
    public ServerResponse list(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("token");
        return cartService.list(userId);
    }

    @PostMapping("/add")
    @LoginRequired
    public ServerResponse add(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        Integer userId = request.getIntHeader("token");
        return cartService.addCart(userId, jsonObject.getInteger("count"), jsonObject.getInteger("productId"));
    }

    @PostMapping("/updateCart")
    @LoginRequired
    public ServerResponse updateCart(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        Integer userId = (Integer) request.getIntHeader("token");
        return cartService.updateCart(userId, jsonObject.getInteger("count"), jsonObject.getInteger("productId"));
    }

    @DeleteMapping("/delete/{prodtcuId}")
    @LoginRequired
    public ServerResponse delete(HttpServletRequest request, @PathVariable("prodtcuId") String prodtcuId) {
        Integer userId = (Integer) request.getIntHeader("token");
        return cartService.deleteCart(userId, prodtcuId);
    }

    @PostMapping("/selectOrUnSelect")
    @LoginRequired
    public ServerResponse sellectOne(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        Integer userId = (Integer) request.getIntHeader("token");
        return cartService.selectOrUnSelect(userId, jsonObject.getInteger("productId"), jsonObject.getInteger("checked"));
    }

    @GetMapping("/getProductCount")
    public ServerResponse getProductCount(HttpServletRequest request) {
        Integer userId = (Integer) request.getIntHeader("token");
        if (userId == null || "".equals(userId)) {
            return ServerResponse.createBySuccess(0);
        }
        return cartService.getCartProductCount(userId);
    }
}
