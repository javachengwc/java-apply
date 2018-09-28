package com.spring.pseudocode.beans.factory.support;

import com.spring.pseudocode.beans.BeansException;
import com.spring.pseudocode.beans.TypeConverter;
import com.spring.pseudocode.beans.factory.BeanFactory;
import com.spring.pseudocode.beans.factory.FactoryBean;
import com.spring.pseudocode.beans.factory.NoSuchBeanDefinitionException;
import com.spring.pseudocode.beans.factory.ObjectFactory;
import com.spring.pseudocode.beans.factory.config.*;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.BeanIsNotAFactoryException;
import org.springframework.util.ClassUtils;

import java.security.AccessControlContext;
import java.util.*;

//spring中有两种类型的Bean：一种是普通的JavaBean；另一种就是工厂Bean（FactoryBean），这两种Bean都受Spring的IoC容器管理
//FactoryBean跟普通Bean不同，它是实现了FactoryBean<T>接口的Bean，通过BeanFactory类的getBean方法直接获取到的并不是该FactoryBean的实例，而是该FactoryBean中方法getObject返回的对象。
//可以通过其它途径获取到该FactoryBean的实例，方法就是在通过getBean方法获取实例时在参数name前面加上“&”符号即可。
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory
{
    private BeanFactory parentBeanFactory;

    private TypeConverter typeConverter;

    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    private ClassLoader tempClassLoader;

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

    private boolean hasInstantiationAwareBeanPostProcessors;
    private boolean hasDestructionAwareBeanPostProcessors;

    private final Map<String, Scope> scopes = new LinkedHashMap(8);

    public AbstractBeanFactory()
    {
    }

    public AbstractBeanFactory(BeanFactory parentBeanFactory)
    {
        this.parentBeanFactory = parentBeanFactory;
    }

    public Object getBean(String name) throws BeansException
    {
        return doGetBean(name, null, null, false);
    }

    public <T> T getBean(String name, Class<T> requiredType) throws BeansException
    {
        return doGetBean(name, requiredType, null, false);
    }

    public Object getBean(String name, Object[] args) throws BeansException
    {
        return doGetBean(name, null, args, false);
    }

    public <T> T getBean(String name, Class<T> requiredType, Object[] args) throws BeansException
    {
        return doGetBean(name, requiredType, args, false);
    }

    protected String transformedBeanName(String name)
    {
        return canonicalName(BeanFactoryUtils.transformedBeanName(name));
    }

    protected String originalBeanName(String name)
    {
        String beanName = transformedBeanName(name);
        if (name.startsWith("&")) {
            beanName = new StringBuilder().append("&").append(beanName).toString();
        }
        return beanName;
    }

    protected boolean hasInstantiationAwareBeanPostProcessors()
    {
        return this.hasInstantiationAwareBeanPostProcessors;
    }

    protected <T> T doGetBean(String name, Class<T> requiredType, Object[] args, boolean typeCheckOnly) throws BeansException
    {
        //查找name是否有别名，获取最终的beanName
        String beanName = transformedBeanName(name);
        //如果bean是单例模式，首先尝试从缓存中获取
        Object sharedInstance = getSingleton(beanName);
        Object bean=null;
        if ((sharedInstance != null) && (args == null)) {
            //getObjectForBeanInstance中会选择bean实例是普通的Bean还是FactoryBean
            bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
        }
        else
        {
            //...
            //判断容器是否有父容器，如果有则首先尝试从父容器中获取
            //BeanFactory parentBeanFactory = getParentBeanFactory();
            if ((parentBeanFactory != null) && (!containsBeanDefinition(beanName)))
            {
                String nameToLookup = originalBeanName(name);
                if (args != null)
                {
                    return (T)parentBeanFactory.getBean(nameToLookup, args);
                }
                return parentBeanFactory.getBean(nameToLookup, requiredType);
            }
            //...

            try {
                //根据beanName获取bean的元数据
                final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
                //...
                String[] dependsOn = mbd.getDependsOn();
                if (dependsOn != null) {
                    for (String dep : dependsOn) {
                        if (isDependent(beanName, dep)) {
                            throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                                    "Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
                        }
                        registerDependentBean(dep, beanName);
                        getBean(dep);
                    }
                }

                // bean是单例
                if (mbd.isSingleton()) {
                    //获取bean，首先会暴露一个ObjectFactory，通过调用getObject来调用createBean(beanName, mbd, args)获取bean
                    sharedInstance = getSingleton(beanName, new ObjectFactory<Object>() {
                        public Object getObject() throws BeansException {
                            try {
                                return createBean(beanName, mbd, args);
                            }
                            catch (BeansException ex) {
                                destroySingleton(beanName);
                                throw ex;
                            }
                        }
                    });
                    //判断bean是否是FactoryBean，如果不是直接返回sharedInstance，否则调用sharedInstance.getObject()方法返回bean
                    bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
                }
                //bean是原型
                else if (mbd.isPrototype()) {
                    // It's a prototype -> create a new instance.
                    Object prototypeInstance = null;
                    try {
                        //beforePrototypeCreation(beanName);
                        //初始化bean
                        prototypeInstance = createBean(beanName, mbd, args);
                    }
                    finally {
                        //afterPrototypeCreation(beanName);
                    }
                    bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
                }
                else {
                    //bean是其他模式
                    String scopeName = mbd.getScope();
                    final Scope scope = this.scopes.get(scopeName);
                    if (scope == null) {
                        throw new IllegalStateException("No Scope registered for scope name '" + scopeName + "'");
                    }
                    try {
                        //Object scopedInstance = scope.get(beanName, new ObjectFactory<Object>() {
                        Object scopedInstance =null;
                        bean = getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
                    }
                    catch (IllegalStateException ex) {
                        throw new BeanCreationException(beanName,"Scope '" + scopeName + "' is not active for the current thread;...", ex);
                    }
                }
            }
            catch (BeansException ex) {
                //...............
                throw ex;
            }
        }
        return (T)bean;
    }

    public boolean containsBean(String name)
    {
        String beanName =name;
        if ((containsSingleton(beanName)) || (containsBeanDefinition(beanName))) {
            return (!BeanFactoryUtils.isFactoryDereference(name)) || (isFactoryBean(name));
        }
        //...
        return  false;
    }

    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException
    {
        //...
        return false;
    }

    public boolean isPrototype(String name)  throws NoSuchBeanDefinitionException
    {
       //...
        return false;
    }

    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor)
    {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
        if ((beanPostProcessor instanceof InstantiationAwareBeanPostProcessor)) {
            this.hasInstantiationAwareBeanPostProcessors = true;
        }
//        if ((beanPostProcessor instanceof DestructionAwareBeanPostProcessor))
//            this.hasDestructionAwareBeanPostProcessors = true;
    }

    public BeanDefinition getMergedBeanDefinition(String name) throws BeansException
    {
        String beanName = transformedBeanName(name);
        if ((!containsBeanDefinition(beanName)) && ((getParentBeanFactory() instanceof ConfigurableBeanFactory))) {
            return ((ConfigurableBeanFactory)getParentBeanFactory()).getMergedBeanDefinition(beanName);
        }
        return getMergedLocalBeanDefinition(beanName);
    }

    public boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException
    {
        String beanName = transformedBeanName(name);

        Object beanInstance = getSingleton(beanName, false);
        if (beanInstance != null) {
            return beanInstance instanceof FactoryBean;
        }
        if (containsSingleton(beanName))
        {
            return false;
        }

        if ((!containsBeanDefinition(beanName)) && ((getParentBeanFactory() instanceof ConfigurableBeanFactory)))
        {
            //return ((ConfigurableBeanFactory)getParentBeanFactory()).isFactoryBean(name);
            return true;
        }

        return isFactoryBean(beanName, getMergedLocalBeanDefinition(beanName));
    }

    protected RootBeanDefinition getMergedLocalBeanDefinition(String beanName) throws BeansException
    {
        //RootBeanDefinition mbd = (RootBeanDefinition)this.mergedBeanDefinitions.get(beanName);
        RootBeanDefinition mbd =null;
        if (mbd != null) {
            return mbd;
        }
        //...
        return null;
    }

    protected boolean isFactoryBean(String beanName, RootBeanDefinition mbd)
    {
        //Class beanType = predictBeanType(beanName, mbd, new Class[] { FactoryBean.class });
        Class beanType =null;
        return (beanType != null) && (FactoryBean.class.isAssignableFrom(beanType));
    }

    protected Class<?> getTypeForFactoryBean(String beanName, RootBeanDefinition mbd)
    {
        if (!mbd.isSingleton())
            return null;
        try
        {
            FactoryBean factoryBean = (FactoryBean)doGetBean(new StringBuilder().append("&").append(beanName).toString(), FactoryBean.class, null, true);
            return getTypeForFactoryBean(factoryBean);
        }
        catch (BeanCreationException ex) {
            onSuppressedException(ex);
        }
        return null;
    }

    protected Object getObjectForBeanInstance(Object beanInstance, String name, String beanName, RootBeanDefinition mbd)
    {
        if ((BeanFactoryUtils.isFactoryDereference(name)) && (!(beanInstance instanceof FactoryBean))) {
            throw new BeanIsNotAFactoryException(transformedBeanName(name), beanInstance.getClass());
        }

        //判断bean类型是否是FactoryBean，或者name是否是以&开头，如果不是则直接返回
        if ((!(beanInstance instanceof FactoryBean)) || (BeanFactoryUtils.isFactoryDereference(name))) {
            return beanInstance;
        }

        //如果是FactoryBean则从getObjectFromFactoryBean中获取
        Object object = null;
        if (mbd == null) {
            object = getCachedObjectForFactoryBean(beanName);
        }
        if (object == null)
        {
            FactoryBean<?> factory = (FactoryBean<?>)beanInstance;
            //...
            boolean synthetic = (mbd != null) && (mbd.isSynthetic());
            object = getObjectFromFactoryBean(factory, beanName, !synthetic);
        }
        return object;
    }

    protected TypeConverter getCustomTypeConverter()
    {
        return this.typeConverter;
    }

    public TypeConverter getTypeConverter()
    {
        TypeConverter customConverter = getCustomTypeConverter();
        if (customConverter != null) {
            return customConverter;
        }

        //SimpleTypeConverter typeConverter = new SimpleTypeConverter();
        //typeConverter.setConversionService(getConversionService());
        //registerCustomEditors(typeConverter);
        //return typeConverter;
        return null;
    }

    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, RootBeanDefinition mbd)
    {
        //AccessControlContext acc = System.getSecurityManager() != null ? getAccessControlContext() : null;
        AccessControlContext acc =null;
        if ((!mbd.isPrototype()) && (requiresDestruction(bean, mbd))) {
            if (mbd.isSingleton()) {
                //registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, mbd, getBeanPostProcessors(), acc));
            } else {
               //...
            }
        }
    }

    protected boolean requiresDestruction(Object bean, RootBeanDefinition mbd)
    {
        //...
        return true;
    }

    protected abstract boolean containsBeanDefinition(String param);

    protected abstract BeanDefinition getBeanDefinition(String param) throws BeansException;

    protected abstract Object createBean(String param, RootBeanDefinition rootBeanDefinition, Object[] arrayOfObject)  throws BeanCreationException;
}