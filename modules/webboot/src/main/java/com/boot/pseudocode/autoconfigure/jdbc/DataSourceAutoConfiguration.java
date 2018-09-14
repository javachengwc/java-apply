package com.boot.pseudocode.autoconfigure.jdbc;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.jdbc.pool.DataSourceProxy;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDataSourceConfiguration;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvidersConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;

import javax.sql.DataSource;
import javax.sql.XADataSource;
import java.sql.SQLException;

@Configuration
@ConditionalOnClass({DataSource.class, EmbeddedDatabaseType.class})
@EnableConfigurationProperties({DataSourceProperties.class})
@Import({DataSourceInitializerPostProcessor.Registrar.class, DataSourcePoolMetadataProvidersConfiguration.class})
public class DataSourceAutoConfiguration
{
    private static final Log logger = LogFactory.getLog(DataSourceAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public DataSourceInitializer dataSourceInitializer(DataSourceProperties properties, ApplicationContext applicationContext) {
        //return new DataSourceInitializer(properties, applicationContext);
        return null;
    }

    public static boolean containsAutoConfiguredDataSource(ConfigurableListableBeanFactory beanFactory)
    {
        try
        {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition("dataSource");
            return EmbeddedDataSourceConfiguration.class.getName().equals(beanDefinition.getFactoryBeanName());
        } catch (NoSuchBeanDefinitionException ex) {
        }
        return false;
    }

    @Order(Integer.MAX_VALUE)
    static class DataSourceAvailableCondition extends SpringBootCondition
    {
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
        {
            //...
            return null;
        }

        private boolean hasBean(ConditionContext context, Class<?> type) {
            return BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context.getBeanFactory(), type,
                    true, false).length > 0;
        }
    }

    @Configuration
    @ConditionalOnProperty(prefix="spring.datasource", name={"jmx-enabled"})
    @ConditionalOnClass(name={"org.apache.tomcat.jdbc.pool.DataSourceProxy"})
    @Conditional({DataSourceAutoConfiguration.DataSourceAvailableCondition.class})
    @ConditionalOnMissingBean(name={"dataSourceMBean"})
    protected static class TomcatDataSourceJmxConfiguration
    {
        @Bean
        public Object dataSourceMBean(DataSource dataSource)
        {
            if ((dataSource instanceof DataSourceProxy)) {
                try {
                    return ((DataSourceProxy)dataSource).createPool().getJmxPool();
                }
                catch (SQLException ex) {
                    DataSourceAutoConfiguration.logger.warn("Cannot expose DataSource to JMX (could not connect)");
                }
            }
            return null;
        }
    }

    @Configuration
    //@Conditional({DataSourceAutoConfiguration.PooledDataSourceCondition.class})
    @ConditionalOnMissingBean({DataSource.class, XADataSource.class})
    //@Import({DataSourceConfiguration.Tomcat.class, DataSourceConfiguration.Hikari.class,
    // DataSourceConfiguration.Dbcp.class, DataSourceConfiguration.Dbcp2.class, DataSourceConfiguration.Generic.class})
    protected static class PooledDataSourceConfiguration
    {
    }

    //@Conditional({DataSourceAutoConfiguration.EmbeddedDatabaseCondition.class})
    @ConditionalOnMissingBean({DataSource.class, XADataSource.class})
    @Import({EmbeddedDataSourceConfiguration.class})
    protected static class EmbeddedDatabaseConfiguration
    {
    }
}
