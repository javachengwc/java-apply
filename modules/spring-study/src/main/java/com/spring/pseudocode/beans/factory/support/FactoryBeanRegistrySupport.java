package com.spring.pseudocode.beans.factory.support;

import com.spring.pseudocode.beans.BeansException;
import com.spring.pseudocode.beans.factory.FactoryBean;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry
{
    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<String,Object>(16);

    protected Class<?> getTypeForFactoryBean(FactoryBean<?> factoryBean)
    {
        try
        {
            return factoryBean.getObjectType();
        }
        catch (Throwable ex)
        {
        }
        return null;
    }

    protected Object getCachedObjectForFactoryBean(String beanName)
    {
        Object object = this.factoryBeanObjectCache.get(beanName);
        return object != NULL_OBJECT ? object : null;
    }

    //获取factoryBean的入口方法
    protected Object getObjectFromFactoryBean(FactoryBean<?> factory, String beanName, boolean shouldPostProcess)
    {
        if ((factory.isSingleton()) && (containsSingleton(beanName))) {
            synchronized (getSingletonMutex()) {
                Object object = this.factoryBeanObjectCache.get(beanName);
                if (object == null) {
                    object = doGetObjectFromFactoryBean(factory, beanName);

                    Object alreadyThere = this.factoryBeanObjectCache.get(beanName);
                    if (alreadyThere != null) {
                        object = alreadyThere;
                    }
                    else {
                        if ((object != null) && (shouldPostProcess)) {
                            try {
                                object = postProcessObjectFromFactoryBean(object, beanName);
                            }
                            catch (Throwable ex) {
                                throw new BeanCreationException(beanName, "Post-processing of FactoryBean's singleton object failed", ex);
                            }
                        }

                        this.factoryBeanObjectCache.put(beanName, object != null ? object : NULL_OBJECT);
                    }
                }
                return object != NULL_OBJECT ? object : null;
            }
        }

        Object object = doGetObjectFromFactoryBean(factory, beanName);
        //...
        return object;
    }

    private Object doGetObjectFromFactoryBean(FactoryBean<?> factory, String beanName) throws BeanCreationException
    {

        Object object;
        try
        {
            //...
            //执行FactoryBean的getObject方法获取bean
            object = factory.getObject();
        }
        catch (Exception ex)
        {
            throw new BeanCurrentlyInCreationException(beanName, ex.toString());
        }
        return object;
    }

    protected Object postProcessObjectFromFactoryBean(Object object, String beanName) throws BeansException
    {
        return object;
    }

    protected FactoryBean<?> getFactoryBean(String beanName, Object beanInstance) throws BeansException
    {
        if (!(beanInstance instanceof FactoryBean))
        {
            throw new BeanCreationException(beanName, "Bean instance of type [" + beanInstance.getClass() + "] is not a FactoryBean");
        }
        return (FactoryBean)beanInstance;
    }

}