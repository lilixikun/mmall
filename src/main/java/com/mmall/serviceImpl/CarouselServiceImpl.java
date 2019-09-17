package com.mmall.serviceImpl;

import com.mmall.common.ServerResponse;
import com.mmall.entity.Carousel;
import com.mmall.entity.Media;
import com.mmall.mapper.CarouselMapper;
import com.mmall.service.CarouselService;
import com.mmall.utils.FtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {

    @Resource
    private CarouselMapper mapper;

    @Autowired
    private FtpUtil ftpUtil;

    @Override
    public ServerResponse queryCarousels() {
        List<Carousel> carousels=mapper.selectAll();
        return ServerResponse.createBySuccess(carousels);
    }

    @Override
    public ServerResponse addCarousel(Carousel carousel) {
        int resultCount=mapper.insertSelective(carousel);
        if (resultCount>0){
            return ServerResponse.createBySuccessMessage("添加成功");
        }
        return ServerResponse.createBySuccessMessage("添加失败");
    }

    @Override
    @Transactional
    public ServerResponse delCarousel(Integer id) {
        int resultCount=0;

        Carousel carousel=mapper.selectByPrimaryKey(id);
        String path=carousel.getUrl();
        //删除ftp上的图片
        ServerResponse response= ftpUtil.delFtp(path.substring(path.lastIndexOf("/")));

        //再删除数据库上的数据
        if (response.isSuccess()){
            //根据指定id删除
            resultCount=mapper.deleteByPrimaryKey(id);
            if (resultCount>0){
                return ServerResponse.createBySuccessMessage("删除成功");
            }
        }

        return ServerResponse.createBySuccessMessage("删除失败");
    }
}
