package com.cloud.feign.config;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * SpringBoot在写启动类的时候如果不使用@ComponentScan指明对象扫描范围，
 * 默认指扫描当前启动类所在的包里的对象，
 * 如果当前启动类没有包，则在启动时会报错：
 * Your ApplicationContext is unlikely to start due to a @ComponentScan ...
 * 因此启动类不能直接放在main/java文件夹下，
 * 必须要建一个包把它放进去或者使用@ComponentScan指明要扫描的包
 */
@Configuration
//@ComponentScan(basePackages = { "com.cloud.feign" })
public class Config {

    @Bean
    public BeanPostProcessor beanPostProcessor() {
        return new BeanPostProcessor() {
            public Object postProcessBeforeInitialization(Object o, String s) {

                System.out.println("BeanPostProcessor object:"+o.getClass().getSimpleName());
                return o;
            }

            public Object postProcessAfterInitialization(Object o, String s) {
                return o;
            }
        };
    }
}
