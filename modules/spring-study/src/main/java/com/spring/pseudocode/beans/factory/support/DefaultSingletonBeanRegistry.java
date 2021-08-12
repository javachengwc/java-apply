package com.spring.pseudocode.beans.factory.support;

import com.spring.pseudocode.beans.factory.ObjectFactory;
import com.spring.pseudocode.beans.factory.config.SingletonBeanRegistry;
import com.spring.pseudocode.core.core.SimpleAliasRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanCreationNotAllowedException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry  extends SimpleAliasRegistry implements SingletonBeanRegistry
{
    protected static final Object NULL_OBJECT = new Object();

    protected final Log logger = LogFactory.getLog(getClass());

    //一级缓存
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap(256);

    //三级缓存
    //三级缓存是为了解决一个重要的问题：早期被别人拿去使用的bean和最终成型的bean是否是一个 bean，如果不是同一个，则会产生异常
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap(16);

    //二级缓存
    //当bean进入到二级缓存的时候说明这个bean的早期对象被其他bean注入了,
    //也就是说，这个bean还是半成品，还未完全创建好的时候，已经被别人拿去使用了，所以必须要有三级缓存，
    //二级缓存中存放的是早期的被别人使用的对象，如果没有二级缓存，是无法判断这个对象在创建的过程中，是否被别人拿去使用了。
    private final Map<String, Object> earlySingletonObjects = new HashMap(16);

    private final Set<String> registeredSingletons = new LinkedHashSet(256);

    //正在创建的单例名称（为了判断是否存在循环依赖）
    private final Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap(16));

    private Set<Exception> suppressedExceptions;
    private boolean singletonsCurrentlyInDestruction = false;

    private final Map<String, Object> disposableBeans = new LinkedHashMap();

    private final Map<String, Set<String>> containedBeanMap = new ConcurrentHashMap(16);

    private final Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap(64);

    private final Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap(64);

    //注册单例bean,添加到一级缓存
    public void registerSingleton(String beanName, Object singletonObject) throws IllegalStateException
    {
        synchronized (this.singletonObjects) {
            Object oldObject = this.singletonObjects.get(beanName);
            if (oldObject != null) {
                throw new IllegalStateException("Could not register object [" + singletonObject + "] under bean name '" + beanName + "': there is already object [" + oldObject + "] bound");
            }
            addSingleton(beanName, singletonObject);
        }
    }

    //添加到一级缓存
    protected void addSingleton(String beanName, Object singletonObject)
    {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, singletonObject != null ? singletonObject : NULL_OBJECT);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.add(beanName);
        }
    }

    //添加到三级缓存
    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory)
    {
        synchronized (this.singletonObjects) {
            if (!this.singletonObjects.containsKey(beanName)) {
                this.singletonFactories.put(beanName, singletonFactory);
                this.earlySingletonObjects.remove(beanName);
                this.registeredSingletons.add(beanName);
            }
        }
    }

    public Object getSingleton(String beanName)
    {
        return getSingleton(beanName, true);
    }

    protected Object getSingleton(String beanName, boolean allowEarlyReference)
    {
        //从一级缓存拿
        Object singletonObject = this.singletonObjects.get(beanName);
        if ((singletonObject == null) && (isSingletonCurrentlyInCreation(beanName))) {
            //bean正在创建中
            synchronized (this.singletonObjects) {
                //从二级缓存获取
                singletonObject = this.earlySingletonObjects.get(beanName);
                if ((singletonObject == null) && (allowEarlyReference)) {
                    //从三级缓存获取
                    ObjectFactory singletonFactory = (ObjectFactory)this.singletonFactories.get(beanName);
                    if (singletonFactory != null) {
                        singletonObject = singletonFactory.getObject();
                        this.earlySingletonObjects.put(beanName, singletonObject);
                        this.singletonFactories.remove(beanName);
                    }
                }
            }
        }
        return singletonObject != NULL_OBJECT ? singletonObject : null;
    }

    //bean是否正在创建
    public boolean isSingletonCurrentlyInCreation(String beanName) {
        //...
        return false;
    }


    public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory)
    {
        synchronized (this.singletonObjects) {
            //从一级缓存获取
            Object singletonObject = this.singletonObjects.get(beanName);
            if (singletonObject == null) {
                if (this.singletonsCurrentlyInDestruction) {
                    throw new BeanCreationNotAllowedException(beanName, "Singleton bean creation not allowed while singletons of this factory are in destruction (Do not request a bean from a BeanFactory in a destroy method implementation!)");
                }
                //beforeSingletonCreation(beanName);  //将bean加入到创建列表
                boolean newSingleton = false;
                boolean recordSuppressedExceptions = this.suppressedExceptions == null;
                if (recordSuppressedExceptions)
                    this.suppressedExceptions = new LinkedHashSet();
                try
                {
                    //创建bean
                    singletonObject = singletonFactory.getObject();
                    newSingleton = true;
                }
                catch (IllegalStateException ex)
                {
                    singletonObject = this.singletonObjects.get(beanName);
                    if (singletonObject == null)
                        throw ex;
                }
                catch (BeanCreationException ex)
                {
                    if (recordSuppressedExceptions) {
                        for (Exception suppressedException : this.suppressedExceptions) {
                            ex.addRelatedCause(suppressedException);
                        }
                    }
                    throw ex;
                }
                finally {
                    if (recordSuppressedExceptions) {
                        this.suppressedExceptions = null;
                    }
                    //将beanName从当前创建列表中移除
                    //afterSingletonCreation(beanName);
                }
                if (newSingleton) {
                    //将创建好的单例bean放到一级缓存中,并将其从二，三级缓存中移除
                    addSingleton(beanName, singletonObject);
                }
            }
            return singletonObject != NULL_OBJECT ? singletonObject : null;
        }
    }

    protected void onSuppressedException(Exception ex)
    {
        synchronized (this.singletonObjects) {
            if (this.suppressedExceptions != null)
                this.suppressedExceptions.add(ex);
        }
    }

    protected void removeSingleton(String beanName)
    {
        synchronized (this.singletonObjects) {
            this.singletonObjects.remove(beanName);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.remove(beanName);
        }
    }

    public boolean containsSingleton(String beanName)
    {
        return this.singletonObjects.containsKey(beanName);
    }

    public String[] getSingletonNames()
    {
        synchronized (this.singletonObjects) {
            return StringUtils.toStringArray(this.registeredSingletons);
        }
    }

    public int getSingletonCount()
    {
        synchronized (this.singletonObjects) {
            return this.registeredSingletons.size();
        }
    }


    public void registerDisposableBean(String beanName, DisposableBean bean)
    {
        synchronized (this.disposableBeans) {
            this.disposableBeans.put(beanName, bean);
        }
    }

    public void registerContainedBean(String containedBeanName, String containingBeanName)
    {
        Set containedBeans = (Set)this.containedBeanMap.get(containingBeanName);
        if ((containedBeans != null) && (containedBeans.contains(containedBeanName))) {
            return;
        }

        synchronized (this.containedBeanMap) {
            containedBeans = (Set)this.containedBeanMap.get(containingBeanName);
            if (containedBeans == null) {
                containedBeans = new LinkedHashSet(8);
                this.containedBeanMap.put(containingBeanName, containedBeans);
            }
            containedBeans.add(containedBeanName);
        }
        registerDependentBean(containedBeanName, containingBeanName);
    }

    public void registerDependentBean(String beanName, String dependentBeanName)
    {
        String canonicalName = null;
        Set dependentBeans = (Set)this.dependentBeanMap.get(canonicalName);
        if ((dependentBeans != null) && (dependentBeans.contains(dependentBeanName))) {
            return;
        }
        synchronized (this.dependentBeanMap) {
            dependentBeans = (Set)this.dependentBeanMap.get(canonicalName);
            if (dependentBeans == null) {
                dependentBeans = new LinkedHashSet(8);
                this.dependentBeanMap.put(canonicalName, dependentBeans);
            }
            dependentBeans.add(dependentBeanName);
        }
        synchronized (this.dependenciesForBeanMap) {
            Set<String> dependenciesForBean = (Set<String>)this.dependenciesForBeanMap.get(dependentBeanName);
            if (dependenciesForBean == null) {
                dependenciesForBean = new LinkedHashSet(8);
                this.dependenciesForBeanMap.put(dependentBeanName, dependenciesForBean);
            }
            ((Set)dependenciesForBean).add(canonicalName);
        }
    }

    protected boolean isDependent(String beanName, String dependentBeanName)
    {
        return isDependent(beanName, dependentBeanName, null);
    }

    private boolean isDependent(String beanName, String dependentBeanName, Set<String> alreadySeen) {
        //...
        return false;
    }

    protected boolean hasDependentBean(String beanName)
    {
        return this.dependentBeanMap.containsKey(beanName);
    }

    public String[] getDependentBeans(String beanName)
    {
        Set dependentBeans = (Set)this.dependentBeanMap.get(beanName);
        if (dependentBeans == null) {
            return new String[0];
        }
        return StringUtils.toStringArray(dependentBeans);
    }

    public String[] getDependenciesForBean(String beanName)
    {
        Set dependenciesForBean = (Set)this.dependenciesForBeanMap.get(beanName);
        if (dependenciesForBean == null) {
            return new String[0];
        }
        return (String[])dependenciesForBean.toArray(new String[dependenciesForBean.size()]);
    }

    public void destroySingletons() {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Destroying singletons in " + this);
        }
        synchronized (this.singletonObjects) {
            this.singletonsCurrentlyInDestruction = true;
        }
        String[] disposableBeanNames;
        synchronized (this.disposableBeans) {
            disposableBeanNames = StringUtils.toStringArray(this.disposableBeans.keySet());
        }
        for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
            destroySingleton(disposableBeanNames[i]);
        }

        this.containedBeanMap.clear();
        this.dependentBeanMap.clear();
        this.dependenciesForBeanMap.clear();

        synchronized (this.singletonObjects) {
            this.singletonObjects.clear();
            this.singletonFactories.clear();
            this.earlySingletonObjects.clear();
            this.registeredSingletons.clear();
            this.singletonsCurrentlyInDestruction = false;
        }
    }

    public void destroySingleton(String beanName)
    {
        removeSingleton(beanName);
        DisposableBean disposableBean;
        synchronized (this.disposableBeans) {
            disposableBean = (DisposableBean)this.disposableBeans.remove(beanName);
        }
        destroyBean(beanName, disposableBean);
    }

    protected void destroyBean(String beanName, DisposableBean bean)
    {
        //...
        this.dependenciesForBeanMap.remove(beanName);
    }

    public final Object getSingletonMutex()
    {
        return this.singletonObjects;
    }
}