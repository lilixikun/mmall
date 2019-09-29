package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.dto.ShippingDTO;

public interface ShippingService {

    /**
     * 查询收货地址列表
     *
     * @param userId
     * @return
     */
    ServerResponse list(Integer userId);

    /**
     * 添加/修改地址
     *
     * @param userId
     * @param shipping
     * @return
     */
    ServerResponse shipSave(Integer userId, ShippingDTO shipping);


    /**
     * 设置默认地址
     * @param id
     * @return
     */
    ServerResponse settingDef(Integer id,Integer userId);

    /**
     * 删除
     *
     * @param shippingId
     * @return
     */
    ServerResponse del(Integer shippingId);
}
