package com.mmall.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Data
public class ShippingDTO implements Serializable{
    private Integer id;

    //@NotNull(message = "缺少用户id")
    private Integer userId;

    @NotNull(message = "收货人姓名必填")
    @NotEmpty(message = "收货人姓名不能为空")
    private String receiverName;

    @NotNull(message = "手机号码必填")
    @Pattern(regexp ="^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$",message = "请输入有效的手机号码")
    private String receiverPhone;

    @NotNull(message = "收货人电话必填")
    private String receiverMobile;

    //@NotNull(message = "省份必填")
    private String receiverProvince;

    //@NotNull(message = "城市必填")
    private String receiverCity;

    //@NotNull(message = "区县必填")
    private String receiverDistrict;

    @NotNull(message = "详细必填")
    private String receiverAddress;

    @NotNull(message = "邮编必填")
    private String receiverZip;

    private Integer checked;

}
