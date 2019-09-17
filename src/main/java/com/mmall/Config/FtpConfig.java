package com.mmall.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ftp")
public class FtpConfig {

    /** FTP ip地址*/
    private String address;

    /** FTP 端口号*/
    private Integer port;

    /** FTP 用户名*/
    private String username;

    /** FTP 密码*/
    private String password;

    /** FTP 上传目录*/
    private String uploadFile;

    /** 前端访问地址*/
    private String httpPath;

}
