package com.spring.pseudocode.web.web.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

//springweb与spring整合的入口
public class ContextLoaderListener extends ContextLoader implements ServletContextListener
{
    public ContextLoaderListener()
    {
    }

    public ContextLoaderListener(WebApplicationContext context)
    {
        super(context);
    }

    //初始servletContext后，初始webApplicationContext
    public void contextInitialized(ServletContextEvent event)
    {
        initWebApplicationContext(event.getServletContext());
    }

    public void contextDestroyed(ServletContextEvent event)
    {
        closeWebApplicationContext(event.getServletContext());
        //ContextCleanupListener.cleanupAttributes(event.getServletContext());
    }
}
