package com.mmall.serviceImpl;

import com.mmall.common.ServerResponse;
import com.mmall.dto.ShippingDTO;
import com.mmall.entity.Shipping;
import com.mmall.mapper.ShippingMapper;
import com.mmall.service.ShippingService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ShippingServiceImpl implements ShippingService {

    @Resource
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse list(Integer userId) {
        List<Shipping> shippingList = shippingMapper.selectShippingByUserId(userId);
        return ServerResponse.createBySuccess(shippingList);
    }

    @Override
    public ServerResponse shipSave(Integer userId, ShippingDTO shippingDTO) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(shippingDTO, shipping);
        //执行添加操作
        if (null == shipping.getId() || "".equals(shipping.getId())) {
            shippingMapper.insertSelective(shipping);
        } else {
            shippingMapper.updateByPrimaryKeySelective(shipping);
        }
        return ServerResponse.createBySuccess();
    }


    @Override
    public ServerResponse del(Integer shippingId) {
        int count = shippingMapper.deleteByPrimaryKey(shippingId);
        if (count > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("操作失败");
        }
    }
}
