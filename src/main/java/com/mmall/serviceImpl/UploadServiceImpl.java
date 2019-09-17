package com.mmall.serviceImpl;

import com.mmall.Config.FtpConfig;
import com.mmall.common.ServerResponse;
import com.mmall.entity.Media;
import com.mmall.mapper.MediaMapper;
import com.mmall.service.UploadService;
import com.mmall.utils.FtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;

@Service
public class UploadServiceImpl implements UploadService {

    @Resource
    private MediaMapper mediaMapper;

    @Autowired
    private FtpUtil ftpUtil;

    @Autowired
    private FtpConfig ftpConfig;

    @Override
    public ServerResponse upload(MultipartFile multipartFile) {

        ServerResponse response= ftpUtil.uploadToFtp(multipartFile);

        if (response.isSuccess()){
            //上传成功把地址存入数据库
            Media media =new Media();
            media.setPath(response.getMsg());
            mediaMapper.insertSelective(media);
            HashMap data=new HashMap();
            data.put("url",ftpConfig.getHttpPath()+media.getPath());
            data.put("id",media.getId());
            return ServerResponse.createBySuccess(data);
        }

        return ServerResponse.createByErrorMessage("上传失败");
    }

    @Override
    public ServerResponse deletePic(String path){
        //删除ftp上的图片
        ServerResponse result=ftpUtil.delFtp(path.substring(path.lastIndexOf('/')));

        if (result.isSuccess()){
            //删除多媒体表中的记录
           int count= mediaMapper.deleteByPath(path);
           if (count>0){
               return ServerResponse.createBySuccess();
           }
        }

        return ServerResponse.createByErrorMessage("删除失败");
    }

    @Override
    public ServerResponse download(String fileNmae){
       // InputStream result=FTPUtil.download(fileNmae);
        return null;
    }
}
