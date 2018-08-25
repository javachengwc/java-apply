package com.spring.pseudocode.beans.factory.support;

import com.spring.pseudocode.beans.BeansException;
import com.spring.pseudocode.beans.factory.*;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.ObjectUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory
{
    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    private boolean allowCircularReferences = true;

    private boolean allowRawInjectionDespiteWrapping = false;

    private final Set<Class<?>> ignoredDependencyTypes = new HashSet();

    private final Set<Class<?>> ignoredDependencyInterfaces = new HashSet();

    private final Map<String, BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap(16);

    private final ConcurrentMap<Class<?>, PropertyDescriptor[]> filteredPropertyDescriptorsCache = new ConcurrentHashMap(256);

    public AbstractAutowireCapableBeanFactory()
    {
        ignoreDependencyInterface(BeanNameAware.class);
        ignoreDependencyInterface(BeanFactoryAware.class);
        ignoreDependencyInterface(BeanClassLoaderAware.class);
    }

    public AbstractAutowireCapableBeanFactory(BeanFactory parentBeanFactory)
    {
        this();
        //setParentBeanFactory(parentBeanFactory);
    }

    public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer)
    {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    protected ParameterNameDiscoverer getParameterNameDiscoverer()
    {
        return this.parameterNameDiscoverer;
    }

    public void setAllowCircularReferences(boolean allowCircularReferences)
    {
        this.allowCircularReferences = allowCircularReferences;
    }

    public void setAllowRawInjectionDespiteWrapping(boolean allowRawInjectionDespiteWrapping)
    {
        this.allowRawInjectionDespiteWrapping = allowRawInjectionDespiteWrapping;
    }

    public void ignoreDependencyType(Class<?> type)
    {
        this.ignoredDependencyTypes.add(type);
    }

    public void ignoreDependencyInterface(Class<?> ifc)
    {
        this.ignoredDependencyInterfaces.add(ifc);
    }

    public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory)
    {
        //...
    }

    public Object initializeBean(Object existingBean, String beanName)
    {
        //return initializeBean(beanName, existingBean, null);
        return null;
    }

    public Object initializeBean(Object existingBean, String beanName,RootBeanDefinition rootBeanDefinition)
    {
        return null;
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        //...
        return null;
    }

    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)  throws BeansException
    {
        Object result = existingBean;
        for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
            result = beanProcessor.postProcessBeforeInitialization(result, beanName);
            if (result == null) {
                return result;
            }
        }
        return result;
    }

    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)  throws BeansException
    {
        Object result = existingBean;
        for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
            result = beanProcessor.postProcessAfterInitialization(result, beanName);
            if (result == null) {
                return result;
            }
        }
        return result;
    }

    public void destroyBean(Object existingBean)
    {
       //...
    }

    protected Object createBean(String beanName, RootBeanDefinition mbd, Object[] args) throws BeanCreationException
    {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Creating instance of bean '" + beanName + "'");
        }
        RootBeanDefinition mbdToUse = mbd;

        //获取类名，通过反射机制来实例化类
        Class resolvedClass = resolveBeanClass(mbd, beanName, new Class[0]);
        if ((resolvedClass != null) && (!mbd.hasBeanClass()) && (mbd.getBeanClassName() != null)) {
            mbdToUse = new RootBeanDefinition(mbd);
            //设置类名
            mbdToUse.setBeanClass(resolvedClass);
        }

        try
        {
            mbdToUse.prepareMethodOverrides();
        }
        catch (Exception ex) {
            //...
        }

        try
        {
            //bean在初始化前做一些预处理操作
            //BeanPostProcessors
            Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
            if (bean != null)
                return bean;
        }
        catch (Throwable ex)
        {
            throw new BeanCreationException(mbdToUse.getResourceDescription(), beanName, "BeanPostProcessor before instantiation of bean failed", ex);
        }
        //初始化bean
        Object beanInstance = doCreateBean(beanName, mbdToUse, args);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Finished creating instance of bean '" + beanName + "'");
        }
        return beanInstance;
    }

    protected Object doCreateBean(String beanName, RootBeanDefinition mbd, Object[] args) throws BeanCreationException
    {
        BeanWrapper instanceWrapper = null;
        if (mbd.isSingleton()) {
            instanceWrapper = (BeanWrapper)this.factoryBeanInstanceCache.remove(beanName);
        }
        if (instanceWrapper == null) {
            //createBeanInstance完成通过构造函数初始化bean的操作
            instanceWrapper = createBeanInstance(beanName, mbd, args);
        }
        Object bean = instanceWrapper != null ? instanceWrapper.getWrappedInstance() : null;
        Class beanType = instanceWrapper != null ? instanceWrapper.getWrappedClass() : null;
        mbd.resolvedTargetType = beanType;
        //...

        //将初始化的bean提前暴露出去，暴露一个ObjectFactory，这也是Spring解决单例bean非构造函数依赖的解决方法
        boolean earlySingletonExposure = (mbd.isSingleton()) && (this.allowCircularReferences) &&
                (isSingletonCurrentlyInCreation(beanName));

        if (earlySingletonExposure) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Eagerly caching bean '" + beanName + "' to allow for resolving potential circular references");
            }

            addSingletonFactory(beanName, new ObjectFactory(beanName, mbd, bean)
            {
                public Object getObject() throws BeansException {
                    //return AbstractAutowireCapableBeanFactory.this.getEarlyBeanReference(this.val$beanName, this.val$mbd, this.val$bean);
                    return null;
                }
            });
        }

        Object exposedObject = bean;
        try {
            //初始化bean的各种注入或者setXX参数
            populateBean(beanName, mbd, instanceWrapper);
            if (exposedObject != null)
                //initializeBean完成了bean注入时设置的init-method方法的执行,
                //同时在执行init-method之前会调用applyBeanPostProcessorsBeforeInitialization完成bean使用前的处理操作，
                //调用applyBeanPostProcessorsAfterInitialization完成bean初始化后的操作；
                //调用注入类的init-method方法
                exposedObject = initializeBean(beanName, exposedObject, mbd);
        }
        catch (Throwable ex)
        {
            if (((ex instanceof BeanCreationException)) && (beanName.equals(((BeanCreationException)ex).getBeanName()))) {
                throw ((BeanCreationException)ex);
            }

            throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Initialization of bean failed", ex);
        }
        //...
        return exposedObject;
    }

    //createBeanInstance中会通过bean的构造函数或者默认构造函数来完成bean的初始化工作
    protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, Object[] args)
    {
        Class beanClass = resolveBeanClass(mbd, beanName, new Class[0]);

        if ((beanClass != null) && (!Modifier.isPublic(beanClass.getModifiers())) && (!mbd.isNonPublicAccessAllowed()))
        {
            throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Bean class isn't public, and non-public access not allowed: " + beanClass
                    .getName());
        }

        if (mbd.getFactoryMethodName() != null) {
            return instantiateUsingFactoryMethod(beanName, mbd, args);
        }

        boolean resolved = false;
        boolean autowireNecessary = false;
        if (args == null) {
            synchronized (mbd.constructorArgumentLock) {
                if (mbd.resolvedConstructorOrFactoryMethod != null) {
                    resolved = true;
                    autowireNecessary = mbd.constructorArgumentsResolved;
                }
            }
        }
        if (resolved) {
            if (autowireNecessary) {
                //通过构造函数初始化
                return autowireConstructor(beanName, mbd, null, null);
            }
            //使用默认构造函数初始化
            return instantiateBean(beanName, mbd);
        }

        Constructor[] ctors = determineConstructorsFromBeanPostProcessors(beanClass, beanName);
        if ((ctors != null) ||
                (mbd
                        .getResolvedAutowireMode() == 3) ||
                (mbd
                        .hasConstructorArgumentValues()) || (!ObjectUtils.isEmpty(args))) {
            return autowireConstructor(beanName, mbd, ctors, args);
        }

        return instantiateBean(beanName, mbd);
    }

    //instantiateBean中会调用bean初始策略InstantiationStrategy的实现类完成bean的初始化操作
    protected BeanWrapper instantiateBean(String beanName, RootBeanDefinition mbd)
    {
        try
        {
            BeanFactory parent = this;
            //调用bean初始化策InstantiationStrategy略初始化bean
            Object beanInstance = getInstantiationStrategy().instantiate(mbd, beanName, parent);
            BeanWrapper bw = new BeanWrapperImpl(beanInstance);
            initBeanWrapper(bw);
            return bw;
        }
        catch (Throwable ex) {
            throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Instantiation of bean failed", ex);
        }
    }

    public void initBeanWrapper(BeanWrapper bw ) {
        //...
    }

    protected void autowireByName(String beanName, AbstractBeanDefinition mbd, BeanWrapper bw, MutablePropertyValues pvs)
    {
        //String[] propertyNames = unsatisfiedNonSimpleProperties(mbd, bw);
        String[] propertyNames =null;
        for (String propertyName : propertyNames) {
            if (containsBean(propertyName)) {
                Object bean = getBean(propertyName);
                pvs.add(propertyName, bean);
                registerDependentBean(propertyName, beanName);

            }
        }
    }

    protected void autowireByType(String beanName, AbstractBeanDefinition mbd, BeanWrapper bw, MutablePropertyValues pvs)
    {
        //...
    }

    private void invokeAwareMethods(String beanName, Object bean) {
        if ((bean instanceof Aware)) {
            if ((bean instanceof BeanNameAware)) {
                ((BeanNameAware)bean).setBeanName(beanName);
            }
            if ((bean instanceof BeanClassLoaderAware)) {
                ((BeanClassLoaderAware)bean).setBeanClassLoader(getBeanClassLoader());
            }
            if ((bean instanceof BeanFactoryAware))
                ((BeanFactoryAware)bean).setBeanFactory(this);
        }
    }

    protected void invokeInitMethods(String beanName, Object bean, RootBeanDefinition mbd)
            throws Throwable
    {
        boolean isInitializingBean = bean instanceof InitializingBean;
        if ((isInitializingBean) && ((mbd == null) || (!mbd.isExternallyManagedInitMethod("afterPropertiesSet")))) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Invoking afterPropertiesSet() on bean with name '" + beanName + "'");
            }
