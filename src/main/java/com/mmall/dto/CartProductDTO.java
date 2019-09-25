package com.mmall.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartProductDTO {

    private Integer id;
    private Integer userId;
    private Integer productId;
    //购物车中此商品的数量
    private Integer quantity;
    //名称
    private String productName;
    //副标题
    private String productSubtitle;
    //图片
    private String productMainImage;
    //商品价格
    private BigDecimal productPrice;
    //商品状态
    private Integer productStatus;
    //产品总价
    private BigDecimal productTotalPrice;
    //库存数量
    private Integer productStock;
    //此商品是否勾选
    private Integer productChecked;
}
