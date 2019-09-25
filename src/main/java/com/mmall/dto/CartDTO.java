package com.mmall.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CartDTO implements Serializable {

    //购物车列表
    List<CartProductDTO> cartProductDTOList;
    //购物车总价
    private BigDecimal cartTotalPrice;
    //是否已经都勾选
    private Boolean allChecked;
    //图片域名
    private String imageHost;
}
