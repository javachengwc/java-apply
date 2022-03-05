package com.es.consumer;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.es.consumer.dao")
public class EsConsumerApplication {

    private static Logger logger = LoggerFactory.getLogger(EsConsumerApplication.class);

    public static void main(String[] args) {
        logger.info("EsConsumerApplication start  begin........");
        SpringApplication.run(EsConsumerApplication.class, args);
        logger.info("EsConsumerApplication start success........");
    }
}
