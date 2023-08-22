package com.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.mybatis.dao")
@EnableTransactionManagement
public class MybatisApplication {

    private static Logger logger = LoggerFactory.getLogger(MybatisApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MybatisApplication.class, args);
        logger.info("----------MybatisApplication start-----------");
    }

}
