package com.mmall.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mmall.entity.Category;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CategoryDTO implements Serializable {

    private Integer id;

    private Integer parentId;

    private String name;

    private Integer status;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Category> children = new ArrayList<>();

}

