package com.bootmp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebbootMybatisPlusApplication {

    private static Logger logger = LoggerFactory.getLogger(WebbootMybatisPlusApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WebbootMybatisPlusApplication.class, args);
        logger.info("----------WebbootMybatisPlusApplication start-----------");
    }

}
