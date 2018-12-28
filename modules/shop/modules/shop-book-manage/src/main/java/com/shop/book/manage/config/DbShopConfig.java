package com.shop.book.manage.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.shop.book.manage.dao.shop"}, sqlSessionFactoryRef = "shopSqlSessionFactory")
public class DbShopConfig {

    public static final String SHOP_TRANSACTION_MANAGER_NAME = "shopTransactionManager";

    @Bean(name = "shopDS")
    public DataSource shopDS(DbConfig dbConfig) {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(dbConfig.getShop().getDriverClassName());
        ds.setUrl(dbConfig.getShop().getUrl());
        ds.setUsername(dbConfig.getShop().getUsername());
        ds.setPassword(dbConfig.getShop().getPassword());
        ds.setMaxActive(dbConfig.getMaxActive());
        ds.setMinIdle(dbConfig.getMinIdle());
        ds.setMaxWait(dbConfig.getMaxWait());
        ds.setQueryTimeout(dbConfig.getQueryTimeout());
        ds.setTestWhileIdle(true);
        ds.setValidationQuery("select 1");
        ds.setTimeBetweenEvictionRunsMillis(30000);
        return ds;
    }

    @Bean("shopSqlSessionFactory")
    public SqlSessionFactory shopSqlSessionFactory(@Qualifier("shopDS") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/sqlmap/shop/**/*.xml"));
        SqlSessionFactory sqlSessionFactory = factoryBean.getObject();
        sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
        return sqlSessionFactory;
    }

    @Bean(name = "shopSqlSessionTemplate")
    public SqlSessionTemplate shopSqlSessionTemplate(@Qualifier("shopSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory);
        return template;
    }

    @Bean(name = SHOP_TRANSACTION_MANAGER_NAME)
    public DataSourceTransactionManager shopTransactionManager(@Qualifier("shopDS") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
