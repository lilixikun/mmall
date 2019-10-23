package com.mmall.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO implements Serializable {

    private Long orderNo;

    private BigDecimal payment;

    private Integer paymentType;

    private String paymentTypeDesc;
    private Integer postage;

    private Integer status;


    private String statusDesc;

    private String paymentTime;

    private String sendTime;

    private String endTime;

    private String closeTime;

    private String createTime;

    //订单明细
    private List<OrderItemDTO> orderItemDTOList;

    private String imageHost;
    private Integer shippingId;
    private String receiverName;

    private ShippingDTO shippingVo;
}
