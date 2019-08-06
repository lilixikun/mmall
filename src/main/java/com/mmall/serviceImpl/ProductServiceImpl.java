package com.mmall.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dto.ProductDTO;
import com.mmall.entity.Product;
import com.mmall.exceptionHandle.MmallException;
import com.mmall.mapper.ProductMapper;
import com.mmall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Override
    public ServerResponse<String> productSave(ProductDTO productDTO) {
        if (productDTO.getId() != null) {
            //做修改
            Product product = new Product();
            BeanUtils.copyProperties(productDTO, product);
            int resluleCount = productMapper.updateByPrimaryKeySelective(product);
            if (resluleCount > 0) {
                return ServerResponse.createBySuccessMessage("修改产品成功!");
            }
        } else {
            //新增
            Product product = new Product();
            BeanUtils.copyProperties(productDTO, product);
            int resluleCount = productMapper.insertSelective(product);
            if (resluleCount > 0) {
                return ServerResponse.createBySuccessMessage("新增产品成功!");
            }
        }
        return ServerResponse.createByErrorMessage("操作失败");
    }

    @Override
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) throws MmallException {
        if (productId == null || status == null) {
            throw new MmallException(ResponseCode.FORM_ERR);
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int resultCount = productMapper.updateByPrimaryKeySelective(product);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("修改状态成功");
        }
        return ServerResponse.createByErrorMessage("操作失败");
    }

    @Override
    public ServerResponse<Product> selectById(Integer productId) throws MmallException{
        if (productId==null){
            throw new MmallException(ResponseCode.FORM_ERR);
        }
        Product product=productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.createByErrorMessage("未找到相关信息");
        }
        return ServerResponse.createBySuccess(product);
    }

    @Override
    public ServerResponse getList(Integer pageNum, Integer pageSize) {
        List<Product> productList=productMapper.selectProducts();
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<Product> productPageInfo=new PageInfo<>(productList,pageSize);
        return ServerResponse.createBySuccess(productPageInfo);
    }
}
