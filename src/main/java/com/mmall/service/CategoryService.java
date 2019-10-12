package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.entity.Category;

import java.util.List;

public interface CategoryService {

    /**
     * 添加/修改分类
     * @param category
     * @return
     */
    ServerResponse<String> categorySave(Category category);

    /**
     * 删除分裂以及下级
     * @param categoryId
     * @return
     */
    ServerResponse<String> deleteCategory(int categoryId);

    /**
     * 查询平级分裂列表
     * @param categoryId
     * @return
     */
    ServerResponse<Category> getCategory(int categoryId);

    /**
     * 查询所有的商品分类
     * @return
     */
    ServerResponse getAllCategory();

    /**
     * 递归查询所有categoryId 下的
     * @param categoryId
     * @return
     */
    ServerResponse getDeepCategory(int categoryId);
}
