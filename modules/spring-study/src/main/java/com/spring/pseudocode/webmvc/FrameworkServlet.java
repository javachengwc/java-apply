package com.spring.pseudocode.webmvc;


import com.spring.pseudocode.context.context.ApplicationContext;
import com.spring.pseudocode.context.context.ApplicationContextAware;
import com.spring.pseudocode.web.web.context.WebApplicationContext;
import com.spring.pseudocode.web.web.context.XmlWebApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class FrameworkServlet extends HttpServletBean implements ApplicationContextAware {

    private static Logger logger= LoggerFactory.getLogger(FrameworkServlet.class);

    private String contextAttribute;
    private Class<?> contextClass =  XmlWebApplicationContext.class;
    private String contextId;
    private String contextConfigLocation;
    private String contextInitializerClasses;

    private WebApplicationContext webApplicationContext;
    private boolean webApplicationContextInjected = false;


    private boolean publishContext = true;
    private boolean publishEvents = true;

    private String namespace;


    public FrameworkServlet()
    {

    }

    public FrameworkServlet(WebApplicationContext webApplicationContext)
    {
        this.webApplicationContext = webApplicationContext;
    }

    public final WebApplicationContext getWebApplicationContext()
    {
        return this.webApplicationContext;
    }


    public void setContextAttribute(String contextAttribute)
    {
        this.contextAttribute = contextAttribute;
    }

    public void setApplicationContext(ApplicationContext applicationContext)
    {
        if ((this.webApplicationContext == null) && ((applicationContext instanceof WebApplicationContext))) {
            this.webApplicationContext = ((WebApplicationContext)applicationContext);
            this.webApplicationContextInjected = true;
        }
    }


    //initServletBean()进行Web上下文初始化，初始化web上下文，提供给子类初始化扩展点；
    protected final void initServletBean()
    {
        long startTime = System.currentTimeMillis();
        try
        {
            this.webApplicationContext = initWebApplicationContext();
            initFrameworkServlet();
        }
        catch (Exception ex) {
            this.logger.error("Context initialization failed", ex);

        }
    }

    protected WebApplicationContext initWebApplicationContext()
    {
        //获取 ContextLoaderListener 初始化并注册在 ServletContext 中的根容器，即 Spring 的容器
        //WebApplicationContext rootContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        WebApplicationContext rootContext =null;
                WebApplicationContext wac = null;

        if (this.webApplicationContext != null)
        {
            // 因为 WebApplicationContext 不为空，说明该类在构造时已经将其注入
            wac = this.webApplicationContext;
            if ((wac instanceof ConfigurableWebApplicationContext)) {
                ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext)wac;
                if (!cwac.isActive())
                {
                    if (cwac.getParent() == null)
                    {
                        //将 Spring 的容器设为springmvc容器的父容器
                        //cwac.setParent(rootContext);
                    }
                }
            }
        }
        if (wac == null)
        {
            // 如果 WebApplicationContext 为空，则进行查找，能找到说明上下文已经在别处初始化。
            //wac = findWebApplicationContext();
        }
        if (wac == null)
        {
            // 如果 WebApplicationContext 仍为空，则以 Spring 的容器为父上下文建立一个新的。
            //wac = createWebApplicationContext(rootContext);
        }

        //if (!this.refreshEventReceived)
        {
            //刷新上下文,执行容器的一些初始化，此方法由子类实现，来进行扩展。
            onRefresh(wac);
        }

        return wac;
    }


    //提供给子类的扩展方法
    protected void initFrameworkServlet() {
    }


    public void refresh()
    {
        WebApplicationContext wac = getWebApplicationContext();
        if (!(wac instanceof ConfigurableApplicationContext)) {
            throw new IllegalStateException("WebApplicationContext does not support refresh: " + wac);
        }
        ((ConfigurableApplicationContext)wac).refresh();
    }


    //处理get请求的方法
    protected final void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    //处理post请求的方法
    protected final void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }


    //处理请求
    protected final void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        long startTime = System.currentTimeMillis();
        Throwable failureCause = null;

        try
        {
            doService(request, response);
        }
        catch (ServletException ex) {
            failureCause = ex;
            throw ex;
        }
        catch (IOException ex) {
            failureCause = ex;
            throw ex;
        }
        catch (Throwable ex) {
            failureCause = ex;
            throw new RuntimeException("Request processing failed", ex);
        }
        finally
        {
            //resetContextHolders(request, previousLocaleContext, previousAttributes)；
            //publishRequestHandledEvent(request, response, startTime, failureCause);
        }
    }

    //由子类DispatcherServlet实现请求的处理
    protected abstract void doService(HttpServletRequest request, HttpServletResponse response) throws Exception;

    protected void onRefresh(ApplicationContext context) {

    }

    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        if (HttpMethod.PATCH.matches(request.getMethod())) {
            processRequest(request, response);
        }
        else
            super.service(request, response);
    }

    public void destroy()
    {
        getServletContext().log("Destroying Spring FrameworkServlet '" + getServletName() + "'");

        ((ConfigurableApplicationContext)this.webApplicationContext).close();
    }


}
