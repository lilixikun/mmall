package com.mmall.controller;

import com.mmall.AspectHand.AdminLoginRequired;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.entity.Category;
import com.mmall.entity.User;
import com.mmall.service.CategoryService;
import com.mmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/manager/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/categorySave")
    @AdminLoginRequired
    public ServerResponse<String> categorySave(@RequestBody Category category) {

        return categoryService.categorySave(category);
    }

    @DeleteMapping("/delete/{categoryId}")
    @AdminLoginRequired
    public ServerResponse<String> deleteCategory(@PathVariable("categoryId") int categoryId) {
        return categoryService.deleteCategory(categoryId);
    }

    @GetMapping("/getCategory")
    public ServerResponse<Category> getCategory(@RequestParam(value = "categoryId", defaultValue = "0", required = false) int categoryId) {
        return categoryService.getCategory(categoryId);
    }

    @GetMapping("/allCategory")
    public ServerResponse<Category> getAllCategory() {
        return categoryService.getAllCategory();
    }

    @GetMapping("/getDeepCategory")
    public ServerResponse getDeepCategory(@RequestParam(value = "categoryId", defaultValue = "0", required = false) int categoryId) {
        return categoryService.getDeepCategory(categoryId);
    }
}
