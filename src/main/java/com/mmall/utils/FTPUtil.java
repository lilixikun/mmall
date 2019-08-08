package com.mmall.utils;

import com.mmall.Config.FtpConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Data
public class FTPUtil {

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    private static String ftpIp = "127.0.01";
    private static String ftpUser = "xikun";
    private static String ftpPass = "xikun";
    private static String FTP_BASEPATH = "img";

    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    private boolean connectServer(String ip, int port, String user, String pwd) {

        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            //连接服务器
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user, pwd);
        } catch (IOException e) {
            log.error("连接FTP服务器异常", e);
        }
        return isSuccess;
    }

    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPass);
        log.info("开始连接ftp服务器");
        boolean result = ftpUtil.uploadFile(FTP_BASEPATH, fileList);
        log.info("开始连接ftp服务器,结束上传,上传结果:{}");
        return result;
    }


    public static boolean deleteFile(String fileName) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPass);
        log.info("开始连接ftp服务器");
        boolean result = ftpUtil.deleteFile(FTP_BASEPATH, fileName);
        return result;
    }

    public static InputStream download(String fileName) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPass);
        log.info("开始连接ftp服务器");
        InputStream result = ftpUtil.downloadFile(FTP_BASEPATH, fileName);
        return result;
    }

    /**
     * 上传文件 可以上传多张
     *
     * @param remotePath
     * @param fileList
     * @return
     * @throws IOException
     */
    public boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        //连接FTP服务器
        if (connectServer(this.ip, this.port, this.user, this.pwd)) {
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for (File fileItem : fileList) {
                    fis = new FileInputStream(fileItem);
                    //存储文件
                    ftpClient.storeFile(fileItem.getName(), fis);
                }

            } catch (IOException e) {
                log.error("上传文件异常", e);
                uploaded = false;
                e.printStackTrace();
            } finally {
                //释放连接
                fis.close();
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }

    /**
     * 删除文件
     *
     * @param remotePath
     * @param fileName
     * @return
     * @throws IOException
     */
    public boolean deleteFile(String remotePath, String fileName) throws IOException {

        boolean uploaded = true;
        if (connectServer(this.ip, this.port, this.user, this.pwd)) {
            try {
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //ftpClient.makeDirectory("img");
                ftpClient.changeWorkingDirectory(FTP_BASEPATH);
                boolean success = ftpClient.deleteFile(fileName);
                if (success == false) {
                    uploaded = false;
                }
                log.info("删除结果={}", success);
                log.info("删除成功");

            } catch (IOException e) {
                uploaded = false;
                log.info("删除文件失败");
                e.printStackTrace();
            } finally {
                ftpClient.disconnect();
            }
        }

        return uploaded;
    }

    /**
     * 下载文件
     * @param remotePath
     * @param fileName
     * @return
     */
    public InputStream downloadFile(String remotePath, String fileName){
        InputStream inputStream = null;
        if (connectServer(this.ip, this.port, this.user, this.pwd)) {
            try {
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.changeWorkingDirectory(FTP_BASEPATH);
                inputStream = ftpClient.retrieveFileStream(fileName);
                log.info("获取到的流={}",inputStream);
            } catch (Exception e) {
                log.info("获取失败");
            }
        }
        return inputStream;
    }
}
