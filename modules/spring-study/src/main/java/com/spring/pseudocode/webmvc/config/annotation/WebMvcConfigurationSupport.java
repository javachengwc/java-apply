package com.spring.pseudocode.webmvc.config.annotation;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Source;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.spring.pseudocode.webmvc.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ClassUtils;
import org.springframework.util.PathMatcher;
import org.springframework.validation.Errors;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.method.support.CompositeUriComponentsContributor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.JsonViewRequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.JsonViewResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import org.springframework.web.servlet.resource.ResourceUrlProvider;
import org.springframework.web.servlet.resource.ResourceUrlProviderExposingInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.ViewResolverComposite;
import org.springframework.web.util.UrlPathHelper;

//web组件的初始化用
public class WebMvcConfigurationSupport implements ApplicationContextAware, ServletContextAware {

    private static boolean romePresent =
            ClassUtils.isPresent("com.rometools.rome.feed.WireFeed",WebMvcConfigurationSupport.class.getClassLoader());

    private static final boolean jaxb2Present =
            ClassUtils.isPresent("javax.xml.bind.Binder", WebMvcConfigurationSupport.class.getClassLoader());

    private static final boolean jackson2Present =
            ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper",WebMvcConfigurationSupport.class.getClassLoader()) &&
                    ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", WebMvcConfigurationSupport.class.getClassLoader());

    private static final boolean jackson2XmlPresent =
            ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper",WebMvcConfigurationSupport.class.getClassLoader());

    private static final boolean gsonPresent =
            ClassUtils.isPresent("com.google.gson.Gson", WebMvcConfigurationSupport.class.getClassLoader());

    private ApplicationContext applicationContext;

    private ServletContext servletContext;

    private List<Object> interceptors;

    private PathMatchConfigurer pathMatchConfigurer;

    private ContentNegotiationManager contentNegotiationManager;

    private List<HandlerMethodArgumentResolver> argumentResolvers;

    private List<HandlerMethodReturnValueHandler> returnValueHandlers;

    private List<HttpMessageConverter<?>> messageConverters;

    private Map<String, CorsConfiguration> corsConfigurations;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {
        return this.servletContext;
    }


    //Spring MVC处理请求映射bean
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping handlerMapping = createRequestMappingHandlerMapping();
        handlerMapping.setOrder(0);
        handlerMapping.setInterceptors(getInterceptors());
        handlerMapping.setContentNegotiationManager(mvcContentNegotiationManager());
        handlerMapping.setCorsConfigurations(getCorsConfigurations());

        PathMatchConfigurer configurer = getPathMatchConfigurer();
        if (configurer.isUseSuffixPatternMatch() != null) {
            handlerMapping.setUseSuffixPatternMatch(configurer.isUseSuffixPatternMatch());
        }
        if (configurer.isUseRegisteredSuffixPatternMatch() != null) {
            handlerMapping.setUseRegisteredSuffixPatternMatch(configurer.isUseRegisteredSuffixPatternMatch());
        }
        if (configurer.isUseTrailingSlashMatch() != null) {
            handlerMapping.setUseTrailingSlashMatch(configurer.isUseTrailingSlashMatch());
        }
        UrlPathHelper pathHelper = configurer.getUrlPathHelper();
        if (pathHelper != null) {
            handlerMapping.setUrlPathHelper(pathHelper);
        }
        PathMatcher pathMatcher = configurer.getPathMatcher();
        if (pathMatcher != null) {
            handlerMapping.setPathMatcher(pathMatcher);
        }

