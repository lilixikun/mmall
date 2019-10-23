package com.mmall.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.mmall.AspectHand.AdminLoginRequired;
import com.mmall.AspectHand.LoginRequired;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.service.OrderService;
import com.mmall.serviceImpl.CartServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    protected OrderService orderService;

    @RequestMapping("/pay")
    //@LoginRequired
    public ServerResponse pay(Long orderNo, HttpServletRequest request, HttpSession session) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        Integer userId = (Integer) session.getAttribute("toke");
        return orderService.pay(1, orderNo, path);
    }


    @RequestMapping("/alipayCallback")
    public Object alipayCallback(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();

        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {

                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        logger.info("支付宝回调,sign:{},trade_status:{},参数:{}", params.get("sign"), params.get("trade_status"), params.toString());

        //非常重要,验证回调的正确性,是不是支付宝发的.并且呢还要避免重复通知.

        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());

            if (!alipayRSACheckedV2) {
                return ServerResponse.createByErrorMessage("非法请求,验证不通过,再恶意请求我就报警找网警了");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常", e);
        }

        ServerResponse serverResponse = orderService.alipayCallback(params);
        if (serverResponse.isSuccess()) {
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }

    @RequestMapping("/queryOrderPayStatus")
    @LoginRequired
    public ServerResponse queryOrderPayStatus(Long orderNo, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("toke");
        ServerResponse response = orderService.queryOrderPayStatus(userId, orderNo);
        if (response.isSuccess()) {
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }

    @RequestMapping("/creatOrder")
    @LoginRequired
    public ServerResponse creatOrder(HttpSession session, Integer shipId) {
        Integer userId = (Integer) session.getAttribute("toke");
        return orderService.creatOrder(userId, shipId);
    }

    @RequestMapping(value = "/cancelOrder", method = RequestMethod.GET)
    @LoginRequired
    public ServerResponse cancelOrder(HttpSession session, @RequestParam(value = "orderNo") Long orderNo) {
        Integer userId = (Integer) session.getAttribute("toke");
        return orderService.cancel(userId, orderNo);
    }

    @RequestMapping(value = "/getOrderList", method = RequestMethod.GET)
    @LoginRequired
    public ServerResponse getOrderList(HttpSession session, @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        Integer userId = (Integer) session.getAttribute("toke");
        return orderService.getOrderList(userId, pageNum, pageSize);
    }

    @RequestMapping(value = "/getOrderDetail", method = RequestMethod.GET)
    @LoginRequired
    public ServerResponse getOrderDetail(HttpSession session, @RequestParam(value = "orderNo") Long orderNo) {
        Integer userId = (Integer) session.getAttribute("toke");
        return orderService.getOrderDetail(userId, orderNo);
    }

    @RequestMapping(value = "/manageOrderList", method = RequestMethod.GET)
    @AdminLoginRequired
    public ServerResponse manageOrderList(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                          @RequestParam(value = "startTime", required = false) String startTime,
                                          @RequestParam(value = "endTime", required = false) String endTime) {
        return orderService.manageOrderList(pageNum, pageSize);

    }

    @RequestMapping(value = "/manageOrderDetail/{orderNo}", method = RequestMethod.GET)
    @AdminLoginRequired
    public ServerResponse manageOrderDetail(@PathVariable("orderNo") Long orderNo) {
        return orderService.manageOrderDetail(orderNo);
    }

    @RequestMapping(value = "/managerSendGoods/{orderNo}", method = RequestMethod.GET)
    @AdminLoginRequired
    public ServerResponse managerSendGoods(@PathVariable("orderNo") String orderNo) {
        return orderService.managerSendGoods(orderNo);
    }
}
