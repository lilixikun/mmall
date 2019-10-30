package com.mmall.mapper;

import com.mmall.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    List<Order> selectAllOrder(@Param("orderNo")Long orderNo, @Param("startTime") String startTime,@Param("endTime") String endTime);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByUserIdAndOrderNo(@Param("userId") Integer userId,@Param("orderNo") Long orderNo);

    List<Order> selectByUserId(Integer userId);

    Order selectByOrderNo(String orderNo);
}