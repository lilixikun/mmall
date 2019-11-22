package com.mmall.controller;

import com.mmall.AspectHand.AdminLoginRequired;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dto.ProductDTO;
import com.mmall.exceptionHandle.MmallException;
import com.mmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductManagerController {

    @Autowired
    private ProductService productService;

    @PostMapping("/productSave")
    @AdminLoginRequired
    public ServerResponse productSave(@RequestBody @Valid ProductDTO productDTO, BindingResult bindingResult) throws MmallException {
        if (bindingResult.hasErrors()) {
            throw new MmallException(ResponseCode.FORM_ERR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        return productService.productSave(productDTO);
    }

    @PostMapping("/setSaleStatus")
    @AdminLoginRequired
    public ServerResponse setSaleStatus(Integer productId, Integer status) {
        return productService.setSaleStatus(productId, status);
    }

    @GetMapping("/detail/{productId}")
    public ServerResponse getDetailById(@PathVariable("productId") Integer productId) {
        return productService.managerProductDetail(productId);
    }

    @DeleteMapping("/delete/{productId}")
    @AdminLoginRequired
    public ServerResponse delete(@PathVariable("productId") Integer productId) {
        return productService.managerProductDetail(productId);
    }

    @GetMapping("/list")
    public ServerResponse getList(@RequestParam(value = "pageNum", defaultValue = "1", required = false) Integer pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                  @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                  @RequestParam(value = "productName", required = false) String productName,
                                  @RequestParam(value = "status", required = false) Integer status,
                                  @RequestParam(value = "productId", required = false) Integer productId) {

        return productService.getList(pageNum, pageSize, categoryId, productName, productId,status);
    }
}
