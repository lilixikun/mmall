package com.mmall.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ShippingDTO implements Serializable {
    private Integer id;

    @NotNull(message = "缺少用户id")
    private Integer userId;

    @NotNull(message = "收货人姓名必填")
    @NotEmpty(message = "收货人姓名不能为空")
    private String receiverName;

    private String receiverPhone;

    @NotNull(message = "收货人电话必填")
    private String receiverMobile;

    @NotNull(message = "省份必填")
    private String receiverProvince;

    @NotNull(message = "城市必填")
    private String receiverCity;

    @NotNull(message = "区县必填")
    private String receiverDistrict;

    @NotNull(message = "详细必填")
    private String receiverAddress;

    @NotNull(message = "邮编必填")
    private String receiverZip;
}
