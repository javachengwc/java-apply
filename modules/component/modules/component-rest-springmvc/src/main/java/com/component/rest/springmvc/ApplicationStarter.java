package com.component.rest.springmvc;

import com.component.rest.springmvc.config.RestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;

public class ApplicationStarter {

    private static Logger logger = LoggerFactory.getLogger(ApplicationStarter.class);

    private static volatile AnnotationConfigEmbeddedWebApplicationContext instance;

    public static void run(Class<?> assemblyClass, String[] args) {

        Object[] sourcesClass =new Class[]{ApplicationStarter.class, assemblyClass};

        if (instance == null) {
            synchronized (ApplicationStarter.class) {
                if (instance == null) {
                    logger.info("ApplicationStarter start init instance");
                    SpringApplicationBuilder builder = new SpringApplicationBuilder(sourcesClass).sources(RestConfig.class);
                    instance = (AnnotationConfigEmbeddedWebApplicationContext)builder.run(args);
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

