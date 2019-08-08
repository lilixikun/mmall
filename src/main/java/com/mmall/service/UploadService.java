package com.mmall.service;

import com.mmall.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {

    /**
     * 上传文件
     * @param multipartFile
     * @return
     */
    ServerResponse<String> upload(MultipartFile multipartFile);

    /**
     * 删除文件
     * @param id
     * @return
     */
    ServerResponse deletePic(Integer id) throws IOException;

    /**
     * 下载文件
     * @param fileNmae
     * @return
     */
    ServerResponse download(String fileNmae)throws IOException;
}
