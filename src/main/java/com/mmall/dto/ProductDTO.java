package com.mmall.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ProductDTO {

    private Integer id;

    @NotNull(message = "请选择产品分类")
    private Integer categoryId;

    @NotEmpty(message = "产品名称必填")
    private String name;

    private String subtitle;

    @NotEmpty(message = "请上传主图")
    private String mainImage;

    private String subImages;

    @NotEmpty(message = "请配置富文本内容")
    private String detail;

    @NotNull(message = "请设置价格")
    @Min(value = 0,message = "请设置有效的价格")
    private BigDecimal price;

    @NotNull(message = "请设置库存")
    @Min(value = 1,message = "请设置有效的库存")
    private Integer stock;

    private Integer status;

    private String createTime;

    private String updateTime;
}
