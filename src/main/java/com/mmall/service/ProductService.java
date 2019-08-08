package com.mmall.service;


import com.mmall.common.ServerResponse;
import com.mmall.dto.ProductDTO;
import com.mmall.entity.Product;

import java.util.List;

public interface ProductService {

    /**
     * 新增修改产品
     * @param productDTO
     * @return
     */
    ServerResponse<String> productSave(ProductDTO productDTO);

    /**
     * 修改上下架状态
     * @param productId
     * @param ststua
     * @return
     */
    ServerResponse<String> setSaleStatus(Integer productId,Integer ststua);

    /**
     * 根据id查询产品详情
     * @param productId
     * @return
     */
    ServerResponse<ProductDTO> managerProductDetail(Integer productId);

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse getList(Integer pageNum,Integer pageSize,String productName,Integer productId);
}
