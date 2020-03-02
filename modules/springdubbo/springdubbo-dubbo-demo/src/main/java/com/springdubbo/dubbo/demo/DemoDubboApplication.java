package com.springdubbo.dubbo.demo;

import com.springdubbo.dubbo.DubboStarter;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class DemoDubboApplication {

    public static void main(String[] args) {
        DubboStarter.run(DemoDubboApplication.class, args);
    }

}
