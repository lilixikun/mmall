package com.mmall.service;

import com.mmall.common.ServerResponse;

public interface CartService {

    /**
     * 购物车列表
     * @param userId
     * @return
     */
    ServerResponse list(Integer userId);

    //查询用户选中购物车
    ServerResponse selectCheckCarts(Integer userId);

    /**
     * 添加购物车
     * @param count
     * @param productId
     * @return
     */
    ServerResponse addCart(Integer userId, Integer count, Integer productId);


    /**
     * 修改购物车数量
     * @param userId
     * @param count
     * @return
     */
    ServerResponse updateCart(Integer userId,Integer count,Integer productId);

    /**
     * 删除购物车里面商品
     * @param productIds
     * @return
     */
    ServerResponse deleteCart(Integer userId,String productIds);


    /**
     * 购物车选中不选中 全选操作
     * @param userId
     * @param productId
     * @param checked
     * @return
     */
    ServerResponse selectOrUnSelect (Integer userId,Integer productId,Integer checked);

    /**
     * 查询购物车商品数量
     * @param userId
     * @return
     */
    ServerResponse<Integer> getCartProductCount(Integer userId);
}
