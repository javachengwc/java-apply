package com.micro.webcore;

import com.micro.webcore.config.RestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class ApplicationStarter {

    private static Logger logger = LoggerFactory.getLogger(ApplicationStarter.class);

    private static volatile ConfigurableApplicationContext instance;

    public static void run(Class<?> assemblyClass, String[] args) {

        if (instance == null) {
            synchronized (ApplicationStarter.class) {
                if (instance == null) {
                    logger.info("ApplicationStarter start init instance");
                    SpringApplicationBuilder builder = new SpringApplicationBuilder(ApplicationStarter.class,assemblyClass).sources(RestConfig.class);
                    instance = (ConfigurableApplicationContext)builder.run(args);
                    instance.registerShutdownHook();
                } else {
                    logger.warn("ApplicationStarter instance exists ");
                }
            }
        } else {
            logger.warn("ApplicationStarter instance exists");
        }
    }
}
