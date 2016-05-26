package com.shopstat.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * spring启动util
 */
public class SpringContextLoadUtil {

    private static Logger logger = LoggerFactory.getLogger(SpringContextLoadUtil.class);

    private static ApplicationContext applicationContext = null;

    public static ApplicationContext getApplicationContext()
    {
        if(applicationContext==null)
        {
            synchronized(SpringContextLoadUtil.class)
            {
                if(applicationContext==null) {
                    try {
                        applicationContext = new ClassPathXmlApplicationContext("classpath:spring/*.xml");
                    } catch (Exception e) {
                        logger.error("SpringContextLoadUtil init error,", e);
                    }
                }
            }
        }
        return applicationContext;
    }
}
