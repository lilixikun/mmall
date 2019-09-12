package com.mmall.controller;

import com.mmall.common.ServerResponse;
import com.mmall.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carousel")
public class CarouselController {

    @Autowired
    private CarouselService carouselService;

    @GetMapping("/list")
    public ServerResponse queryCarousels(){
        return carouselService.queryCarousels();
    }
}
