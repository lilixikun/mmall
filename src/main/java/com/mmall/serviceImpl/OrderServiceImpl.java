package com.mmall.serviceImpl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dto.OrderDTO;
import com.mmall.dto.OrderItemDTO;
import com.mmall.dto.ShippingDTO;
import com.mmall.entity.*;
import com.mmall.mapper.*;
import com.mmall.service.OrderService;
import com.mmall.utils.BigDecimalUtil;
import com.mmall.utils.DateTimeUtil;
import com.mmall.utils.FtpUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    private static AlipayTradeService tradeService;

    private static String pattern = "yyyy-MM-dd HH:mm:ss";

    static {

        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }

    //@Value("${alipay.callbackUrl}")
    private static String callbackUrl = "http://zwcjvw.natappfree.cc/order/alipayCallback";

    @Autowired
    private FtpUtil ftpUtil;

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderItemMapper orderItemMapper;
    @Resource
    private PayInfoMapper payInfoMapper;
    @Resource
    private CartMapper cartMapper;
    @Resource
    private ShippingMapper shippingMapper;
    @Resource
    private ProductMapper productMapper;

    @Override
    public ServerResponse pay(Integer userId, Long orderNo, String path) {
        Map<String, String> map = new HashMap<>();
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return ServerResponse.createBySuccessMessage("用户没有该订单");
        }

        map.put("orderNo", String.valueOf(order.getOrderNo()));

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderNo().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append("扫码支付,订单号:").append(outTradeNo).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单").append(outTradeNo).append("购买商品共").append(totalAmount).append("元").toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");


        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

        List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(orderNo, userId);

        for (OrderItem orderItem : orderItemList) {
            GoodsDetail good = GoodsDetail.newInstance(orderItem.getProductId().toString(), orderItem.getProductName(),
                    BigDecimalUtil.mul(orderItem.getCurrentUnitPrice().doubleValue(), new Double(100).doubleValue()).longValue(), orderItem.getQuantity());
            goodsDetailList.add(good);
        }

        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        //GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "xxx小面包", 1000, 1);
        // 创建好一个商品后添加至商品明细列表
        //goodsDetailList.add(goods1);

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(callbackUrl) //支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                //打印信息
                dumpResponse(response);

                File folder = new File(path);
                if (!folder.exists()) {
                    folder.setWritable(true);
                    folder.mkdirs();
                }

                // 需要修改为运行机器上的路径
                String filePath = String.format(path + "/qr-%s.png", response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png", response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);

                log.info("filePath:{},fileName:{}", filePath, qrFileName);

                File targetFile = new File(path, qrFileName);

                ServerResponse serverResponse = null;
                //上传到服务器
                FileInputStream fileInputStream = null;
                try {
                    //file转成 MultipartFile
                    fileInputStream = new FileInputStream(targetFile);

                    MultipartFile multipartFile = new MockMultipartFile("file", targetFile.getName(), "text/plain", fileInputStream);
                    //执行上传
                    serverResponse = ftpUtil.uploadToFtp(multipartFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ;
                if (serverResponse.isSuccess()) {
                    //拼接上传后路径
                    String qrUrl = serverResponse.getMsg();
                    map.put("qrUrl", qrUrl);
                    return ServerResponse.createBySuccess(map);
                }

            case FAILED:
                log.error("支付宝预下单失败!!!");
                return ServerResponse.createBySuccessMessage("支付宝预下单失败!!!");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return ServerResponse.createBySuccessMessage("系统异常，预下单状态未知!!!");

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return ServerResponse.createBySuccessMessage("不支持的交易状态，交易返回异常!!!");
        }
    }

    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

    @Override
    public ServerResponse alipayCallback(Map<String, String> params) {
        Long outTradeNo = Long.parseLong(params.get("out_trade_no"));
        String tradeNo = params.get("trade_no");
        String tradStatus = params.get("trade_status");

        Order order = orderMapper.selectByOrderNo(tradeNo);
        if (order == null) {
            return ServerResponse.createByErrorMessage("订单号错误!");
        }
        if (order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()) {
            return ServerResponse.createByErrorMessage("支付宝重复调用!");
        }

        if (Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradStatus)) {
            //更新订单状态
            try {
                order.setPaymentTime(DateTimeUtil.parseDate(params.get("gmt_payment")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            order.setStatus(Const.OrderStatusEnum.PAID.getCode());
            orderMapper.updateByPrimaryKeySelective(order);
        }

        PayInfo payInfo = new PayInfo();
        payInfo.setId(order.getId());
        payInfo.setOrderNo(order.getOrderNo());
        payInfo.setPayPlatform(Const.PayPlatformEnum.ALIPAY.getCode());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradStatus);
        payInfoMapper.insertSelective(payInfo);
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse queryOrderPayStatus(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return ServerResponse.createBySuccessMessage("用户没有该订单");
        }
        if (order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse<Object> creatOrder(Integer userId, Integer shipId) {
        //查询用户购物车勾选
        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(userId);
        //计算订单总价
        ServerResponse response = this.getOrderItem(userId, cartList);
        if (!response.isSuccess()) {
            return response;
        }
        List<OrderItem> orderItemList = (List<OrderItem>) response.getData();
        BigDecimal payment = this.getOrderTotalPrice(orderItemList);

        //生成订单
        Order order = new Order();
        order.setUserId(userId);
        //生成订单号
        Long orderNo = System.currentTimeMillis() + new Random().nextInt(100);
        order.setOrderNo(orderNo);
        order.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());
        //运费
        order.setPostage(0);
        order.setPayment(payment);
        order.setPaymentType(Const.PaymentTypeEnum.ONLINE_PAY.getCode());
        //设置地址
        order.setShippingId(shipId);

        int result = orderMapper.insertSelective(order);
        if (result > 0) {
            //管理订单号到明细表
            for (OrderItem orderItem : orderItemList) {
                orderItem.setOrderNo(order.getOrderNo());
            }
            //mybatis 批量插入
            orderItemMapper.batchInsert(orderItemList);
            //插入后减库存
            for (OrderItem orderItem : orderItemList) {
                Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
                product.setStock(product.getStock() - orderItem.getQuantity());
                productMapper.updateByPrimaryKeySelective(product);
            }

            //清空购物车
            for (Cart cart : cartList) {
                cartMapper.deleteByPrimaryKey(cart.getId());
            }
            return ServerResponse.createBySuccess(orderNo);

        } else {
            return ServerResponse.createBySuccessMessage("生成订单失败");
        }
    }


    @Override
    public ServerResponse cancel(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return ServerResponse.createByErrorMessage("订单不存在");
        }

        if (order.getStatus() != Const.OrderStatusEnum.NO_PAY.getCode()) {
            return ServerResponse.createByErrorMessage("已付款,无法取消订单");
        }

        order.setStatus(Const.OrderStatusEnum.CANCELED.getCode());
        int row = orderMapper.updateByPrimaryKeySelective(order);
        if (row > 0) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse getOrderList(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectByUserId(userId);

        List<OrderDTO> orderDTOList = getOrderVoList(orderList, userId);

        PageInfo<OrderDTO> orderPageInfo = new PageInfo<>(orderDTOList, pageSize);

        return ServerResponse.createBySuccess(orderPageInfo);
    }

    @Override
    public ServerResponse getOrderDetail(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order != null) {
            List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(orderNo, userId);

            OrderDTO orderDTO = this.getOrderDTO(order, orderItemList);
            return ServerResponse.createBySuccess(orderDTO);
        }
        return ServerResponse.createByErrorMessage("没有找到该订单");
    }

    @Override
    public ServerResponse manageOrderList(int pageNum, int pageSize, Long orderNo, String startTime, String endTime) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectAllOrder(orderNo, startTime, endTime);
        PageInfo<Order> orderPageInfo = new PageInfo<>(orderList, pageSize);
        return ServerResponse.createBySuccess(orderPageInfo);
    }

    @Override
    public ServerResponse manageOrderDetail(Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(null, orderNo);
        if (order != null) {
            List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(orderNo, null);

            OrderDTO orderDTO = this.getOrderDTO(order, orderItemList);
            return ServerResponse.createBySuccess(orderDTO);
        }
        return ServerResponse.createByErrorMessage("没有找到该订单");
    }

    @Override
    public ServerResponse managerSendGoods(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            return ServerResponse.createByErrorMessage("订单不存在");
        }
        if (order.getStatus() == Const.OrderStatusEnum.PAID.getCode()) {
            order.setStatus(Const.OrderStatusEnum.SHIPPED.getCode());
            order.setSendTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            return ServerResponse.createByErrorMessage("订单未付款");
        }
        return ServerResponse.createBySuccess();
    }

    //计算购物车总价
    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList) {
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList) {
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }
        return payment;
    }

    //拼接orderItem
    private ServerResponse<List<OrderItem>> getOrderItem(Integer userId, List<Cart> cartList) {
        if (CollectionUtils.isEmpty(cartList)) {
            return ServerResponse.createByErrorMessage("购物车为空");
        }
        List<OrderItem> orderItemList = new ArrayList<>();
        for (Cart cart : cartList) {
            OrderItem orderItem = new OrderItem();
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            if (Const.ProductStatusEnum.ON_SALE.getCode() != product.getStatus()) {
                return ServerResponse.createByErrorMessage("不是在线售卖状态");
            }
            //校验库存
            if (cart.getQuantity() > product.getStock()) {
                return ServerResponse.createByErrorMessage("产品" + product.getName() + "库存不足");
            }
            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cart.getQuantity()));
            orderItemList.add(orderItem);
        }
        return ServerResponse.createBySuccess(orderItemList);
    }

    //拼接orderDTO
    private OrderDTO getOrderDTO(Order order, List<OrderItem> orderItemList) {
        OrderDTO orderVo = new OrderDTO();

        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTypeDesc(Const.PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());

        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(Const.OrderStatusEnum.codeOf(order.getStatus()).getValue());

        orderVo.setShippingId(order.getShippingId());
        //查询地址详情
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        if (shipping != null) {
            orderVo.setReceiverName(shipping.getReceiverName());
            ShippingDTO shippingVo = new ShippingDTO();
            shippingVo.setReceiverName(shipping.getReceiverName());
            shippingVo.setReceiverAddress(shipping.getReceiverAddress());
            shippingVo.setReceiverProvince(shipping.getReceiverProvince());
            shippingVo.setReceiverCity(shipping.getReceiverCity());
            shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
            shippingVo.setReceiverMobile(shipping.getReceiverMobile());
            shippingVo.setReceiverZip(shipping.getReceiverZip());
            shippingVo.setReceiverPhone(shippingVo.getReceiverPhone());
            orderVo.setShippingVo(shippingVo);
        }
        orderVo.setPaymentTime(DateTimeUtil.formatDatetime(order.getPaymentTime(), pattern));
        orderVo.setSendTime(DateTimeUtil.formatDatetime(order.getSendTime(), pattern));
        orderVo.setEndTime(DateTimeUtil.formatDatetime(order.getEndTime(), pattern));
        orderVo.setCreateTime(DateTimeUtil.formatDatetime(order.getCreateTime(), pattern));
        orderVo.setCloseTime(DateTimeUtil.formatDatetime(order.getCloseTime(), pattern));

        List<OrderItemDTO> orderItemVoList = new ArrayList<>();
        for (OrderItem orderItem : orderItemList) {
            OrderItemDTO orderItemVo = new OrderItemDTO();
            orderItemVo.setOrderNo(orderItem.getOrderNo());
            orderItemVo.setProductId(orderItem.getProductId());
            orderItemVo.setProductName(orderItem.getProductName());
            orderItemVo.setProductImage(orderItem.getProductImage());
            orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
            orderItemVo.setQuantity(orderItem.getQuantity());
            orderItemVo.setTotalPrice(orderItem.getTotalPrice());
            orderItemVo.setCreateTime(DateTimeUtil.formatDatetime(orderItem.getCreateTime(), pattern));
            orderItemVoList.add(orderItemVo);
        }
        orderVo.setOrderItemDTOList(orderItemVoList);
        return orderVo;
    }

    private List<OrderDTO> getOrderVoList(List<Order> orderList, Integer userId) {
        List<OrderDTO> orderDTOList = new ArrayList<>();

        for (Order order : orderList) {
            //根据订单查询 订单详情
            List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(order.getOrderNo(), userId);
            //拼接orderDTO
            OrderDTO orderDTO = getOrderDTO(order, orderItemList);
            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }
}
