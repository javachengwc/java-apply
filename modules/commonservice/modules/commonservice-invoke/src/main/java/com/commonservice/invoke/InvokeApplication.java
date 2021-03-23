package com.commonservice.invoke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class InvokeApplication {

    private static Logger logger = LoggerFactory.getLogger(InvokeApplication.class);

    public static void main(String[] args) {
        logger.info("InvokeApplication start.....");
        SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(InvokeApplication.class);
        applicationBuilder.run(args);
        System.out.println("InvokeApplication start success .....");
    }

}
