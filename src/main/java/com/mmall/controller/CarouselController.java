package com.mmall.controller;

import com.mmall.common.ServerResponse;
import com.mmall.entity.Carousel;
import com.mmall.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carousel")
public class CarouselController {

    @Autowired
    private CarouselService carouselService;

    @GetMapping("/list")
    public ServerResponse queryCarousels(){
        return carouselService.queryCarousels();
    }

    @PostMapping("/addCarousel")
    public ServerResponse addCarousel(@RequestBody Carousel carousel){
        return carouselService.addCarousel(carousel);
    }

    @DeleteMapping("/delCarousel/{id}")
    public ServerResponse delCarousel(@PathVariable(value = "id",required = false) Integer id){
        return carouselService.delCarousel(id);
    }
}
