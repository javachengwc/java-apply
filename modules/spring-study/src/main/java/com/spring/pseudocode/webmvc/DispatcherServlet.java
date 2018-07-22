package com.spring.pseudocode.webmvc;

import com.spring.pseudocode.context.ApplicationContext;
import com.spring.pseudocode.web.context.WebApplicationContext;
import com.spring.pseudocode.web.multipart.MultipartResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class DispatcherServlet extends FrameworkServlet
{

    private static Logger logger= LoggerFactory.getLogger(DispatcherServlet.class);

    //默认的配置信息(比如ViewResolver,HandlerMapping,异常处理器等);
    private static final Properties defaultStrategies;


    private MultipartResolver multipartResolver;

    private LocaleResolver localeResolver;

    private ThemeResolver themeResolver;

    private List<HandlerMapping> handlerMappings;            //HandlerMapping列表

    private List<HandlerAdapter> handlerAdapters;

    private List<HandlerExceptionResolver> handlerExceptionResolvers;

    private List<ViewResolver> viewResolvers;                //ViewResolver列表

    private boolean detectAllHandlerMappings = true;

    public DispatcherServlet()
    {

    }

    public DispatcherServlet(WebApplicationContext webApplicationContext)
    {
        super(webApplicationContext);
    }


    static
    {
        //初始加载默认配置信息
        try
        {
            //加载jar包中与此类同目录下的配置文件DispatcherServlet.properties
            ClassPathResource resource = new ClassPathResource("DispatcherServlet.properties", DispatcherServlet.class);
            defaultStrategies = PropertiesLoaderUtils.loadProperties(resource);
        }
        catch (IOException ex) {
            throw new IllegalStateException("Could not load 'DispatcherServlet.properties': " + ex.getMessage());
        }
    }


    //刷新上下文,执行容器的一些初始化
    protected void onRefresh(ApplicationContext context)
    {
        initStrategies(context);
    }

    //初始化
    protected void initStrategies(ApplicationContext context)
    {
        initMultipartResolver(context);
        initLocaleResolver(context);
        initThemeResolver(context);
        initHandlerMappings(context);
        //initHandlerAdapters(context);
        initHandlerExceptionResolvers(context);
        //initRequestToViewNameTranslator(context);
        initViewResolvers(context);
        //initFlashMapManager(context);
    }

    //初始化多文件处理器
    private void initMultipartResolver(ApplicationContext context)
    {
        try
        {
            this.multipartResolver = ((MultipartResolver)context.getBean("multipartResolver", MultipartResolver.class));
        }
        catch (NoSuchBeanDefinitionException ex)
        {
            this.multipartResolver = null;
        }
    }

    private void initLocaleResolver(ApplicationContext context)
    {
        try
        {
            this.localeResolver = ((LocaleResolver)context.getBean("localeResolver", LocaleResolver.class));
        }
        catch (NoSuchBeanDefinitionException ex)
        {
            this.localeResolver = ((LocaleResolver)getDefaultStrategy(context, LocaleResolver.class));
            if (this.logger.isDebugEnabled())
                this.logger.debug("Unable to locate LocaleResolver with name 'localeResolver': using default [" + this.localeResolver + "]");
        }
    }

    private void initThemeResolver(ApplicationContext context)
    {
        try
        {
            this.themeResolver = ((ThemeResolver)context.getBean("themeResolver", ThemeResolver.class));
        }
        catch (NoSuchBeanDefinitionException ex)
        {
            this.themeResolver = ((ThemeResolver)getDefaultStrategy(context, ThemeResolver.class));
            if (this.logger.isDebugEnabled())
                this.logger.debug("Unable to locate ThemeResolver with name 'themeResolver': using default [" + this.themeResolver + "]");
        }
    }

    //初始化HandlerMappings
    private void initHandlerMappings(ApplicationContext context)
    {
        this.handlerMappings = null;
        if (this.detectAllHandlerMappings)
        {
            // 从 SpringMVC 的 IOC 容器及 Spring 的 IOC 容器中查找 HandlerMapping 实例
            //Map<String, HandlerMapping> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerMapping.class, true, false);
            Map<String, HandlerMapping> matchingBeans =null;
            if (!matchingBeans.isEmpty()) {
                this.handlerMappings = new ArrayList<HandlerMapping>(matchingBeans.values());
                // 按一定顺序放置 HandlerMapping 对象
                //OrderComparator.sort(this.handlerMappings);
            }
        } else {
            try {
                HandlerMapping hm = (HandlerMapping)context.getBean("handlerMapping", HandlerMapping.class);
                this.handlerMappings = Collections.singletonList(hm);
            }
            catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException)
            {
            }
        }
        // 如果没有 HandlerMapping，则加载默认的
        if (this.handlerMappings == null) {
            this.handlerMappings = getDefaultStrategies(context, HandlerMapping.class);
            if (this.logger.isDebugEnabled())
                this.logger.debug("No HandlerMappings found in servlet '" + getServletName() + "': using default");
        }
    }

    //初始化处理异常解决器Resolver
    private void initHandlerExceptionResolvers(ApplicationContext context)
    {
        this.handlerExceptionResolvers = null;
        //....................
        try {
            HandlerExceptionResolver her = (HandlerExceptionResolver) context
                    .getBean("handlerExceptionResolver", HandlerExceptionResolver.class);

            this.handlerExceptionResolvers = Collections.singletonList(her);
        } catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException) {
        }
        if (this.handlerExceptionResolvers == null) {
            this.handlerExceptionResolvers = getDefaultStrategies(context, HandlerExceptionResolver.class);
            if (this.logger.isDebugEnabled())
                this.logger.debug("No HandlerExceptionResolvers found in servlet '" + getServletName() + "': using default");
        }
    }


    //初始化示图渲染器
    private void initViewResolvers(ApplicationContext context)
    {
        this.viewResolvers = null;
        //....................
        try {
            ViewResolver vr = (ViewResolver)context.getBean("viewResolver", ViewResolver.class);
            this.viewResolvers = Collections.singletonList(vr);
        }
        catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException)
        {
        }
        if (this.viewResolvers == null) {
            this.viewResolvers = getDefaultStrategies(context, ViewResolver.class);
            if (this.logger.isDebugEnabled())
                this.logger.debug("No ViewResolvers found in servlet '" + getServletName() + "': using default");
        }
    }


    protected <T> T getDefaultStrategy(ApplicationContext context, Class<T> strategyInterface)
    {
        List<T> strategies = getDefaultStrategies(context, strategyInterface);
        if (strategies.size() != 1)
        {
            throw new RuntimeException("DispatcherServlet needs exactly 1 strategy for interface [" + strategyInterface
                    .getName() + "]");
        }
        return strategies.get(0);
    }

    //获取默认的配置
    protected <T> List<T> getDefaultStrategies(ApplicationContext context, Class<T> strategyInterface)
    {

        String key = strategyInterface.getName();
        String value = defaultStrategies.getProperty(key);
        if (value != null) {
            String[] classNames = StringUtils.commaDelimitedListToStringArray(value);
            List strategies = new ArrayList(classNames.length);
            for (String className : classNames) {
                try {
                    Class clazz = ClassUtils.forName(className, DispatcherServlet.class.getClassLoader());
                    //Object strategy = createDefaultStrategy(context, clazz);
                    Object strategy=null;
                    strategies.add(strategy);
                }
                catch (ClassNotFoundException ex) {
                    throw new BeanInitializationException("Could not find DispatcherServlet's default strategy class [" + className + "] for interface [" + key + "]", ex);
                }
                catch (LinkageError err)
                {
                    throw new BeanInitializationException("Error loading DispatcherServlet's default strategy class [" + className + "] for interface [" + key + "]: problem with class file or dependent class", err);
                }

            }

            return strategies;
        }

        return new LinkedList();
    }

    //处理请求入口
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception
    {

//        request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebApplicationContext());
//        request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, this.localeResolver);
//        request.setAttribute(THEME_RESOLVER_ATTRIBUTE, this.themeResolver);

        try
        {
            doDispatch(request, response);
        }
        finally {

        }
    }

    //处理请求(从分派到过程控制到结果返回全过程)
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        HttpServletRequest processedRequest = request;
        HandlerExecutionChain mappedHandler = null;
        boolean multipartRequestParsed = false;

        try
        {
            ModelAndView mv = null;
            Exception dispatchException = null;
            try
            {
                //processedRequest = checkMultipart(request);
                multipartRequestParsed = processedRequest != request;

                //获取mappedHandler
                mappedHandler = getHandler(processedRequest);
                //获取handlerAdapter
                HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

                String method = request.getMethod();
                //.........
                //调用拦截器链的preHandle方法
                if (!mappedHandler.applyPreHandle(processedRequest, response))
                {
                    return;
                }

                //处理请求返回ModelAndView
                mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

                //applyDefaultViewName(processedRequest, mv);
                //调用拦截器链的postHandle方法
                mappedHandler.applyPostHandle(processedRequest, response, mv);
            }
            catch (Exception ex) {
                dispatchException = ex;
            }
            processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
        }
        catch (Exception ex) {
            triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
        }
        catch (Error err) {
            //triggerAfterCompletionWithError(processedRequest, response, mappedHandler, err);
        }
        finally {
        }
    }

    //处理请求结果,将controller方法返回的结果经过示图渲染器渲染返回给客户端
    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
                                       HandlerExecutionChain mappedHandler, ModelAndView mv, Exception exception) throws Exception
    {
        boolean errorView = false;

//        if (exception != null) {
//            if ((exception instanceof ModelAndViewDefiningException)) {
//                this.logger.debug("ModelAndViewDefiningException encountered", exception);
//                mv = ((ModelAndViewDefiningException)exception).getModelAndView();
//            }
        if(false) {

        }
        else {
            Object handler = mappedHandler != null ? mappedHandler.getHandler() : null;
            mv = processHandlerException(request, response, handler, exception);
            errorView = mv != null;
        }


        if ((mv != null) && (!mv.wasCleared())) {
            //渲染页面
            render(mv, request, response);
            if (errorView) {
                //WebUtils.clearErrorRequestAttributes(request);
            }

        }
        if (mappedHandler != null) {
            //调用拦截器的afterCompletion方法，在这之前是已经渲染了页面
            mappedHandler.triggerAfterCompletion(request, response, null);
        }
    }

    //根据请求在handleMapping链中获取对应的HandlerExecutionChain
    protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception
    {
        for (HandlerMapping hm : this.handlerMappings) {
            HandlerExecutionChain handler = hm.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }

    //根据handler获取相应的handlerAdapter
    protected HandlerAdapter getHandlerAdapter(Object handler) throws ServletException
    {
        for (HandlerAdapter ha : this.handlerAdapters) {
            if (ha.supports(handler)) {
                return ha;
            }
        }
        throw new ServletException("No adapter for handler [" + handler + "]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
    }

    //处理请求异常
    protected ModelAndView processHandlerException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception
    {
        ModelAndView exMv = null;
        for (HandlerExceptionResolver handlerExceptionResolver : this.handlerExceptionResolvers) {
            exMv = handlerExceptionResolver.resolveException(request, response, handler, ex);
            if (exMv != null) {
                break;
            }
        }
        if (exMv != null) {
            if (exMv.isEmpty()) {
                //request.setAttribute(EXCEPTION_ATTRIBUTE, ex);
                return null;
            }
//            if (!exMv.hasView()) {
//                exMv.setViewName(getDefaultViewName(request));
//            }
//            WebUtils.exposeErrorRequestAttributes(request, ex, getServletName());
            return exMv;
        }

        throw ex;
    }

    //渲染页面
    protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        Locale locale = this.localeResolver.resolveLocale(request);
        response.setLocale(locale);
        View view=null;
        if (mv.isReference())
        {
            view = resolveViewName(mv.getViewName(), mv.getModelInternal(), locale, request);
            if (view == null)
            {
                throw new ServletException("Could not resolve view with name '" + mv.getViewName() + "' in servlet with name '" +
                        getServletName() + "'");
            }
        }
        else
        {
            view = (View)mv.getView();
            if (view == null)
            {
                throw new ServletException("ModelAndView [" + mv + "] neither contains a view name nor a " + "View object in servlet with name '" +
                        getServletName() + "'");
            }

        }
        try
        {
            //渲染页面
            view.render(mv.getModelInternal(), request, response);
        }
        catch (Exception ex) {
            throw ex;
        }
    }


    protected View resolveViewName(String viewName, Map<String, Object> model, Locale locale, HttpServletRequest request) throws Exception
    {
        for (ViewResolver viewResolver : this.viewResolvers) {
            View view = viewResolver.resolveViewName(viewName, locale);
            if (view != null) {
                return view;
            }
        }
        return null;
    }

    //触发调用拦截器的afterCompletion方法
    private void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, HandlerExecutionChain mappedHandler, Exception ex)
            throws Exception
    {
        if (mappedHandler != null) {
            mappedHandler.triggerAfterCompletion(request, response, ex);
        }
        throw ex;
    }

}