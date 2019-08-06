package com.mmall.mapper;

import com.mmall.entity.Product;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    List<Product> selectProducts();

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);
}