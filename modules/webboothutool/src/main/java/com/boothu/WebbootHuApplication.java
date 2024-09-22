package com.boothu;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.boothu.dao")
public class WebbootHuApplication {

    private static Logger logger = LoggerFactory.getLogger(WebbootHuApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WebbootHuApplication.class, args);
        logger.info("----------WebbootHuApplication start-----------");
    }

}
