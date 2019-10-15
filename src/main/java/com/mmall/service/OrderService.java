package com.mmall.service;

import com.mmall.common.ServerResponse;

import java.util.Map;

public interface OrderService {

    //调用支付接口
    ServerResponse pay(Integer userId, Long orderNo,String path);

    //支付宝回调
    ServerResponse alipayCallback(Map<String,String> map);

    //查询订单状态
    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);
}
