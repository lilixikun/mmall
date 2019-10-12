package com.mmall.controller;

import com.mmall.AspectHand.AdminLoginRequired;
import com.mmall.common.ServerResponse;
import com.mmall.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/file")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @AdminLoginRequired
    public ServerResponse upload(@RequestParam("file") MultipartFile multipartFile) {

        return uploadService.upload(multipartFile);
    }

    @GetMapping(value = "/delete")
    @AdminLoginRequired
    public ServerResponse delete(@RequestParam("path") String path) {

        return uploadService.deletePic(path);
    }

    @GetMapping("/download")
    public ServerResponse download(@RequestParam("fileName") String fileName) {
        return uploadService.download(fileName);
    }
}
