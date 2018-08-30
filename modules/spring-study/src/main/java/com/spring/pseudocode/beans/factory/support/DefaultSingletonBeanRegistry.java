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

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap(256);

    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap(16);

    private final Map<String, Object> earlySingletonObjects = new HashMap(16);

    private final Set<String> registeredSingletons = new LinkedHashSet(256);

    private final Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap(16));

    private Set<Exception> suppressedExceptions;
    private boolean singletonsCurrentlyInDestruction = false;

    private final Map<String, Object> disposableBeans = new LinkedHashMap();

    private final Map<String, Set<String>> containedBeanMap = new ConcurrentHashMap(16);

    private final Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap(64);

    private final Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap(64);

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

    protected void addSingleton(String beanName, Object singletonObject)
    {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, singletonObject != null ? singletonObject : NULL_OBJECT);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.add(beanName);
        }
    }

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
        Object singletonObject = this.singletonObjects.get(beanName);
        if ((singletonObject == null) && (isSingletonCurrentlyInCreation(beanName))) {
            synchronized (this.singletonObjects) {
                singletonObject = this.earlySingletonObjects.get(beanName);
                if ((singletonObject == null) && (allowEarlyReference)) {
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

    public boolean isSingletonCurrentlyInCreation(String beanName) {
        //...
        return false;
    }


    public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory)
    {
        synchronized (this.singletonObjects) {
            Object singletonObject = this.singletonObjects.get(beanName);
            if (singletonObject == null) {
                if (this.singletonsCurrentlyInDestruction) {
                    throw new BeanCreationNotAllowedException(beanName, "Singleton bean creation not allowed while singletons of this factory are in destruction (Do not request a bean from a BeanFactory in a destroy method implementation!)");
                }
                //beforeSingletonCreation(beanName);
                boolean newSingleton = false;
                boolean recordSuppressedExceptions = this.suppressedExceptions == null;
                if (recordSuppressedExceptions)
                    this.suppressedExceptions = new LinkedHashSet();
                try
                {
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
                    //afterSingletonCreation(beanName);
                }
                if (newSingleton) {
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