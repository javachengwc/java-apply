package com.database.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DatabaseToolApplication {

    private static Logger logger = LoggerFactory.getLogger(DatabaseToolApplication.class);

    public static void main(String[] args) {
        logger.info("DatabaseToolApplication start  begin........");
        SpringApplication.run(DatabaseToolApplication.class, args);
        logger.info("DatabaseToolApplication start success........");
    }

}