        return handlerMapping;
    }

    //处理请求映射扩展点
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    protected final Object[] getInterceptors() {
        if (this.interceptors == null) {
            org.springframework.web.servlet.config.annotation.InterceptorRegistry registry = new org.springframework.web.servlet.config.annotation.InterceptorRegistry();
            addInterceptors(registry);
            registry.addInterceptor(new ConversionServiceExposingInterceptor(mvcConversionService()));
            registry.addInterceptor(new ResourceUrlProviderExposingInterceptor(mvcResourceUrlProvider()));
            this.interceptors = registry.getInterceptors();
        }
        return this.interceptors.toArray();
    }

    protected void addInterceptors(InterceptorRegistry registry) {
    }

    protected PathMatchConfigurer getPathMatchConfigurer() {
        if (this.pathMatchConfigurer == null) {
            this.pathMatchConfigurer = new PathMatchConfigurer();
            configurePathMatch(this.pathMatchConfigurer);
        }
        return this.pathMatchConfigurer;
    }

    protected void configurePathMatch(PathMatchConfigurer configurer) {
    }

    @Bean
    public PathMatcher mvcPathMatcher() {
        PathMatcher pathMatcher = getPathMatchConfigurer().getPathMatcher();
        return (pathMatcher != null ? pathMatcher : new AntPathMatcher());
    }

    @Bean
    public UrlPathHelper mvcUrlPathHelper() {
        UrlPathHelper pathHelper = getPathMatchConfigurer().getUrlPathHelper();
        return (pathHelper != null ? pathHelper : new UrlPathHelper());
    }

    @Bean
    public ContentNegotiationManager mvcContentNegotiationManager() {
        if (this.contentNegotiationManager == null) {
            ContentNegotiationConfigurer configurer = new ContentNegotiationConfigurer(this.servletContext);
            configurer.mediaTypes(getDefaultMediaTypes());
            configureContentNegotiation(configurer);
            try {
                this.contentNegotiationManager = configurer.getContentNegotiationManager();
            }
            catch (Exception ex) {
                throw new BeanInitializationException("Could not create ContentNegotiationManager", ex);
            }
        }
        return this.contentNegotiationManager;
    }

    protected Map<String, MediaType> getDefaultMediaTypes() {
        Map<String, MediaType> map = new HashMap<String, MediaType>(4);
        if (romePresent) {
            map.put("atom", MediaType.APPLICATION_ATOM_XML);
            map.put("rss", MediaType.APPLICATION_RSS_XML);
        }
        if (jaxb2Present || jackson2XmlPresent) {
            map.put("xml", MediaType.APPLICATION_XML);
        }
        if (jackson2Present || gsonPresent) {
            map.put("json", MediaType.APPLICATION_JSON);
        }
        return map;
    }

    protected void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    }

    @Bean
    public HandlerMapping viewControllerHandlerMapping() {
        org.springframework.web.servlet.config.annotation.ViewControllerRegistry registry = new org.springframework.web.servlet.config.annotation.ViewControllerRegistry();
        registry.setApplicationContext(this.applicationContext);
        addViewControllers(registry);

        AbstractHandlerMapping handlerMapping = registry.getHandlerMapping();
        handlerMapping = (handlerMapping != null ? handlerMapping : new org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport.EmptyHandlerMapping());
        handlerMapping.setPathMatcher(mvcPathMatcher());
        handlerMapping.setUrlPathHelper(mvcUrlPathHelper());
        handlerMapping.setInterceptors(getInterceptors());
        handlerMapping.setCorsConfigurations(getCorsConfigurations());
        return handlerMapping;
    }

    protected void addViewControllers(ViewControllerRegistry registry) {
    }

    @Bean
    public BeanNameUrlHandlerMapping beanNameHandlerMapping() {
        BeanNameUrlHandlerMapping mapping = new BeanNameUrlHandlerMapping();
        mapping.setOrder(2);
        mapping.setInterceptors(getInterceptors());
        mapping.setCorsConfigurations(getCorsConfigurations());
        return mapping;
    }

    @Bean
    public HandlerMapping resourceHandlerMapping() {
        org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry = new org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry(this.applicationContext,
                this.servletContext, mvcContentNegotiationManager());
        addResourceHandlers(registry);

        AbstractHandlerMapping handlerMapping = registry.getHandlerMapping();
        if (handlerMapping != null) {
            handlerMapping.setPathMatcher(mvcPathMatcher());
            handlerMapping.setUrlPathHelper(mvcUrlPathHelper());
            handlerMapping.setInterceptors(new ResourceUrlProviderExposingInterceptor(mvcResourceUrlProvider()));
            handlerMapping.setCorsConfigurations(getCorsConfigurations());
        }
        else {
            handlerMapping = new org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport.EmptyHandlerMapping();
        }
        return handlerMapping;
    }

    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    }

    @Bean
    public ResourceUrlProvider mvcResourceUrlProvider() {
        ResourceUrlProvider urlProvider = new ResourceUrlProvider();
        UrlPathHelper pathHelper = getPathMatchConfigurer().getUrlPathHelper();
        if (pathHelper != null) {
            urlProvider.setUrlPathHelper(pathHelper);
        }
        PathMatcher pathMatcher = getPathMatchConfigurer().getPathMatcher();
        if (pathMatcher != null) {
            urlProvider.setPathMatcher(pathMatcher);
        }
        return urlProvider;
    }

    @Bean
    public HandlerMapping defaultServletHandlerMapping() {
        DefaultServletHandlerConfigurer configurer = new DefaultServletHandlerConfigurer(servletContext);
        configureDefaultServletHandling(configurer);
        AbstractHandlerMapping handlerMapping = configurer.getHandlerMapping();
        handlerMapping = handlerMapping != null ? handlerMapping : new org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport.EmptyHandlerMapping();
        return handlerMapping;
    }

    protected void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    }

    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter adapter = createRequestMappingHandlerAdapter();
        adapter.setContentNegotiationManager(mvcContentNegotiationManager());
        adapter.setMessageConverters(getMessageConverters());
        adapter.setWebBindingInitializer(getConfigurableWebBindingInitializer());
        adapter.setCustomArgumentResolvers(getArgumentResolvers());
        adapter.setCustomReturnValueHandlers(getReturnValueHandlers());

        if (jackson2Present) {
            adapter.setRequestBodyAdvice(
                    Collections.<RequestBodyAdvice>singletonList(new JsonViewRequestBodyAdvice()));
            adapter.setResponseBodyAdvice(
                    Collections.<ResponseBodyAdvice<?>>singletonList(new JsonViewResponseBodyAdvice()));
        }

        AsyncSupportConfigurer configurer = new AsyncSupportConfigurer();
        configureAsyncSupport(configurer);
        if (configurer.getTaskExecutor() != null) {
            adapter.setTaskExecutor(configurer.getTaskExecutor());
        }
        if (configurer.getTimeout() != null) {
            adapter.setAsyncRequestTimeout(configurer.getTimeout());
        }
        adapter.setCallableInterceptors(configurer.getCallableInterceptors());
        adapter.setDeferredResultInterceptors(configurer.getDeferredResultInterceptors());

        return adapter;
    }

    protected RequestMappingHandlerAdapter createRequestMappingHandlerAdapter() {
        return new RequestMappingHandlerAdapter();
    }

    protected ConfigurableWebBindingInitializer getConfigurableWebBindingInitializer() {
        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setConversionService(mvcConversionService());
        initializer.setValidator(mvcValidator());
        initializer.setMessageCodesResolver(getMessageCodesResolver());
        return initializer;
    }

    protected MessageCodesResolver getMessageCodesResolver() {
        return null;
    }

    protected void configureAsyncSupport(AsyncSupportConfigurer configurer) {
    }

    @Bean
    public FormattingConversionService mvcConversionService() {
        FormattingConversionService conversionService = new DefaultFormattingConversionService();
        addFormatters(conversionService);
        return conversionService;
    }

    protected void addFormatters(FormatterRegistry registry) {
    }

    @Bean
    public Validator mvcValidator() {
        Validator validator = getValidator();
        if (validator == null) {
            if (ClassUtils.isPresent("javax.validation.Validator", getClass().getClassLoader())) {
                Class<?> clazz;
                try {
                    String className = "org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean";
                    clazz = ClassUtils.forName(className, org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport.class.getClassLoader());
                }
                catch (ClassNotFoundException ex) {
                    throw new BeanInitializationException("Could not find default validator class", ex);
                }
                catch (LinkageError ex) {
                    throw new BeanInitializationException("Could not load default validator class", ex);
                }
                validator = (Validator) BeanUtils.instantiateClass(clazz);
            }
            else {
                validator = new org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport.NoOpValidator();
            }
        }
        return validator;
    }

    protected Validator getValidator() {
        return null;
    }

    protected final List<HandlerMethodArgumentResolver> getArgumentResolvers() {
        if (this.argumentResolvers == null) {
            this.argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
            addArgumentResolvers(this.argumentResolvers);
        }
        return this.argumentResolvers;
    }

    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    }

    protected final List<HandlerMethodReturnValueHandler> getReturnValueHandlers() {
        if (this.returnValueHandlers == null) {
            this.returnValueHandlers = new ArrayList<HandlerMethodReturnValueHandler>();
            addReturnValueHandlers(this.returnValueHandlers);
        }
        return this.returnValueHandlers;
    }

    protected void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
    }

    protected final List<HttpMessageConverter<?>> getMessageConverters() {
        if (this.messageConverters == null) {
            this.messageConverters = new ArrayList<HttpMessageConverter<?>>();
            configureMessageConverters(this.messageConverters);
            if (this.messageConverters.isEmpty()) {
                addDefaultHttpMessageConverters(this.messageConverters);
            }
            extendMessageConverters(this.messageConverters);
        }
        return this.messageConverters;
    }

    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    }

    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    }

    protected final void addDefaultHttpMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        stringConverter.setWriteAcceptCharset(false);

        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(stringConverter);
        messageConverters.add(new ResourceHttpMessageConverter());
        messageConverters.add(new SourceHttpMessageConverter<Source>());
        messageConverters.add(new AllEncompassingFormHttpMessageConverter());

        if (romePresent) {
            messageConverters.add(new AtomFeedHttpMessageConverter());
            messageConverters.add(new RssChannelHttpMessageConverter());
        }

        if (jackson2XmlPresent) {
            ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.xml().applicationContext(this.applicationContext).build();
            messageConverters.add(new MappingJackson2XmlHttpMessageConverter(objectMapper));
        }
        else if (jaxb2Present) {
            messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
        }

        if (jackson2Present) {
            ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().applicationContext(this.applicationContext).build();
            messageConverters.add(new MappingJackson2HttpMessageConverter(objectMapper));
        }
        else if (gsonPresent) {
            messageConverters.add(new GsonHttpMessageConverter());
        }
    }

    @Bean
    public CompositeUriComponentsContributor mvcUriComponentsContributor() {
        return new CompositeUriComponentsContributor(
                requestMappingHandlerAdapter().getArgumentResolvers(), mvcConversionService());
    }

    @Bean
    public HttpRequestHandlerAdapter httpRequestHandlerAdapter() {
        return new HttpRequestHandlerAdapter();
    }

    @Bean
    public SimpleControllerHandlerAdapter simpleControllerHandlerAdapter() {
        return new SimpleControllerHandlerAdapter();
    }

    @Bean
    public HandlerExceptionResolver handlerExceptionResolver() {
        List<HandlerExceptionResolver> exceptionResolvers = new ArrayList<HandlerExceptionResolver>();
        configureHandlerExceptionResolvers(exceptionResolvers);
        if (exceptionResolvers.isEmpty()) {
            addDefaultHandlerExceptionResolvers(exceptionResolvers);
        }
        extendHandlerExceptionResolvers(exceptionResolvers);
        HandlerExceptionResolverComposite composite = new HandlerExceptionResolverComposite();
        composite.setOrder(0);
        composite.setExceptionResolvers(exceptionResolvers);
        return composite;
    }

    protected void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
    }

    protected void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
    }

    protected final void addDefaultHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        ExceptionHandlerExceptionResolver exceptionHandlerResolver = createExceptionHandlerExceptionResolver();
        exceptionHandlerResolver.setContentNegotiationManager(mvcContentNegotiationManager());
        exceptionHandlerResolver.setMessageConverters(getMessageConverters());
        exceptionHandlerResolver.setCustomArgumentResolvers(getArgumentResolvers());
        exceptionHandlerResolver.setCustomReturnValueHandlers(getReturnValueHandlers());
        if (jackson2Present) {
            exceptionHandlerResolver.setResponseBodyAdvice(
                    Collections.<ResponseBodyAdvice<?>>singletonList(new JsonViewResponseBodyAdvice()));
        }
        exceptionHandlerResolver.setApplicationContext(this.applicationContext);
        exceptionHandlerResolver.afterPropertiesSet();
        exceptionResolvers.add(exceptionHandlerResolver);

        ResponseStatusExceptionResolver responseStatusResolver = new ResponseStatusExceptionResolver();
        responseStatusResolver.setMessageSource(this.applicationContext);
        exceptionResolvers.add(responseStatusResolver);

        exceptionResolvers.add(new DefaultHandlerExceptionResolver());
    }

    protected ExceptionHandlerExceptionResolver createExceptionHandlerExceptionResolver() {
        return new ExceptionHandlerExceptionResolver();
    }

    @Bean
    public ViewResolver mvcViewResolver() {
        org.springframework.web.servlet.config.annotation.ViewResolverRegistry registry = new org.springframework.web.servlet.config.annotation.ViewResolverRegistry();
        registry.setContentNegotiationManager(mvcContentNegotiationManager());
        registry.setApplicationContext(this.applicationContext);
        configureViewResolvers(registry);

        if (registry.getViewResolvers().isEmpty()) {
            String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
                    this.applicationContext, ViewResolver.class, true, false);
            if (names.length == 1) {
                registry.getViewResolvers().add(new InternalResourceViewResolver());
            }
        }

        ViewResolverComposite composite = new ViewResolverComposite();
        composite.setOrder(registry.getOrder());
        composite.setViewResolvers(registry.getViewResolvers());
        composite.setApplicationContext(this.applicationContext);
        composite.setServletContext(this.servletContext);
        return composite;
    }

    protected void configureViewResolvers(ViewResolverRegistry registry) {
    }

    protected final Map<String, CorsConfiguration> getCorsConfigurations() {
        if (this.corsConfigurations == null) {
            org.springframework.web.servlet.config.annotation.CorsRegistry registry = new org.springframework.web.servlet.config.annotation.CorsRegistry();
            addCorsMappings(registry);
            this.corsConfigurations = registry.getCorsConfigurations();
        }
        return this.corsConfigurations;
    }

    protected void addCorsMappings(CorsRegistry registry) {
    }


    private static final class EmptyHandlerMapping extends AbstractHandlerMapping {

        @Override
        protected Object getHandlerInternal(HttpServletRequest request) {
            return null;
        }
    }


    private static final class NoOpValidator implements Validator {

        @Override
        public boolean supports(Class<?> clazz) {
            return false;
        }

        @Override
        public void validate(Object target, Errors errors) {
        }
    }

}
