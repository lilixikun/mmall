package com.mmall.mapper;

import com.mmall.entity.Media;

public interface MediaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Media record);

    int insertSelective(Media record);

    Media selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Media record);

    int updateByPrimaryKey(Media record);

    int deleteByPath(String path);
}