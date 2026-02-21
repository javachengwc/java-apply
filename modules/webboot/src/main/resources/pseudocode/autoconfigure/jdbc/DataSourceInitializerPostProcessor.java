package com.boot.pseudocode.autoconfigure.jdbc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;

import javax.sql.DataSource;

class DataSourceInitializerPostProcessor implements BeanPostProcessor, Ordered
{
    private int order = Integer.MIN_VALUE;

    @Autowired
    private BeanFactory beanFactory;

    public int getOrder() { return this.order;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
    {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
    {
        if ((bean instanceof DataSource))
        {
            this.beanFactory.getBean(DataSourceInitializer.class);
        }
        return bean;
    }

    static class Registrar implements ImportBeanDefinitionRegistrar
    {
        private static final String BEAN_NAME = "dataSourceInitializerPostProcessor";

        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry)
        {
            if (!registry.containsBeanDefinition(BEAN_NAME)) {
                GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                beanDefinition.setBeanClass(DataSourceInitializerPostProcessor.class);
                beanDefinition.setRole(2);
                beanDefinition.setSynthetic(true);
                registry.registerBeanDefinition(BEAN_NAME, beanDefinition);
            }
        }
    }
}
