package com.spring.pseudocode.web.web.context;


import com.spring.pseudocode.beans.factory.AutowireCapableBeanFactory;
import com.spring.pseudocode.context.context.ApplicationContext;

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

    public Object getBean(String paramString) {
        return null;
    }

    public <T> T getBean(String paramString, Class<T> paramClass) {
        return null;
    }

    public <T> T getBean(Class<T> paramClass) {
        return null;
    }

    public Object getBean(String paramString, Object[] paramArrayOfObject) {
        return null;
    }

    public <T> T getBean(Class<T> paramClass, Object[] paramArrayOfObject) {
        return null;
    }

    public boolean containsBean(String paramString) {
        return false;
    }

    public boolean isSingleton(String paramString) {
        return false;
    }

    public boolean isPrototype(String paramString) {
        return false;
    }

    public boolean isTypeMatch(String paramString, Class<?> paramClass) {
        return false;
    }

    public Class<?> getType(String paramString) {
        return null;
    }

    public String[] getAliases(String paramString) {
        return new String[0];
    }
}
