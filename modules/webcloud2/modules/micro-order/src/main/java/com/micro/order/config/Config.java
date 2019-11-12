package com.micro.order.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;

@Configuration
//@ImportResource({"classpath:applicationContext.xml"})
public class Config implements EnvironmentAware {

    private static Logger logger = LoggerFactory.getLogger(Config.class);

    private static String [] activeProfiles= null;

    private static Environment environment;

    @Override
    public void setEnvironment(Environment env) {
        environment=env;
        activeProfiles = env.getActiveProfiles();
        logger.info("----------------------Application profile={}--------------------", activeProfiles);
    }

    public static String [] getActiveProfiles() {
        return activeProfiles;
    }

    public static Environment getEnvironment() {
        return environment;
    }

    @Bean
    public BeanPostProcessor beanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object o, String s) {
                logger.info("BeanPostProcessor object:" + o.getClass().getSimpleName());
                return o;
            }

            @Override
            public Object postProcessAfterInitialization(Object o, String s) {
                return o;
            }
        };
    }
}
