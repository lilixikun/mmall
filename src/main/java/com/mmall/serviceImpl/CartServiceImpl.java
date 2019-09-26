package com.mmall.serviceImpl;

import com.mmall.Config.FtpConfig;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dto.CartDTO;
import com.mmall.dto.CartProductDTO;
import com.mmall.entity.Cart;
import com.mmall.entity.Product;
import com.mmall.exceptionHandle.MmallException;
import com.mmall.mapper.CartMapper;
import com.mmall.mapper.ProductMapper;
import com.mmall.service.CartService;
import com.mmall.utils.BigDecimalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    protected FtpConfig ftpConfig;

    @Autowired
    private BigDecimalUtil bigDecimalUtil;

    @Resource
    protected ProductMapper productMapper;

    @Resource
    private CartMapper cartMapper;

    private Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Override
    public ServerResponse<CartDTO> list(Integer userId) {
        CartDTO cartDTO = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartDTO);
    }

    @Override
    public ServerResponse addCart(Integer userId, Integer count, Integer productId) throws MmallException {
        if (count == null || productId == null) {
            throw new MmallException(ResponseCode.FORM_ERR);
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        //没有加入购物车
        if (cart == null) {
            Cart cartItem = new Cart();
            cartItem.setChecked(Const.Cart.CHECKED);
            cartItem.setProductId(productId);
            cartItem.setQuantity(count);
            cartItem.setUserId(userId);
            cartMapper.insertSelective(cartItem);
        } else {
            cart.setQuantity(cart.getQuantity() + count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return ServerResponse.createBySuccess();
    }


    @Override
    public ServerResponse updateCart(Integer userId, Integer count,Integer productId) {
        if (count == null || productId == null) {
            throw new MmallException(ResponseCode.FORM_ERR);
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        cart.setQuantity(count);
        cartMapper.updateByPrimaryKeySelective(cart);
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse deleteCart(Integer userId, String productIds) {
        String[] productArray = productIds.split(",");
        List productList = Arrays.asList(productArray);
        if (productList == null || productList.size() == 0) {
            throw new MmallException(ResponseCode.FORM_ERR);
        }
        cartMapper.deleteByUserIdProductIds(userId, productList);
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse selectOrUnSelect(Integer userId, Integer productId, Integer checked) {
        int result= cartMapper.checkedOrUncheckedProduct(userId, productId, checked);
        if (result>0){
            return ServerResponse.createBySuccess();
        }else {
            return ServerResponse.createBySuccessMessage("操作失败");
        }
    }

    @Override
    public ServerResponse<Integer> getCartProductCount(Integer userId) {
        if (userId == null) {
            return ServerResponse.createBySuccess(0);
        }
        Integer count = cartMapper.selecProductCount(userId);
        return ServerResponse.createBySuccess(count);
    }


    private CartDTO getCartVoLimit(Integer userId) {

        CartDTO cartDTO = new CartDTO();

        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductDTO> cartProductDTOList = new ArrayList<>();
        BigDecimal cartTotalPrice = new BigDecimal(0);

        for (Cart cartItem:cartList){
            CartProductDTO cartProductDTO=new CartProductDTO();
            cartProductDTO.setProductId(cartItem.getProductId());
            cartProductDTO.setId(cartItem.getId());
            cartProductDTO.setUserId(userId);

            //查询产品信息
            Product product=productMapper.selectByPrimaryKey(cartItem.getProductId());
            if (product!=null){
                //BeanUtils.copyProperties(cartProductDTO,product);  字段名不同
                cartProductDTO.setProductMainImage(product.getMainImage());
                cartProductDTO.setProductPrice(product.getPrice());
                cartProductDTO.setProductName(product.getName());
                cartProductDTO.setProductSubtitle(product.getSubtitle());
                cartProductDTO.setProductStatus(product.getStatus());
                cartProductDTO.setProductStock(product.getStock());
                //判断库存
                int buyLimitCount = 0;
                if (product.getStock()>=cartItem.getQuantity()){
                    //库存充足
                    buyLimitCount=cartItem.getQuantity();
                }else {
                    //库存不足
                    buyLimitCount=product.getStock();
                    //更新购物车库存
                    Cart cart=new Cart();
                    cart.setUserId(cartItem.getId());
                    cart.setQuantity(buyLimitCount);
                    cartMapper.updateByPrimaryKeySelective(cart);
                }
                cartProductDTO.setQuantity(buyLimitCount);
                //计算同类商品价格
                BigDecimal cartTotal=BigDecimalUtil.mul(cartItem.getQuantity(),product.getPrice().doubleValue());
                cartProductDTO.setProductTotalPrice(cartTotal);
                cartProductDTO.setProductChecked(cartItem.getChecked());
            }

            if (cartItem.getChecked()==Const.Cart.CHECKED){
                //如果商品是勾选 计算总价
                cartTotalPrice=BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductDTO.getProductTotalPrice().doubleValue());
            }
            cartProductDTOList.add(cartProductDTO);
        }

        cartDTO.setCartProductDTOList(cartProductDTOList);
        cartDTO.setCartTotalPrice(cartTotalPrice);
        cartDTO.setImageHost(ftpConfig.getImageBaseUrl());
        cartDTO.setAllChecked(this.getAllCheckedStatus(userId));
        return cartDTO;
    }

    private boolean getAllCheckedStatus(Integer userId){

        return cartMapper.selectCartProductCheckedStatusByUserId(userId)==0;
    }
}
