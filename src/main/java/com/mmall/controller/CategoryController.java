package com.mmall.controller;

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

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    @ResponseBody
    public ServerResponse<String> addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NO_LOGIN.getCode(), "用户未登录,请登录");
        }
        //校验一下是否是管理员
        if (userService.checkAdminRole(user)) {
            //增加我们处理分类的逻辑
            return categoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public ServerResponse<String> updateCategory(HttpSession session, String categoryName, int categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NO_LOGIN.getCode(), "用户未登录,请登录");
        }
        //校验一下是否是管理员
        if (userService.checkAdminRole(user)) {
            //修改逻辑
            return categoryService.updateCategory(categoryName, categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }

    @DeleteMapping("/delete/{categoryId}")
    public ServerResponse<String> deleteCategory(HttpSession session, @PathVariable("categoryId") int categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NO_LOGIN.getCode(), "用户未登录,请登录");
        }
        //校验一下是否是管理员
        if (userService.checkAdminRole(user)) {
            //删除逻辑
            return categoryService.deleteCategory(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }

    @GetMapping("/getCategory")
    public ServerResponse<Category> getCategory(@RequestParam(value = "categoryId", defaultValue = "0", required = false) int categoryId) {
        return categoryService.getCategory(categoryId);
    }

    @GetMapping("/getDeepCategory")
    public ServerResponse getDeepCategory(@RequestParam(value = "categoryId",defaultValue = "0",required = false) int categoryId) {
        return categoryService.getDeepCategory(categoryId);
    }
}
