package com.mmall.serviceImpl;

import com.mmall.common.ServerResponse;
import com.mmall.entity.Carousel;
import com.mmall.mapper.CarouselMapper;
import com.mmall.service.CarouselService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {

    @Resource
    private CarouselMapper mapper;

    @Override
    public ServerResponse queryCarousels() {
        List<Carousel> carousels=mapper.selectAll();
        return ServerResponse.createBySuccess(carousels);
    }

    @Override
    public ServerResponse addCarousel(String url) {
        Carousel carousel=new Carousel();
        carousel.setUrl(url);
        int resultCount=mapper.insertSelective(carousel);
        if (resultCount>0){
            return ServerResponse.createBySuccessMessage("添加成功");
        }
        return ServerResponse.createBySuccessMessage("添加失败");
    }

    @Override
    public ServerResponse delCarousel(Integer id) {
        int resultCount=mapper.deleteByPrimaryKey(id);
        if (resultCount>0){
            return ServerResponse.createBySuccessMessage("删除成功");
        }
        return ServerResponse.createBySuccessMessage("删除失败");
    }
}
