package com.spring.pseudocode.web.web.context.support;

import com.spring.pseudocode.beans.BeansException;
import com.spring.pseudocode.beans.factory.AutowireCapableBeanFactory;
import com.spring.pseudocode.beans.factory.xml.XmlBeanDefinitionReader;
import com.spring.pseudocode.context.context.ApplicationContext;
import com.spring.pseudocode.context.context.ApplicationListener;
import com.spring.pseudocode.core.core.env.ConfigurableEnvironment;
import com.spring.pseudocode.web.web.context.WebApplicationContext;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.ResourceEntityResolver;

import javax.servlet.ServletContext;
import java.io.IOException;

public class XmlWebApplicationContext extends AbstractRefreshableWebApplicationContext {

    public static final String DEFAULT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml";
    public static final String DEFAULT_CONFIG_LOCATION_PREFIX = "/WEB-INF/";
    public static final String DEFAULT_CONFIG_LOCATION_SUFFIX = ".xml";

    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException
    {
        //XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        XmlBeanDefinitionReader beanDefinitionReader =null;
        beanDefinitionReader.setEnvironment(getEnvironment());
        //beanDefinitionReader.setResourceLoader(this);
        //beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

        initBeanDefinitionReader(beanDefinitionReader);
        loadBeanDefinitions(beanDefinitionReader);
    }

    protected void initBeanDefinitionReader(XmlBeanDefinitionReader beanDefinitionReader)
    {
    }

    protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws IOException
    {
        String[] configLocations = getConfigLocations();
        if (configLocations != null)
            for (String configLocation : configLocations)
                reader.loadBeanDefinitions(configLocation);
    }

    protected String[] getDefaultConfigLocations()
    {
        if (getNamespace() != null) {
            return new String[] { "/WEB-INF/" + getNamespace() + ".xml" };
        }
        return new String[] { "/WEB-INF/applicationContext.xml" };
    }
}
