package com.spring.pseudocode.beans.factory.support;

import com.spring.pseudocode.beans.BeansException;
import com.spring.pseudocode.beans.factory.FactoryBean;
import com.spring.pseudocode.beans.factory.ListableBeanFactory;
import com.spring.pseudocode.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.*;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StaticListableBeanFactory implements ListableBeanFactory
{
    private final Map<String, Object> beans;

    public StaticListableBeanFactory()
    {
        this.beans = new LinkedHashMap();
    }

    public StaticListableBeanFactory(Map<String, Object> beans)
    {
        this.beans = beans;
    }

    public void addBean(String name, Object bean)
    {
        this.beans.put(name, bean);
    }

    public Object getBean(String name) throws BeansException
    {
        String beanName = BeanFactoryUtils.transformedBeanName(name);
        Object bean = this.beans.get(beanName);

        if (bean == null)
        {
            throw new NoSuchBeanDefinitionException(beanName,
                    "Defined beans are [" + StringUtils.collectionToCommaDelimitedString(this.beans.keySet()) + "]");
        }

        if ((BeanFactoryUtils.isFactoryDereference(name)) && (!(bean instanceof FactoryBean))) {
            throw new BeanIsNotAFactoryException(beanName, bean.getClass());
        }

        if (((bean instanceof FactoryBean)) && (!BeanFactoryUtils.isFactoryDereference(name))) {
            try {
                return ((FactoryBean)bean).getObject();
            }
            catch (Exception ex) {
                throw new BeanCreationException(beanName, "FactoryBean threw exception on object creation", ex);
            }
        }

        return bean;
    }

    public <T> T getBean(String name, Class<T> requiredType) throws BeansException
    {
        Object bean = getBean(name);
        if ((requiredType != null) && (!requiredType.isAssignableFrom(bean.getClass()))) {
            throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
        }
        return (T)bean;
    }

    public <T> T getBean(Class<T> requiredType) throws BeansException
    {
        String[] beanNames = getBeanNamesForType(requiredType);
        if (beanNames.length == 1) {
            return getBean(beanNames[0], requiredType);
        }
        if (beanNames.length > 1) {
            throw new NoUniqueBeanDefinitionException(requiredType, beanNames);
        }
        throw new NoSuchBeanDefinitionException(requiredType);
    }

    public Object getBean(String name, Object[] args) throws BeansException
    {
        if (args != null) {
            throw new UnsupportedOperationException("StaticListableBeanFactory does not support explicit bean creation arguments");
        }
        return getBean(name);
    }

    public <T> T getBean(Class<T> requiredType, Object[] args) throws BeansException
    {
        if (args != null) {
            throw new UnsupportedOperationException("StaticListableBeanFactory does not support explicit bean creation arguments");
        }
        return getBean(requiredType);
    }

    public boolean containsBean(String name)
    {
        return this.beans.containsKey(name);
    }

    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException
    {
        Object bean = getBean(name);
        return ((bean instanceof FactoryBean)) && (((FactoryBean)bean).isSingleton());
    }

    public boolean isPrototype(String name) throws NoSuchBeanDefinitionException
    {
        Object bean = getBean(name);
        return (((bean instanceof SmartFactoryBean)) && (((SmartFactoryBean)bean).isPrototype())) ||
                (((bean instanceof FactoryBean)) && (!((FactoryBean)bean).isSingleton()));
    }

    public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException
    {
        Class type = getType(name);
        return (type != null) && (typeToMatch.isAssignableFrom(type));
    }

    public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException
    {
        Class type = getType(name);
        return (typeToMatch == null) || ((type != null) && (typeToMatch.isAssignableFrom(type)));
    }

    public Class<?> getType(String name) throws NoSuchBeanDefinitionException
    {
        String beanName = BeanFactoryUtils.transformedBeanName(name);

        Object bean = this.beans.get(beanName);
        if (bean == null)
        {
            throw new NoSuchBeanDefinitionException(beanName,
                    "Defined beans are [" + StringUtils.collectionToCommaDelimitedString(this.beans.keySet()) + "]");
        }

        if (((bean instanceof FactoryBean)) && (!BeanFactoryUtils.isFactoryDereference(name)))
        {
            return ((FactoryBean)bean).getObjectType();
        }
        return bean.getClass();
    }

    public String[] getAliases(String name)
    {
        return new String[0];
    }

    public boolean containsBeanDefinition(String name)
    {
        return this.beans.containsKey(name);
    }

    public int getBeanDefinitionCount()
    {
        return this.beans.size();
    }

    public String[] getBeanDefinitionNames()
    {
        return StringUtils.toStringArray(this.beans.keySet());
    }

    public String[] getBeanNamesForType(ResolvableType type)
    {
        boolean isFactoryType = false;
        if (type != null) {
            Class resolved = type.resolve();
            if ((resolved != null) && (FactoryBean.class.isAssignableFrom(resolved))) {
                isFactoryType = true;
            }
        }
        List matches = new ArrayList();
        for (Map.Entry entry : this.beans.entrySet()) {
            String name = (String)entry.getKey();
            Object beanInstance = entry.getValue();
            if (((beanInstance instanceof FactoryBean)) && (!isFactoryType)) {
                Class objectType = ((FactoryBean)beanInstance).getObjectType();
                if ((objectType != null) && ((type == null) || (type.isAssignableFrom(objectType)))) {
                    matches.add(name);
                }

            }
            else if ((type == null) || (type.isInstance(beanInstance))) {
                matches.add(name);
            }
        }

        return StringUtils.toStringArray(matches);
    }

    public String[] getBeanNamesForType(Class<?> type)
    {
        return getBeanNamesForType(ResolvableType.forClass(type));
    }

    public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit)
    {
        return getBeanNamesForType(ResolvableType.forClass(type));
    }

    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException
    {
        return getBeansOfType(type, true, true);
    }

    public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
            throws BeansException
    {
        boolean isFactoryType = (type != null) && (FactoryBean.class.isAssignableFrom(type));
        Map matches = new LinkedHashMap();

        for (Map.Entry entry : this.beans.entrySet()) {
            String beanName = (String)entry.getKey();
            Object beanInstance = entry.getValue();

            if (((beanInstance instanceof FactoryBean)) && (!isFactoryType))
            {
                FactoryBean factory = (FactoryBean)beanInstance;
                Class objectType = factory.getObjectType();
                if (((includeNonSingletons) || (factory.isSingleton())) && (objectType != null) &&
                        ((type == null) || (type.isAssignableFrom(objectType))))
                {
                    matches.put(beanName, getBean(beanName, type));
                }

            }
            else if ((type == null) || (type.isInstance(beanInstance)))
            {
                if (isFactoryType) {
                    beanName = "&" + beanName;
                }
                matches.put(beanName, beanInstance);
            }
        }

        return matches;
    }

    public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType)
    {
        List results = new ArrayList();
        for (String beanName : this.beans.keySet()) {
            if (findAnnotationOnBean(beanName, annotationType) != null) {
                results.add(beanName);
            }
        }
        return (String[])results.toArray(new String[results.size()]);
    }

    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException
    {
        Map results = new LinkedHashMap();
        for (String beanName : this.beans.keySet()) {
            if (findAnnotationOnBean(beanName, annotationType) != null) {
                results.put(beanName, getBean(beanName));
            }
        }
        return results;
    }

    public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) throws NoSuchBeanDefinitionException
    {
        return AnnotationUtils.findAnnotation(getType(beanName), annotationType);
    }
}
