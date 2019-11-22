package com.mmall.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mmall.common.ServerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RequestMapping
@RestController
public class TestController {
    @GetMapping("/select")
    public ServerResponse select() {

        String lists="[{\"id\":1,\"dockPn\":\"40AC015US\",\"footNoteId\":\"100000\",\"footNoteText\":\"\",\"compDescription\":\"Kensingtom\",\"dockDescription\":\"tHINKpAD\",\"compatibilityPn\":\"4XEON80914\"},{\"id\":2,\"dockPn\":\"40AC015US\",\"footNoteId\":\"100000\",\"footNoteText\":\"\",\"compDescription\":\"Haier\",\"dockDescription\":\"HUIPU\",\"compatibilityPn\":\"4XEON80914\"}]";

        //JSONArray objar = new JSONArray().toJavaList(lists);
        List list= new ArrayList<>();
        return ServerResponse.createBySuccess(lists);
    }
    @GetMapping("/delete")
    public String delete(){
        return "删除成功";
    }
}
