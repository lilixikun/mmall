package com.mmall.mapper;

import com.mmall.entity.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {

    List<Shipping> selectShippingByUserId(Integer userId);

    int deleteByPrimaryKey(Integer id);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int settingDef(Integer userId);
}