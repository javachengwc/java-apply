package com.datastore.mysql.server.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.datastore.mysql.server.DbProperties;
import org.apache.commons.dbcp2.BasicDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan(basePackages = { "com.datastore.mysql" })
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

    @Bean
    public DataSource dataSource(DbProperties dbProperties) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(dbProperties.getDriverClassName());
        ds.setUrl(dbProperties.getUrl());
        ds.setUsername(dbProperties.getUserName());
        ds.setPassword(dbProperties.getPwd());
        logger.info("Config dataSource created..........");
        return ds;
    }

//    @Bean
//    public DataSource dataSource(DbProperties dbProperties) {
//        DruidDataSource ds = new DruidDataSource();
//        ds.setDriverClassName(dbProperties.getDriverClassName());
//        ds.setUrl(dbProperties.getUrl());
//        ds.setUsername(dbProperties.getUserName());
//        ds.setPassword(dbProperties.getPwd());
//        ds.setTestWhileIdle(false);
//        try {
//            ds.getConnection();
//        } catch (Exception e) {
//            logger.error("Config dataSource pre connection error,", e);
//        }
//        logger.info("Config dataSource created..........");
//        return ds;
//    }
}
