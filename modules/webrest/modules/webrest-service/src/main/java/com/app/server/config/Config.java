package com.app.server.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = { "com.app" })
@EnableConfigurationProperties({ DbProperties.class })
public class Config implements EnvironmentAware {

    private static Logger logger = LoggerFactory.getLogger(Config.class);

    private static final String  PROFILE_KEY="spring.profiles.active";
    private static final String  DATA_SOURCE_USER = "dataSourceUser";
    private static final String  SESSION_FACTORY_USER = "sessionFactoryUser";
    public static final String  TRANSACTION_USER = "transactionManagerUser";
    private static final String  MAPPERS_USER = "com.app.dao.tkmapper";

    private String profile;

    public String getProfile() {
        return profile;
    }

    @Override
    public void setEnvironment(Environment environment) {
        profile = environment.getProperty(PROFILE_KEY);
        logger.info("###############Application profile={}#############", profile);
    }

    @Bean(name = DATA_SOURCE_USER)
    @Primary
    public DataSource dataSource(DbProperties dbProperties) {

        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(dbProperties.getDriverClassName());
        ds.setUrl(dbProperties.getUrl());
        ds.setUsername(dbProperties.getUsername());
        ds.setPassword(dbProperties.getPassword());
        ds.setMaxActive(8); //最大连接数
        ds.setMinIdle(2);   //最小连接数
        ds.setMaxWait(20000); //获取连接时最大等待时间,20000毫秒
        ds.setQueryTimeout(20);  //查询超时时间 20秒
        ds.setTestWhileIdle(true);
        ds.setValidationQuery("select 1");
        ds.setTimeBetweenEvictionRunsMillis(30000);
        return ds;
    }

    @Bean(name = SESSION_FACTORY_USER)
    @Primary
    public SqlSessionFactoryBean sqlSessionFactory(@Qualifier(DATA_SOURCE_USER) DataSource dataSource,
                                                   DbProperties dbProperties) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        //sqlSessionFactoryBean.setTypeHandlersPackage("com.app.dao.typehandler");
        org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
        //配置项MapUnderscoreToCamelCase设置为true，数据库列name_ch将自动映射到pojo中的nameCh属性
        config.setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.setConfiguration(config);

        return sqlSessionFactoryBean;
    }

    @Bean
    @Primary
    public tk.mybatis.spring.mapper.MapperScannerConfigurer mapperScannerConfigurer() {
        tk.mybatis.spring.mapper.MapperScannerConfigurer configurer = new tk.mybatis.spring.mapper.MapperScannerConfigurer();
        configurer.setBasePackage(MAPPERS_USER);
        configurer.setSqlSessionFactoryBeanName(SESSION_FACTORY_USER);
        return configurer;
    }

    @Bean(name = TRANSACTION_USER)
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier(DATA_SOURCE_USER) DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
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
