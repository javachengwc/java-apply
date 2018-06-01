package com.commonservice.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SmsApplication {

    private static Logger logger = LoggerFactory.getLogger(SmsApplication.class);

    public static void main(String[] args) {
        logger.info("SmsApplication start.....");
        SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(SmsApplication.class);
        applicationBuilder.web(true).run(args);
        System.out.println("SmsApplication start success .....");
    }

}
