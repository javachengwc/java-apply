package com.boot.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 应用访问类
 * @EnableAutoConfiguration注解让Spring Boot根据应用所声明的依赖来对Spring框架进行自动配置
 */
@RestController
@EnableAutoConfiguration
public class BootController {

    @RequestMapping(value = "/boot")
    public String boot() {
        System.out.println("boot invoke");
        return  "boot";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(BootController.class, args);
    }
}
