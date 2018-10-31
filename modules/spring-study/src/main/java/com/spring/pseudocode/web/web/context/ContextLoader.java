package com.spring.pseudocode.web.web.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ContextLoader {

    private static Map<ClassLoader, WebApplicationContext> currentContextPerThread= new HashMap<ClassLoader, WebApplicationContext>();
    private static volatile WebApplicationContext currentContext;
    private static final String DEFAULT_STRATEGIES_PATH = "ContextLoader.properties";
    private static final Properties defaultStrategies;

    private WebApplicationContext context;

    private BeanFactoryReference parentContextRef;

    static {
        try {
            ClassPathResource resource = new ClassPathResource(DEFAULT_STRATEGIES_PATH, ContextLoader.class);
            defaultStrategies = PropertiesLoaderUtils.loadProperties(resource);
        }
        catch (IOException ex) {
            throw new IllegalStateException("Could not load 'ContextLoader.properties': " + ex.getMessage());
        }
    }

    public ContextLoader()
    {
    }

    public ContextLoader(WebApplicationContext context)
    {
        this.context = context;
    }

    //ServletContext是web应用级的上下文。web容器比如tomcat启动的时候，会为每个web应用程序创建一个ServletContext对象，代表当前web应用的上下文
    //每个web应用有且仅创建一个ServletContext，一个web中的所有servlet共享一个ServletContext对象
    //这里是web应用启动时创建IoC容器WebApplicationContext的入口，WebApplicationContext创建后，会放入ServletContext的属性中，以便于获取
    public WebApplicationContext initWebApplicationContext(ServletContext servletContext)
    {
        if (servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null) {
            throw new IllegalStateException("Cannot initialize context because there is already a root application context present ");
        }

        Log logger = LogFactory.getLog(ContextLoader.class);
        servletContext.log("Initializing Spring root WebApplicationContext");
        if (logger.isInfoEnabled()) {
            logger.info("Root WebApplicationContext: initialization started");
        }
        long startTime = System.currentTimeMillis();
        try
        {
            //创建IoC容器WebApplicationContext
            if (this.context == null) {
               this.context = createWebApplicationContext(servletContext);
            }
            if ((this.context instanceof ConfigurableWebApplicationContext)) {
                ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext)this.context;
                if (!cwac.isActive())
                {
                    if (cwac.getParent() == null)
                    {
//                        ApplicationContext parent = loadParentContext(servletContext);
//                        cwac.setParent(parent);
                    }
                    configureAndRefreshWebApplicationContext(cwac, servletContext);
                }
            }
            //Spring的IoC容器WebApplicationContext，以WebApplicationContext.ROOTWEBAPPLICATIONCONTEXTATTRIBUTE为属性Key
            //存储到ServletContext中，以便于获取
            servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);

            ClassLoader ccl = Thread.currentThread().getContextClassLoader();
            if (ccl == ContextLoader.class.getClassLoader()) {
                currentContext = this.context;
            }
            else if (ccl != null) {
                currentContextPerThread.put(ccl, this.context);
            }
            if (logger.isInfoEnabled()) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                logger.info("Root WebApplicationContext: initialization completed in " + elapsedTime + " ms");
            }
            return this.context;
        }
        catch (RuntimeException ex) {
            logger.error("Context initialization failed", ex);
            servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ex);
            throw ex;
        }
        catch (Error err) {
            logger.error("Context initialization failed", err);
            servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, err);
            throw err;
        }
    }

    protected WebApplicationContext createWebApplicationContext(ServletContext sc)
    {
        Class contextClass = determineContextClass(sc);
        if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass))
        {
            throw new ApplicationContextException("Custom context class [" + contextClass.getName() + "] is not of type ["
                    + ConfigurableWebApplicationContext.class.getName() + "]");
        }
        return (WebApplicationContext) BeanUtils.instantiateClass(contextClass);
    }


    protected Class<?> determineContextClass(ServletContext servletContext)
    {
        String contextClassName = servletContext.getInitParameter("contextClass");
        if (contextClassName != null) {
            try {
                return ClassUtils.forName(contextClassName, ClassUtils.getDefaultClassLoader());
            }
            catch (ClassNotFoundException ex) {
                throw new ApplicationContextException("Failed to load custom context class [" + contextClassName + "]", ex);
            }
        }
        contextClassName = defaultStrategies.getProperty(WebApplicationContext.class.getName());
        try {
            return ClassUtils.forName(contextClassName, ContextLoader.class.getClassLoader());
        } catch (ClassNotFoundException ex) {
            throw new ApplicationContextException("Failed to load default context class [" + contextClassName + "]", ex);
        }
    }

    public void closeWebApplicationContext(ServletContext servletContext)
    {
        servletContext.log("Closing Spring root WebApplicationContext");
        try {
            if ((this.context instanceof ConfigurableWebApplicationContext)) {
                ((ConfigurableWebApplicationContext)this.context).close();
            }

            ClassLoader ccl = Thread.currentThread().getContextClassLoader();
            if (ccl == ContextLoader.class.getClassLoader()) {
                currentContext = null;
            }
            else if (ccl != null) {
                currentContextPerThread.remove(ccl);
            }
            servletContext.removeAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            if (this.parentContextRef != null)
                this.parentContextRef.release();
        }
        finally
        {
            ClassLoader ccl = Thread.currentThread().getContextClassLoader();
            if (ccl == ContextLoader.class.getClassLoader()) {
                currentContext = null;
            }
            else if (ccl != null) {
                currentContextPerThread.remove(ccl);
            }
            servletContext.removeAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            if (this.parentContextRef != null)
                this.parentContextRef.release();
        }
    }

    protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac, ServletContext sc)
    {
        if (ObjectUtils.identityToString(wac).equals(wac.getId()))
        {
            String idParam = sc.getInitParameter("contextId");
            if (idParam != null) {
                wac.setId(idParam);
            }
            else
            {
                wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX +
                        ObjectUtils.getDisplayString(sc
                                .getContextPath()));
            }
        }
        wac.setServletContext(sc);
        String configLocationParam = sc.getInitParameter("contextConfigLocation");
        if (configLocationParam != null) {
            wac.setConfigLocation(configLocationParam);
        }
//        ConfigurableEnvironment env = wac.getEnvironment();
//        if ((env instanceof ConfigurableWebEnvironment)) {
//            ((ConfigurableWebEnvironment)env).initPropertySources(sc, null);
//        }
//        customizeContext(sc, wac);
        try {
            //refresh()方法触发容器初始化,注册bean之类的事情
            wac.refresh();
        }catch (Exception ee) {

        }
    }
}
