package com.otd.boot.demo.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jsp")
@Api(value = "jsp接口", description = "jsp接口")
@Slf4j
public class JspController {

    //hello页面
    @RequestMapping("/hello")
    public String hello(){
        log.info("JspController hello 访问hello页面");
        return "hello";
    }

}
