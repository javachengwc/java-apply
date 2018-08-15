package com.spring.pseudocode.context.context.support;

import com.spring.pseudocode.beans.BeansException;
import com.spring.pseudocode.context.context.ApplicationContext;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContextException;

import java.io.IOException;

public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {

    private Boolean allowBeanDefinitionOverriding;

    private Boolean allowCircularReferences;

    private DefaultListableBeanFactory beanFactory;

    private final Object beanFactoryMonitor = new Object();

    public AbstractRefreshableApplicationContext()
    {
    }

    public AbstractRefreshableApplicationContext(ApplicationContext parent)
    {
        super(parent);
    }

    //初始化容器，并加载配置文件loadBeanDefinitions
    protected final void refreshBeanFactory()  throws BeansException
    {
        if (hasBeanFactory()) {
            destroyBeans();
            closeBeanFactory();
        }
        try {
            //创建Bean容器
            DefaultListableBeanFactory beanFactory = createBeanFactory();
            beanFactory.setSerializationId(getId());
            customizeBeanFactory(beanFactory);
            //加载配置文件
            loadBeanDefinitions(beanFactory);
            synchronized (this.beanFactoryMonitor) {
                this.beanFactory = beanFactory;
            }
        }
        catch (IOException ex) {
            throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);
        }
    }

    protected final boolean hasBeanFactory()
    {
        synchronized (this.beanFactoryMonitor) {
            return this.beanFactory != null;
        }
    }

    protected DefaultListableBeanFactory createBeanFactory()
    {
        //return new DefaultListableBeanFactory(getInternalParentBeanFactory());
        return  null;
    }

    protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory)
    {
        if (this.allowBeanDefinitionOverriding != null) {
            beanFactory.setAllowBeanDefinitionOverriding(this.allowBeanDefinitionOverriding.booleanValue());
        }
        if (this.allowCircularReferences != null)
            beanFactory.setAllowCircularReferences(this.allowCircularReferences.booleanValue());
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory paramDefaultListableBeanFactory) throws BeansException, IOException;
}