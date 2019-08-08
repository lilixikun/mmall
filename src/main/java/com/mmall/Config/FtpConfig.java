package com.mmall.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ftp")
public class FtpConfig {

    private String address;

    private Integer port;

    private String username;

    private String password;

    private String bastPath;

    private String imageBaseUrl;

}
