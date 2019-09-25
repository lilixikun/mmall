package com.mmall.utils;

import com.mmall.Config.FtpConfig;
import com.mmall.common.ServerResponse;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FtpUtil {

    @Autowired
    private FtpConfig ftpConfig;

    private FTPClient ftpClient;

    private Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    /**
     * 上传
     * @param uploadFile
     * @return
     */
    public ServerResponse uploadToFtp(MultipartFile uploadFile) {

        //指定文件夹上传路径
        String pathDir = ftpConfig.getUploadFile();
        File dir = new File(pathDir);

        //判断目录是否存在，不存在则创建目录
        if (!dir.exists()) {
            dir.setWritable(true);
            //创建目录
            dir.mkdirs();
        }

        //获取原始文件名
        String originalFileName = uploadFile.getOriginalFilename();
        //2、使用UUID生成新文件名
        String newFileName = UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf('.'));

       boolean connect= connectServer();

       if (connect==false){
           return ServerResponse.createByErrorMessage("ftp服务器链接异常");
       }
        try {
            ftpClient.setBufferSize(1024);
            //开启被动模式（按自己如何配置的ftp服务器来决定是否开启）
            ftpClient.enterLocalPassiveMode();
            //进入到文件保存目录
            boolean c= ftpClient.changeWorkingDirectory(pathDir);
            //ftpClient.listFiles(pathDir);
            //以文件流形式储存
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            //执行上传
            ftpClient.storeFile(newFileName, uploadFile.getInputStream());
            //插入到数据库中
            return ServerResponse.createBySuccessMessage(ftpConfig.getImageBaseUrl()+newFileName);
        } catch (IOException e) {
            logger.error("上传文件异常", e);
            e.printStackTrace();
        } finally {
            //释放连接
            logout();
        }
        return ServerResponse.createBySuccessMessage("上传失败!");
    }


    public ServerResponse delFtp(String fileName){
        boolean connect=connectServer();
        if (connect==false){
            return ServerResponse.createByErrorMessage("ftp connetc err");
        }
        try {
            connect= ftpClient.deleteFile(fileName);
            if (connect==true){
                return ServerResponse.createBySuccess();
            }
        }catch (Exception e){
            logger.error("ftp delete err");
        }finally {
            logout();
        }
        return ServerResponse.createByErrorMessage("删除失败");
    }

    /**
     * 连接ftp服务器
     * @return
     */
    public boolean connectServer(){
        boolean isSuccess=false;
        ftpClient = new FTPClient();
        try {
            //连接服务器
            ftpClient.connect(ftpConfig.getAddress());
            //登录ftp
            isSuccess = ftpClient.login(ftpConfig.getUsername(), ftpConfig.getPassword());
           if (isSuccess){
               //设置字符集
               ftpClient.setControlEncoding("UTF-8");
               ftpClient.enterLocalPassiveMode();
           }
        } catch (IOException e) {
            logger.error("connet ftp err");
        }
        return isSuccess;
    }

    /**
     * 关闭连接
     */
    public void logout(){
        if (ftpClient!=null){
            try {
                ftpClient.disconnect();
            }catch (Exception e){
                logger.error("ftp disconnect err");
            }
        }
    }
}
