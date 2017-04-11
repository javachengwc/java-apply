package com.boot.controller;

import com.boot.model.Uu;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/boot")
public class BootController {

    @RequestMapping(value = "/boot")
    public String boot() {
        System.out.println("boot invoke");
        return  "boot";
    }

    @RequestMapping("/{id}")
    @ResponseBody
     public Uu getUser(@PathVariable String id){
        Uu uu  = new Uu();
        uu.setId(id);
        uu.setUsername("uu");
        return uu;
    }

//    public static void main(String[] args) throws Exception {
//        SpringApplication.run(RtController.class, args);
//        new SpringApplicationBuilder().sources(RtController.class).run(args);
//    }

//    //修改内嵌tomcat端口
//    @Bean
//    public EmbeddedServletContainerFactory servletFactory(){
//        TomcatEmbeddedServletContainerFactory tomcatFactory =new TomcatEmbeddedServletContainerFactory();
//        tomcatFactory.setPort(8088);
//        tomcatFactory.setSessionTimeout(10, TimeUnit.SECONDS);
//        return tomcatFactory;
//    }
}