//            if (System.getSecurityManager() != null) {
//                try {
//                    AccessController.doPrivileged(new PrivilegedExceptionAction(bean)
//                                                  {
//                                                      public Object run() throws Exception {
//                                                          ((InitializingBean)this.val$bean).afterPropertiesSet();
//                                                          return null;
//                                                      }
//                                                  }
//                            , getAccessControlContext());
//                }
//                catch (PrivilegedActionException pae) {
//                    throw pae.getException();
//                }
//            }
//            else {
                ((InitializingBean)bean).afterPropertiesSet();
//            }
        }

        if (mbd != null) {
            String initMethodName = mbd.getInitMethodName();
            if ((initMethodName != null) && ((!isInitializingBean) || (!"afterPropertiesSet".equals(initMethodName))) &&
                    (!mbd
                            .isExternallyManagedInitMethod(initMethodName)))
            {
                invokeCustomInitMethod(beanName, bean, mbd);
            }
        }
    }

    protected void invokeCustomInitMethod(String beanName, Object bean, RootBeanDefinition mbd) throws Throwable
    {
        //...
    }

    protected Object postProcessObjectFromFactoryBean(Object object, String beanName)
    {
        return applyBeanPostProcessorsAfterInitialization(object, beanName);
    }

    protected void removeSingleton(String beanName)
    {
        super.removeSingleton(beanName);
        this.factoryBeanInstanceCache.remove(beanName);
    }

}
