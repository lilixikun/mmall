package com.mmall.serviceImpl;

import com.mmall.common.ServerResponse;
import com.mmall.dto.CategoryDTO;
import com.mmall.entity.Category;
import com.mmall.mapper.CategoryMapper;
import com.mmall.service.CategoryService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper mapper;

    @Override
    public ServerResponse<String> addCategory(Category category) {

        if (category.getParentId()==null){
            category.setParentId(0);
        }

        int resultCount = mapper.insertSelective(category);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("添加商品分类成功");
        }
        return ServerResponse.createByErrorMessage("添加商品分类失败");
    }

    @Override
    public ServerResponse<String> updateCategory(Category category) {
        int resultCount = mapper.updateByPrimaryKeySelective(category);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("修改成功");
        }
        return ServerResponse.createByErrorMessage("修改失败");
    }

    @Override
    public ServerResponse<String> deleteCategory(int categoryId) {
        int resultCount = mapper.deleteByPrimaryKey(categoryId);
        if (resultCount > 0) {
            return ServerResponse.createBySuccess("删除成功");
        }
        return ServerResponse.createByErrorMessage("删除失败");
    }

    @Override
    public ServerResponse<Category> getCategory(int categoryId) {
        Category category = mapper.selectByPrimaryKey(categoryId);
        if (category == null) {
            return ServerResponse.createByErrorMessage("未找到相关信息");
        }
        return ServerResponse.createBySuccess(category);
    }

    @Override
    public ServerResponse getAllCategory() {
        List<Category> categoryList = mapper.selectAllCategory();
        return ServerResponse.createBySuccess(categoryList);
    }

    @Override
    public ServerResponse getDeepCategory(int categoryId) {
        List<CategoryDTO> categoryList = mapper.selectCategoryChildByParentId(categoryId);
        return ServerResponse.createBySuccess(categoryList);
    }
}
