package com.mmall.serviceImpl;

import com.mmall.common.ServerResponse;
import com.mmall.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    public static final Long orderNo = 1493568000000L;

    private Logger log = LoggerFactory.getLogger(OrderServiceImplTest.class);

    @Test
    public void pay() {

       ServerResponse response= orderService.pay(1,orderNo,"upload");
       log.info("response={}",response);
    }
}