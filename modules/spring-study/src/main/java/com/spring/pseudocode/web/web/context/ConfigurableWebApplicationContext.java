package com.spring.pseudocode.web.web.context;

import com.spring.pseudocode.context.context.ConfigurableApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public abstract interface ConfigurableWebApplicationContext extends WebApplicationContext, ConfigurableApplicationContext
{
    public static final String APPLICATION_CONTEXT_ID_PREFIX = WebApplicationContext.class.getName() + ":";
    public static final String SERVLET_CONFIG_BEAN_NAME = "servletConfig";

    public abstract void setServletContext(ServletContext servletContext);

    public abstract void setServletConfig(ServletConfig servletConfig);

    public abstract ServletConfig getServletConfig();

    public abstract void setNamespace(String namespace);

    public abstract String getNamespace();

    public abstract void setConfigLocation(String configLocation);

    public abstract void setConfigLocations(String[] configLocations);

    public abstract String[] getConfigLocations();
}
