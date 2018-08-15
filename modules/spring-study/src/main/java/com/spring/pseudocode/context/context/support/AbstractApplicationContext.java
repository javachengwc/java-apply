package com.spring.pseudocode.context.context.support;

import com.spring.pseudocode.beans.BeansException;
import com.spring.pseudocode.beans.factory.BeanFactory;
import com.spring.pseudocode.context.context.ApplicationContext;
import com.spring.pseudocode.context.context.ApplicationListener;
import com.spring.pseudocode.context.context.ConfigurableApplicationContext;
import com.spring.pseudocode.core.core.env.ConfigurableEnvironment;
import com.spring.pseudocode.core.core.io.ResourceLoader;
import com.spring.pseudocode.core.core.io.support.ResourcePatternResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.*;
import org.springframework.context.event.*;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.context.support.DefaultLifecycleProcessor;
import org.springframework.context.weaving.LoadTimeWeaverAwareProcessor;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.ReflectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractApplicationContext extends DefaultResourceLoader
        implements ConfigurableApplicationContext, DisposableBean
{
    protected final Log logger = LogFactory.getLog(getClass());

    private ConfigurableEnvironment environment;

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<BeanFactoryPostProcessor>();

    private ResourcePatternResolver resourcePatternResolver;

    private LifecycleProcessor lifecycleProcessor;

    private MessageSource messageSource;

    private ApplicationEventMulticaster applicationEventMulticaster;

    private final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<ApplicationListener<?>>();

    private Set<ApplicationEvent> earlyApplicationEvents;

    private Thread shutdownHook;
    private final Object startupShutdownMonitor = new Object();
    private long startupDate;
    private final AtomicBoolean active = new AtomicBoolean();
    private final AtomicBoolean closed = new AtomicBoolean();

    public AbstractApplicationContext()
    {
        this.resourcePatternResolver = getResourcePatternResolver();
    }

    public AbstractApplicationContext(ApplicationContext parent)
    {
        this();
        setParent(parent);
    }

    protected ResourcePatternResolver getResourcePatternResolver()
    {
        //....
        return null;
    }

    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors()
    {
        return this.beanFactoryPostProcessors;
    }

    //广播事件
    public void publishEvent(ApplicationEvent event)
    {
        publishEvent(event, null);
    }

    //广播事件
    public void publishEvent(Object event)
    {
        publishEvent(event, null);
    }

    //广播事件
    protected void publishEvent(Object event, ResolvableType eventType)
    {
        //......
    }

    public void refresh() throws Exception
    {
        synchronized (this.startupShutdownMonitor)
        {
            //准备刷新,给容器设置同步标识
            prepareRefresh();

            //告诉子类启动refreshBeanFactory()方法，Bean定义资源文件的载入从子类的refreshBeanFactory()方法启动
            ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

            //为BeanFactory配置容器特性，例如类加载器、事件处理器等
            prepareBeanFactory(beanFactory);
            try
            {
                //为容器的某些子类指定特殊的BeanPost事件处理器
                postProcessBeanFactory(beanFactory);

                //调用所有注册的BeanFactoryPostProcessor的Bean
                invokeBeanFactoryPostProcessors(beanFactory);

                //为BeanFactory注册BeanPost事件处理器.
                //BeanPostProcessor是Bean后置处理器，用于监听容器触发的事件
                registerBeanPostProcessors(beanFactory);

                //初始化信息源，和国际化相关
                initMessageSource();

                //初始化容器事件传播器
                initApplicationEventMulticaster();

                //调用子类的某些特殊Bean初始化方法
                onRefresh();

                //为事件传播器注册事件监听器
                registerListeners();

                //初始化所有剩余的单态Bean
                finishBeanFactoryInitialization(beanFactory);

                //初始化容器的生命周期事件处理器，并发布容器的生命周期事件
                finishRefresh();
            }
            catch (BeansException ex)
            {
                //销毁以创建的单态Bean
                destroyBeans();
                //取消refresh操作，重置容器的同步标识
                cancelRefresh(ex);
                throw ex;
            }
            finally
            {
                resetCommonCaches();
            }
        }
    }

    //准备刷新
    protected void prepareRefresh()
    {
        this.startupDate = System.currentTimeMillis();
        this.closed.set(false);
        this.active.set(true);
        initPropertySources();
        this.earlyApplicationEvents = new LinkedHashSet<ApplicationEvent>();
    }

    protected void initPropertySources()
    {

    }

    protected ConfigurableListableBeanFactory obtainFreshBeanFactory()
    {
        //调用子类的refreshBeanFactory方法，其实就是创建BeanFactory，包含很多操作，包括xml文件的解析，bean注入，以及beanFactory的生成
        refreshBeanFactory();
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        return beanFactory;
    }

    protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory)
    {
        beanFactory.setBeanClassLoader(getClassLoader());
        beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));
