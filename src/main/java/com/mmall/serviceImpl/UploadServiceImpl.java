package com.mmall.serviceImpl;

import com.mmall.common.ServerResponse;
import com.mmall.entity.Img;
import com.mmall.mapper.ImgMapper;
import com.mmall.service.UploadService;
import com.mmall.utils.FTPUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {

    @Resource
    private ImgMapper imgMapper;

    @Value("${ftp.imageBaseUrl}")
    //返回地址
    public String imageBaseUrl;

    @Value("${ftp.uploadFile}")
    //上传路径
    public String uploadFile;


    @Override
    public ServerResponse<String> upload(MultipartFile multipartFile) {
        //指定文件夹上传路径
        String pathDir = uploadFile;
        File dir = new File(pathDir);

        //判断目录是否存在，不存在则创建目录
        if (!dir.exists()) {
            dir.setWritable(true);
            //创建目录
            dir.mkdirs();
        }

        //生成新文件名，防止文件名重复而导致文件覆盖
        //1、获取原文件后缀名 .img .jpg ....
        String originalFileName = multipartFile.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf('.'));
        //2、使用UUID生成新文件名
        String newFileName = UUID.randomUUID() + suffix;

        //生成文件
        File file = new File(dir, newFileName);
        List<File> fileList=new ArrayList<>();
        fileList.add(file);
        //本地上传
        try {
            multipartFile.transferTo(file);
            Img img=new Img();
            img.setPath(newFileName);
            imgMapper.insertSelective(img);
            //上传成功删除本地
            file.delete();
            System.out.println("上传文件成功！");
        } catch (IOException e) {
            System.out.println("上传文件失败！");
            e.printStackTrace();
        }
        //上传至ftp服务器
       try {
           boolean result= FTPUtil.uploadFile(fileList);
           if (result){
               return ServerResponse.createBySuccess(imageBaseUrl+file.getName());
           }
       }catch (Exception e){
           return ServerResponse.createByErrorMessage("上传失败");
       }
        return ServerResponse.createByErrorMessage("上传失败");
    }

    @Override
    public ServerResponse deletePic(Integer id) throws IOException {
        Img img=imgMapper.selectByPrimaryKey(id);
        boolean result=FTPUtil.deleteFile(img.getPath());
        if (result==true){
            return ServerResponse.createBySuccess("删除成功");
        }
        return ServerResponse.createByErrorMessage("删除失败");
    }

    @Override
    public ServerResponse download(String fileNmae) throws IOException {
        InputStream result=FTPUtil.download(fileNmae);
        return null;
    }
}
