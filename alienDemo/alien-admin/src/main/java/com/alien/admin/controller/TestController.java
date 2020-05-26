package com.alien.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(value = "test",tags = "測試API")
@RequestMapping("/alien")
public class TestController {
    @GetMapping("/test/{id}")
    @ApiOperation(value = "測試详情", notes = "测试详情注意事项", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public  String  test(@PathVariable("id") Integer id){
        return "测试成功啦啦啦啦啦啦"+id;
    }
}
