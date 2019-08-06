package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.entity.Category;

import java.util.List;

public interface CategoryService {

    /**
     * 添加分类
     * @param categoryName
     * @param parentId
     * @return
     */
    ServerResponse<String> addCategory(String categoryName,int parentId);

    /**
     * 修改分类
     * @param categoryName
     * @param categoryId
     * @return
     */
    ServerResponse<String> updateCategory(String categoryName,int categoryId);

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
     * 递归查询所有categoryId 下的
     * @param categoryId
     * @return
     */
    ServerResponse getDeepCategory(int categoryId);
}
