package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.entity.Carousel;

public interface CarouselService {

    /**
     * 查询轮播图列表
     * @return
     */
    ServerResponse queryCarousels();


    /**
     * 添加轮播图
     * @param url
     * @return
     */
    ServerResponse addCarousel(String url);


    /**
     * 删除
     * @param id
     * @return
     */
    ServerResponse delCarousel(Integer id);
}
