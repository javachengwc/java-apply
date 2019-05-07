package com.storefront.manage.config;


import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.storefront.manage.dao"}, sqlSessionFactoryRef = "manageSqlSessionFactory")
public class DbStorefrontConfig {

    public static final String MANAGE_TRANSACTION_MANAGER_NAME = "manageTransactionManager";

    @Bean(name = "manageDS")
    @Primary
    public DataSource manageDS(DbConfig dbConfig) {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(dbConfig.getStorefront().getDriverClassName());
        ds.setUrl(dbConfig.getStorefront().getUrl());
        ds.setUsername(dbConfig.getStorefront().getUsername());
        ds.setPassword(dbConfig.getStorefront().getPassword());
        ds.setMaxActive(dbConfig.getMaxActive());
        ds.setMinIdle(dbConfig.getMinIdle());
        ds.setMaxWait(dbConfig.getMaxWait());
        ds.setQueryTimeout(dbConfig.getQueryTimeout());
        ds.setTestWhileIdle(true);
        ds.setValidationQuery("select 1");
        ds.setTimeBetweenEvictionRunsMillis(30000);
        return ds;
    }

    @Bean("manageSqlSessionFactory")
    @Primary
    public SqlSessionFactory manageSqlSessionFactory(@Qualifier("manageDS") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/sqlmap/**/*.xml"));
        SqlSessionFactory sqlSessionFactory = factoryBean.getObject();
        sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
        return sqlSessionFactory;

    }

    @Bean(name = "manageSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate manageSqlSessionTemplate(@Qualifier("manageSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory);
        return template;
    }

    @Bean(name = MANAGE_TRANSACTION_MANAGER_NAME)
    @Primary
    public DataSourceTransactionManager manageTransactionManager(@Qualifier("manageDS") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