//        beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));

        //beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
        beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
        beanFactory.ignoreDependencyInterface(EmbeddedValueResolverAware.class);
        beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
        beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
        beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
        beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);

        beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
        beanFactory.registerResolvableDependency(ResourceLoader.class, this);
        beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
        beanFactory.registerResolvableDependency(ApplicationContext.class, this);

        //beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));

        if (beanFactory.containsBean("loadTimeWeaver")) {
            beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
           // beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
        }
        if (!beanFactory.containsLocalBean("environment")) {
            beanFactory.registerSingleton("environment", getEnvironment());
        }
        if (!beanFactory.containsLocalBean("systemProperties")) {
            beanFactory.registerSingleton("systemProperties", getEnvironment().getSystemProperties());
        }
        if (!beanFactory.containsLocalBean("systemEnvironment"))
            beanFactory.registerSingleton("systemEnvironment", getEnvironment().getSystemEnvironment());
    }

    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
    {
    }

    protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory)
    {
        //....
    }

    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory)
    {
       //....
    }

    protected void initMessageSource()
    {
       //....
    }

    protected void initApplicationEventMulticaster()
    {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        if (beanFactory.containsLocalBean("applicationEventMulticaster")) {
            this.applicationEventMulticaster =((ApplicationEventMulticaster)beanFactory.getBean("applicationEventMulticaster", ApplicationEventMulticaster.class));
        }
        else
        {
            this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
            beanFactory.registerSingleton("applicationEventMulticaster", this.applicationEventMulticaster);
        }
    }

    protected void initLifecycleProcessor()
    {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        if (beanFactory.containsLocalBean("lifecycleProcessor")) {
            this.lifecycleProcessor =((LifecycleProcessor)beanFactory.getBean("lifecycleProcessor", LifecycleProcessor.class));
        }
        else
        {
            DefaultLifecycleProcessor defaultProcessor = new DefaultLifecycleProcessor();
            defaultProcessor.setBeanFactory(beanFactory);
            this.lifecycleProcessor = defaultProcessor;
            beanFactory.registerSingleton("lifecycleProcessor", this.lifecycleProcessor);
        }
    }

    protected void onRefresh() throws Exception
    {

    }

    protected void registerListeners()
    {
        //....
    }

    protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory)
    {
        //....
    }

    protected void finishRefresh()
    {
        //....
    }

    protected void cancelRefresh(BeansException ex)
    {
        this.active.set(false);
    }

    protected void resetCommonCaches()
    {
        ReflectionUtils.clearCache();
        ResolvableType.clearCache();
        CachedIntrospectionResults.clearClassLoader(getClassLoader());
    }

    public void registerShutdownHook()
    {
        if (this.shutdownHook == null)
        {
            this.shutdownHook = new Thread()
            {
                public void run() {
                    synchronized (AbstractApplicationContext.this.startupShutdownMonitor) {
                        AbstractApplicationContext.this.doClose();
                    }
                }
            };
            Runtime.getRuntime().addShutdownHook(this.shutdownHook);
        }
    }

    public void destroy()
    {
        close();
    }

    public void close()
    {
        synchronized (this.startupShutdownMonitor) {
            doClose();

            if (this.shutdownHook != null)
                try {
                    Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
                }
                catch (IllegalStateException localIllegalStateException)
                {
                }
        }
    }

    protected void doClose()
    {
        //....
    }

    protected void destroyBeans()
    {
        getBeanFactory().destroySingletons();
    }


    protected abstract void refreshBeanFactory() throws BeansException, IllegalStateException;

    protected abstract void closeBeanFactory();

    public abstract ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

}
