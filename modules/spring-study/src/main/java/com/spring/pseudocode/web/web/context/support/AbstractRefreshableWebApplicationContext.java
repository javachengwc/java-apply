package com.spring.pseudocode.web.web.context.support;

import com.spring.pseudocode.beans.factory.config.ConfigurableListableBeanFactory;
import com.spring.pseudocode.context.context.support.AbstractRefreshableConfigApplicationContext;
import com.spring.pseudocode.core.core.io.support.ResourcePatternResolver;
import com.spring.pseudocode.web.web.context.ConfigurableWebApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;
import org.springframework.ui.context.support.UiApplicationContextUtils;
import org.springframework.web.context.ConfigurableWebEnvironment;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public abstract class AbstractRefreshableWebApplicationContext extends AbstractRefreshableConfigApplicationContext
        implements ConfigurableWebApplicationContext, ThemeSource {
    private ServletContext servletContext;
    private ServletConfig servletConfig;
    private String namespace;
    private ThemeSource themeSource;

    public AbstractRefreshableWebApplicationContext()
    {
        //setDisplayName("Root WebApplicationContext");
    }

    public void setServletContext(ServletContext servletContext)
    {
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext()
    {
        return this.servletContext;
    }

    public void setServletConfig(ServletConfig servletConfig)
    {
        this.servletConfig = servletConfig;
        if ((servletConfig != null) && (this.servletContext == null))
            setServletContext(servletConfig.getServletContext());
    }

    public ServletConfig getServletConfig()
    {
        return this.servletConfig;
    }

    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
        if (namespace != null) {
            //setDisplayName("WebApplicationContext for namespace '" + namespace + "'");
        }
    }

    public String getNamespace()
    {
        return this.namespace;
    }

    public String[] getConfigLocations()
    {
        return super.getConfigLocations();
    }

    public String getApplicationName()
    {
        return this.servletContext != null ? this.servletContext.getContextPath() : "";
    }

    protected ConfigurableEnvironment createEnvironment()
    {
        return new StandardServletEnvironment();
    }

    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
    {
        //beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(this.servletContext, this.servletConfig));
        beanFactory.ignoreDependencyInterface(ServletContextAware.class);
        beanFactory.ignoreDependencyInterface(ServletConfigAware.class);

//        WebApplicationContextUtils.registerWebApplicationScopes(beanFactory, this.servletContext);
//        WebApplicationContextUtils.registerEnvironmentBeans(beanFactory, this.servletContext, this.servletConfig);
    }

    protected Resource getResourceByPath(String path)
    {
        //return new ServletContextResource(this.servletContext, path);
        return null;
    }

    protected ResourcePatternResolver getResourcePatternResolver()
    {
        //return new ServletContextResourcePatternResolver(this);
        return null;
    }

    protected void onRefresh()
    {
        //this.themeSource = UiApplicationContextUtils.initThemeSource(this);
    }

    protected void initPropertySources()
    {
//        ConfigurableEnvironment env = getEnvironment();
//        if ((env instanceof ConfigurableWebEnvironment)) {
//            ((ConfigurableWebEnvironment) env).initPropertySources(this.servletContext, this.servletConfig);
//        }
    }

    public Theme getTheme(String themeName)
    {
        return this.themeSource.getTheme(themeName);
    }
}
