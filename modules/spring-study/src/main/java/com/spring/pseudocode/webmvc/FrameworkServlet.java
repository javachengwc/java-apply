package com.spring.pseudocode.webmvc;


import com.spring.pseudocode.context.context.ApplicationContext;
import com.spring.pseudocode.context.context.ApplicationContextAware;
import com.spring.pseudocode.context.context.ConfigurableApplicationContext;
import com.spring.pseudocode.core.core.env.ConfigurableEnvironment;
import com.spring.pseudocode.web.web.context.ConfigurableWebApplicationContext;
import com.spring.pseudocode.web.web.context.WebApplicationContext;
import com.spring.pseudocode.web.web.context.request.RequestAttributes;
import com.spring.pseudocode.web.web.context.request.RequestContextHolder;
import com.spring.pseudocode.web.web.context.request.ServletRequestAttributes;
import com.spring.pseudocode.web.web.context.support.XmlWebApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.http.HttpMethod;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.ConfigurableWebEnvironment;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class FrameworkServlet extends HttpServletBean implements ApplicationContextAware {

    private static Logger logger= LoggerFactory.getLogger(FrameworkServlet.class);

    private boolean threadContextInheritable = false;

    private String contextAttribute;
    private Class<?> contextClass =  XmlWebApplicationContext.class;
    private String contextId;
    private String contextConfigLocation;
    private String contextInitializerClasses;

    //这里的webApplicationContext就是springmvc容器
    //springmvc容器只负责controller层相关的bean的管理，而它的父容器,也就是spring容器负责service层，dao层等其他相关bean的管理
    private WebApplicationContext webApplicationContext;

    private boolean webApplicationContextInjected = false;


    private boolean publishContext = true;
    private boolean publishEvents = true;

    private String namespace;

    private boolean refreshEventReceived = false;

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


    //initServletBean()进行Web上下文初始化，spring mvc容器初始化，提供给子类初始化扩展点；
    protected final void initServletBean()
    {
        long startTime = System.currentTimeMillis();
        try
        {
            //initWebApplicationContext()方法进行springmvc容器初始化
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
                        cwac.setParent(rootContext);
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
            // 如果 WebApplicationContext 仍为空，则以 Spring 的容器为父上下文建立一个新的springmvc容器。
            wac = createWebApplicationContext(rootContext);
        }

        if (!this.refreshEventReceived)
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
       //((ConfigurableApplicationContext)wac).refresh();
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
//
//        LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
//        LocaleContext localeContext = buildLocaleContext(request);
        LocaleContext localeContext = null;

        RequestAttributes previousAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes requestAttributes = buildRequestAttributes(request, response, previousAttributes);
//
//        WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
//        asyncManager.registerCallableInterceptor(FrameworkServlet.class.getName(), new RequestBindingInterceptor(null));

        //绑定request到当前线程
        initContextHolders(request, localeContext, requestAttributes);

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

    protected ServletRequestAttributes buildRequestAttributes(HttpServletRequest request,
              HttpServletResponse response, RequestAttributes previousAttributes)
    {
        if ((previousAttributes == null) || ((previousAttributes instanceof ServletRequestAttributes))) {
            return new ServletRequestAttributes(request, response);
        }

        return null;
    }

    //绑定request到当前线程
    private void initContextHolders(HttpServletRequest request, LocaleContext localeContext, RequestAttributes requestAttributes)
    {
//        if (localeContext != null) {
//            LocaleContextHolder.setLocaleContext(localeContext, this.threadContextInheritable);
//        }
        if (requestAttributes != null) {
            RequestContextHolder.setRequestAttributes(requestAttributes, this.threadContextInheritable);
        }
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Bound request context to thread: " + request);
        }
    }

    //以 Spring 的容器为父上下文建立一个新的springmvc容器
    protected WebApplicationContext createWebApplicationContext(ApplicationContext parent)
    {
        //Class contextClass = getContextClass();
        Class contextClass=null;
        if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass))
        {
            throw new ApplicationContextException("Fatal initialization error in servlet with name '" +
                    getServletName() + "': custom WebApplicationContext class [" + contextClass
                    .getName() + "] is not of type ConfigurableWebApplicationContext");
        }

        ConfigurableWebApplicationContext wac = (ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);
        //wac.setEnvironment(getEnvironment());
        wac.setParent(parent);
        //wac.setConfigLocation(getContextConfigLocation());
        configureAndRefreshWebApplicationContext(wac);
        return wac;
    }

    protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac) {
        if (ObjectUtils.identityToString(wac).equals(wac.getId()))
        {
            if (this.contextId != null) {
                wac.setId(this.contextId);
            }
            else
            {
                wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX +
                        ObjectUtils.getDisplayString(getServletContext().getContextPath()) + "/" + getServletName());
            }
        }
        wac.setServletContext(getServletContext());
        wac.setServletConfig(getServletConfig());
        //wac.setNamespace(getNamespace());
        //wac.addApplicationListener(new SourceFilteringListener(wac, new ContextRefreshListener(null)));

        ConfigurableEnvironment env = wac.getEnvironment();
        if ((env instanceof ConfigurableWebEnvironment)) {
            ((ConfigurableWebEnvironment)env).initPropertySources(getServletContext(), getServletConfig());
        }
        //postProcessWebApplicationContext(wac);
        //applyInitializers(wac);
        //wac.refresh();
    }
}
