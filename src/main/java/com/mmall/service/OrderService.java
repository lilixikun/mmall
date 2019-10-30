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

    //创建订单
    ServerResponse<Object> creatOrder(Integer userId,Integer shipId);

    //取消订单
    ServerResponse cancel(Integer userId,Long orderNo);

    //获取用户订单列表
    ServerResponse getOrderList(Integer userId,int pageNum,int pageSize);

    //查询订单详情
    ServerResponse getOrderDetail(Integer userId,Long orderNo);



    //后台接口
    ServerResponse manageOrderList(int pageNum,int pageSize,Long orderNo, String startTime,String endTime);
    //详情
    ServerResponse manageOrderDetail(Long orderNo);
    //发货
    ServerResponse managerSendGoods(String orderNo);
}
