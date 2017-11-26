package com.cache;

import com.cache.redis.service.CountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ReduceMain {

    private static Logger logger = LoggerFactory.getLogger(ReduceMain.class);

    public static void main(String args[]) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                new String[]{"classpath:spring/ApplicationContext-context.xml",
                        "classpath:spring/ApplicationContext-redis.xml"
                });
        applicationContext.start();

        logger.info("ReduceMain started..............");
        CountService reduceService = applicationContext.getBean(CountService.class);
        try {
            //可不初始化，直接加减值
            reduceService.incrCount("ee", 12);
            System.out.println(reduceService.getCount("ee"));

            String counter="counter";
            reduceService.iniCount(counter,10);
            System.out.println(reduceService.getCount(counter));
            reduceService.decrCount(counter, 12);
            System.out.println(reduceService.getCount(counter));
            boolean rt=reduceService.iniCountIfAbsent(counter, 20);
            System.out.println(rt+" "+reduceService.getCount(counter));

        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            logger.info("ReduceMain exit.............");
            System.exit(0);
        }
    }
}