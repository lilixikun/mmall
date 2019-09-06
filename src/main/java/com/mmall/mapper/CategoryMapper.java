package com.mmall.mapper;

import com.mmall.dto.CategoryDTO;
import com.mmall.entity.Category;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    List<Category> selectAllCategory();

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<CategoryDTO> selectCategoryChildByParentId(Integer parentId);
}