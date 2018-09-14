package com.boot.pseudocode.autoconfigure.web;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

@AutoConfigureOrder(Integer.MIN_VALUE)
@Configuration
@ConditionalOnWebApplication
@Import({EmbeddedServletContainerAutoConfiguration.BeanPostProcessorsRegistrar.class})
public class EmbeddedServletContainerAutoConfiguration {

    public static class BeanPostProcessorsRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware
    {
        private ConfigurableListableBeanFactory beanFactory;

        public void setBeanFactory(BeanFactory beanFactory) throws BeansException
        {
            if ((beanFactory instanceof ConfigurableListableBeanFactory))
                this.beanFactory = ((ConfigurableListableBeanFactory)beanFactory);
        }

        public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry)
        {
            if (this.beanFactory == null) {
                return;
            }
            //...
        }
    }
}
