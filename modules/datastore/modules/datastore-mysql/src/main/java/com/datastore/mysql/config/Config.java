package com.datastore.mysql.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.datastore.mysql.dao")
public class Config {

    private static Logger logger = LoggerFactory.getLogger(Config.class);

    @Bean
    public BeanPostProcessor beanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object o, String s) {

                System.out.println("BeanPostProcessor object:"+o.getClass().getSimpleName());
                return o;
            }

            @Override
            public Object postProcessAfterInitialization(Object o, String s) {
                return o;
            }
        };
    }

}
