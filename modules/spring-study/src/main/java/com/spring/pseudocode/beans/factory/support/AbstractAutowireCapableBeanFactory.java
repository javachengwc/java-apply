package com.spring.pseudocode.beans.factory.support;

import com.spring.pseudocode.beans.BeansException;
import com.spring.pseudocode.beans.PropertyValue;
import com.spring.pseudocode.beans.factory.*;
import com.spring.pseudocode.beans.factory.config.BeanDefinition;
import com.spring.pseudocode.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.ObjectUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory
{
    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    //是否自动尝试去解析循环引用的bean
    private boolean allowCircularReferences = true;

    private boolean allowRawInjectionDespiteWrapping = false;

    //在依赖检查和自动绑定时要忽略的依赖类型，是一组类对象
    private final Set<Class<?>> ignoredDependencyTypes = new HashSet();

    //在依赖检查和自动绑定时要忽略的依赖接口，是一组类对象，默认情况下，只有beanFactory接口被忽略。
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

    //初始化bean
    public Object initializeBean(String beanName, Object bean,RootBeanDefinition mbd)
    {
        //首先执行aware相关接口
        invokeAwareMethods(beanName, bean);
        Object wrappedBean = bean;
        if ((mbd == null) || (!mbd.isSynthetic())) {
            //执行BeanPostProcessor的前置处理
            wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
        }
        try
        {
            //执行InitializingBean的afterPropertiesSet方法和bean的init-method方法
            invokeInitMethods(beanName, wrappedBean, mbd);
        }
        catch (Throwable ex)
        {
            throw new BeanCreationException(mbd != null ? mbd
                    .getResourceDescription() : null, beanName, "Invocation of init method failed", ex);
        }

        if ((mbd == null) || (!mbd.isSynthetic())) {
            //执行BeanPostProcessor的后置处理
            wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
        }
        return wrappedBean;
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        //...
        return null;
    }

    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)  throws BeansException
    {
        Object result = existingBean;
        for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
            //执行BeanPostProcessor的前置处理
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
            //执行BeanPostProcessor的后置处理
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

    public Class resolveBeanClass(RootBeanDefinition mbd,String beanName) {
        //...
        return null;
    }

    //创建bean实例
    protected Object createBean(String beanName, RootBeanDefinition mbd, Object[] args) throws BeanCreationException
    {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Creating instance of bean '" + beanName + "'");
        }
        RootBeanDefinition mbdToUse = mbd;

        //获取类名，通过反射机制来实例化类
        Class resolvedClass = resolveBeanClass(mbd, beanName);
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
            //InstantiationAwareBeanPostProcessor调用postProcessBeforeInstantiation()将在里面执行
            Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
            if (bean != null)
                return bean;
        }
        catch (Throwable ex)
        {
            //...
        }
        //初始化bean
        Object beanInstance = doCreateBean(beanName, mbdToUse, args);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Finished creating instance of bean '" + beanName + "'");
        }
        return beanInstance;
    }

    public Object resolveBeforeInstantiation(String beanName,RootBeanDefinition mbd) {
        //...
        Object bean = null;
        if (!Boolean.FALSE.equals(mbd.beforeInstantiationResolved))
        {
            if ((!mbd.isSynthetic()) && (hasInstantiationAwareBeanPostProcessors())) {
                //Class targetType = determineTargetType(beanName, mbd, new Class[0]);
                Class targetType=null;
                if (targetType != null) {
                    //InstantiationAwareBeanPostProcessor调用postProcessBeforeInstantiation()将在里面执行
                    bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
                    if (bean != null) {
                        bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
                    }
                }
            }
            mbd.beforeInstantiationResolved = Boolean.valueOf(bean != null);
        }
        return bean;
    }

    protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName)
    {
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if ((bp instanceof InstantiationAwareBeanPostProcessor)) {
                InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor)bp;
                //InstantiationAwareBeanPostProcessor调用postProcessBeforeInstantiation()在此执行
                Object result = ibp.postProcessBeforeInstantiation(beanClass, beanName);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    protected Object doCreateBean(String beanName, RootBeanDefinition mbd, Object[] args) throws BeanCreationException
    {
        BeanWrapper instanceWrapper = null;
        if (mbd.isSingleton()) {
            instanceWrapper = (BeanWrapper)this.factoryBeanInstanceCache.remove(beanName);
        }
        if (instanceWrapper == null) {
            //createBeanInstance完成通过构造函数初始化bean的操作，通过反射实例化bean
            instanceWrapper = createBeanInstance(beanName, mbd, args);
        }
        Object bean = instanceWrapper != null ? instanceWrapper.getWrappedInstance() : null;
        Class beanType = instanceWrapper != null ? instanceWrapper.getWrappedClass() : null;
        mbd.resolvedTargetType = beanType;
        //...

        boolean earlySingletonExposure = (mbd.isSingleton()) && (this.allowCircularReferences) &&
                (isSingletonCurrentlyInCreation(beanName));

        if (earlySingletonExposure) {
            //将初始化的bean提前暴露出去，暴露一个ObjectFactory，这也是Spring解决单例bean非构造函数依赖的解决方法
            //添加到三级缓存
            addSingletonFactory(beanName, new ObjectFactory<Object>() {
                public Object getObject() throws BeansException {
                    //return AbstractAutowireCapableBeanFactory.this.getEarlyBeanReference(this.val$beanName, this.val$mbd, this.val$bean);
                    return null;
                }
            });
        }

        Object exposedObject = bean;
        try {
            //populateBean将进行InstantiationAwareBeanPostProcessor.postProcessPropertyValues()的调用
            //初始化bean的各种注入或者setXX参数
            populateBean(beanName, mbd, instanceWrapper);
            if (exposedObject != null)
                //initializeBean完成了bean注入时设置的init-method方法的执行,
                //同时在执行init-method之前会调用applyBeanPostProcessorsBeforeInitialization完成BeanPostProcessor的前置处理，
                //在init-method之后调用applyBeanPostProcessorsAfterInitialization完成BeanPostProcessor的后置处理
                exposedObject = initializeBean(beanName, exposedObject, mbd);
        }
        catch (Throwable ex)
        {
            //...
        }
        //...
        try
        {
            registerDisposableBeanIfNecessary(beanName, bean, mbd);
        }
        catch (BeanDefinitionValidationException ex)
        {
            throw new BeanCreationException(mbd
                    .getResourceDescription(), beanName, "Invalid destruction signature", ex);
        }
        return exposedObject;
    }

    //createBeanInstance中会通过bean的构造函数或者默认构造函数来完成bean的初始化工作
    protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, Object[] args)
    {
        Class beanClass = resolveBeanClass(mbd, beanName);
        //...
        if (mbd.getFactoryMethodName() != null) {
            return instantiateUsingFactoryMethod(beanName, mbd, args);
        }

        boolean resolved = false;
        boolean autowireNecessary = false;
        if (args == null) {
            synchronized (mbd.constructorArgumentLock) {
                if (mbd.resolvedConstructorOrFactoryMethod != null) {
                    resolved = true;
                   // autowireNecessary = mbd.constructorArgumentsResolved;
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
       // Constructor[] ctors = determineConstructorsFromBeanPostProcessors(beanClass, beanName);
        Constructor[] ctors =null;
        if ((ctors != null) || (mbd.getResolvedAutowireMode() == 3) ||
                (mbd.hasConstructorArgumentValues()) || (!ObjectUtils.isEmpty(args))) {
            return autowireConstructor(beanName, mbd, ctors, args);
        }
        return instantiateBean(beanName, mbd);
    }

    protected BeanWrapper instantiateUsingFactoryMethod(String beanName, RootBeanDefinition mbd, Object[] explicitArgs)
    {
        //...
        return null;
    }

    public BeanWrapper autowireConstructor(String beanName,RootBeanDefinition mbd,Constructor[] ctors ,Object[] args) {
        //...
        return null;
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

    //创建bean 实例的策略
    public InstantiationStrategy getInstantiationStrategy () {
        //...
        return null;
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

    //执行aware相关接口
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

    protected void invokeInitMethods(String beanName, Object bean, RootBeanDefinition mbd) throws Throwable
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
                //执行InitializingBean的afterPropertiesSet方法
                ((InitializingBean)bean).afterPropertiesSet();
//            }
        }

        if (mbd != null) {
            String initMethodName = mbd.getInitMethodName();
            if ((initMethodName != null) && ((!isInitializingBean) || (!"afterPropertiesSet".equals(initMethodName))) &&
                    (!mbd.isExternallyManagedInitMethod(initMethodName)))
            {
                //执行bean指定的init-method方法
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

    //注入bean成员变量值
    //对于通过set方法注入的变量值就是调用类的xx.xx.setXx(args ..)来完成变量值的注入操作，
    //对于通过注解@Autowired注入的对象，通过Java提供的反射机制通过获取Field对象来获取变量值，通过调用Field.set(Object obj, Object value)方法来完成变量值的注入操作
    protected void populateBean(String beanName, RootBeanDefinition mbd, BeanWrapper bw)
    {
        //获取要设置的属性值
        PropertyValues pvs = mbd.getPropertyValues();
        //...

        boolean hasInstAwareBpps = hasInstantiationAwareBeanPostProcessors();
        boolean needsDepCheck = mbd.getDependencyCheck() != 0;

        if ((hasInstAwareBpps) || (needsDepCheck)) {
            //PropertyDescriptor[] filteredPds = filterPropertyDescriptorsForDependencyCheck(bw, mbd.allowCaching);
            PropertyDescriptor[] filteredPds=null;
            if (hasInstAwareBpps) {
                for (BeanPostProcessor bp : getBeanPostProcessors()) {
                    if ((bp instanceof InstantiationAwareBeanPostProcessor)) {
                        InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor)bp;
                        //InstantiationAwareBeanPostProcessor调用postProcessPropertyValues方法
                        pvs = ibp.postProcessPropertyValues(pvs, filteredPds, bw.getWrappedInstance(), beanName);
                        if (pvs == null) {
                            return;
                        }
                    }
                }
            }
            //....
        }
        applyPropertyValues(beanName, mbd, bw, pvs);
    }

    //applyPropertyValues方法中对属性值做注入处理，对参数值做深拷贝处理
    protected void applyPropertyValues(String beanName, BeanDefinition mbd, BeanWrapper bw, PropertyValues pvs) {
        if ((pvs == null) || (pvs.isEmpty())) {
            return;
        }

        MutablePropertyValues mpvs = null;

        if ((System.getSecurityManager() != null) &&  ((bw instanceof BeanWrapperImpl))) {
            //((BeanWrapperImpl) bw).setSecurityContext(getAccessControlContext());
        }
        List original=null;
        if ((pvs instanceof MutablePropertyValues)) {
            mpvs = (MutablePropertyValues)pvs;
            if (mpvs.isConverted()) {
                try
                {
                    bw.setPropertyValues(mpvs);
                    return;
                }
                catch (BeansException ex)
                {
                    throw new BeanCreationException("...", beanName, "Error setting property values", ex);
                }
            }
            original = mpvs.getPropertyValueList();
        }
        else {
            original = Arrays.asList(pvs.getPropertyValues());
        }
        List<PropertyValue> deepCopy = new ArrayList<PropertyValue>(original.size());
        //...
        try
        {
            //调用BeanWrapper的方法做参数值注入处理
            //bw.setPropertyValues(new MutablePropertyValues(deepCopy));
        }
        catch (BeansException ex)
        {
            throw new BeanCreationException("...", beanName, "Error setting property values", ex);
        }
    }
}
