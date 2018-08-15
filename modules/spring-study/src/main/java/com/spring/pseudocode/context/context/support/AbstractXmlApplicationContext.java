package com.spring.pseudocode.context.context.support;

import com.spring.pseudocode.beans.BeansException;
import com.spring.pseudocode.beans.factory.xml.XmlBeanDefinitionReader;
import com.spring.pseudocode.context.context.ApplicationContext;
import com.spring.pseudocode.core.core.io.Resource;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.io.IOException;

public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext
{
    private boolean validating = true;

    public AbstractXmlApplicationContext()
    {
    }

    public AbstractXmlApplicationContext(ApplicationContext parent)
    {
        super(parent);
    }

    public void setValidating(boolean validating)
    {
        this.validating = validating;
    }

    //初始化了XmlBeanDefinitionReader，XmlBeanDefinitionReader是用来解析ApplicationContext.xml
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException
    {
        //创建ApplicationContext.xml文件的处理类XmlBeanDefinitionReader
        //XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        XmlBeanDefinitionReader beanDefinitionReader=null;

        //beanDefinitionReader.setEnvironment(getEnvironment());
        //beanDefinitionReader.setResourceLoader(this);
        //beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

        initBeanDefinitionReader(beanDefinitionReader);
        //加载ApplicationContext.xml文件
        loadBeanDefinitions(beanDefinitionReader);
    }

    protected void initBeanDefinitionReader(XmlBeanDefinitionReader reader)
    {
        reader.setValidating(this.validating);
    }

    protected void loadBeanDefinitions(XmlBeanDefinitionReader reader)throws BeansException, IOException
    {
        //获取配置文件路径
        Resource[] configResources = getConfigResources();
        if (configResources != null) {
            //reader.loadBeanDefinitions(configResources);
        }
//        String[] configLocations = getConfigLocations();
//        if (configLocations != null) {
//            reader.loadBeanDefinitions(configLocations);
//        }
    }

    protected Resource[] getConfigResources()
    {
        return null;
    }
}