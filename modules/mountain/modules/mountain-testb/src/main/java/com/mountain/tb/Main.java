package com.mountain.tb;

import com.mountain.tb.service.TransConsumeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String args [])
    {
        ClassPathXmlApplicationContext applicationContext =  new ClassPathXmlApplicationContext("/spring-alone.xml");
        logger.info("Main testb start............");

        TransConsumeService transConsumeService =applicationContext.getBean("transConsumeService",TransConsumeService.class);
        transConsumeService.consume(1);

    }

}
