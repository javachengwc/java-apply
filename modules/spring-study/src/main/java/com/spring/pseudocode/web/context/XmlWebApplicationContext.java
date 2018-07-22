package com.spring.pseudocode.web.context;


import com.spring.pseudocode.beans.factory.AutowireCapableBeanFactory;
import com.spring.pseudocode.context.ApplicationContext;

import javax.servlet.ServletContext;

public class XmlWebApplicationContext implements WebApplicationContext {

    public static final String DEFAULT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml";
    public static final String DEFAULT_CONFIG_LOCATION_PREFIX = "/WEB-INF/";
    public static final String DEFAULT_CONFIG_LOCATION_SUFFIX = ".xml";

    private String namespace;

    public String getNamespace()
    {
        return this.namespace;
    }

    protected String[] getDefaultConfigLocations()
    {
        if (getNamespace() != null) {
            return new String[] { "/WEB-INF/" + getNamespace() + ".xml" };
        }

        return new String[] { "/WEB-INF/applicationContext.xml" };
    }

    public  ServletContext getServletContext() {
        return null;
    }

    public  String getId() {
        return null;
    }

    public String getApplicationName() {
        return null;
    }

    public String getDisplayName() {
        return null;
    }

    public  long getStartupDate() {
        return 0L;
    }

    public  ApplicationContext getParent(){
        return null;
    }

    public  AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
        return null;
    }
}
