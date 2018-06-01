package com.commonservice.push;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class PushApplication {

    private static Logger logger = LoggerFactory.getLogger(PushApplication.class);

    public static void main(String[] args) {
        logger.info("PushApplication start.....");
        SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(PushApplication.class);
        applicationBuilder.web(true).run(args);
        System.out.println("PushApplication start success .....");
    }

}
