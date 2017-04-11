package com.boot.controller;

import com.boot.model.Uu;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@ComponentScan
@Configuration
@RestController
@RequestMapping("/rt")
public class RtController {

    @RequestMapping("/{id}")
    public Uu getUser(@PathVariable String id){
        Uu uu  = new Uu();
        uu.setId(id);
        uu.setUsername("uu");
        return uu;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RtController.class, args);
    }

    //修改内嵌tomcat端口
    @Bean
    public EmbeddedServletContainerFactory servletFactory(){
        TomcatEmbeddedServletContainerFactory tomcatFactory =new TomcatEmbeddedServletContainerFactory();
        tomcatFactory.setPort(8088);
        tomcatFactory.setSessionTimeout(10, TimeUnit.SECONDS);
        return tomcatFactory;
    }
}
