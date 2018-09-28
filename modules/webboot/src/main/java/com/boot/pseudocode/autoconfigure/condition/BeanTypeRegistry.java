package com.boot.pseudocode.autoconfigure.condition;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class BeanTypeRegistry implements SmartInitializingSingleton
{
    private static final Log logger = LogFactory.getLog(BeanTypeRegistry.class);

    static final String FACTORY_BEAN_OBJECT_TYPE = "factoryBeanObjectType";

    private static final String BEAN_NAME = BeanTypeRegistry.class.getName();

    private final DefaultListableBeanFactory beanFactory;

    private final Map<String, Class<?>> beanTypes = new HashMap<String, Class<?>>();

    private int lastBeanDefinitionCount = 0;

    private BeanTypeRegistry(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public static BeanTypeRegistry get(ListableBeanFactory beanFactory)
    {
        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory)beanFactory;

        if (!listableBeanFactory.containsLocalBean(BEAN_NAME)) {
            BeanDefinition bd = new RootBeanDefinition(BeanTypeRegistry.class);
            bd.getConstructorArgumentValues().addIndexedArgumentValue(0, beanFactory);
            listableBeanFactory.registerBeanDefinition(BEAN_NAME, bd);
        }

        return (BeanTypeRegistry)listableBeanFactory.getBean(BEAN_NAME, BeanTypeRegistry.class);
    }

    public Set<String> getNamesForType(Class<?> type)
    {
        Set matches = new LinkedHashSet();
        for (Map.Entry entry : this.beanTypes.entrySet()) {
            if ((entry.getValue() != null) && (type.isAssignableFrom((Class)entry.getValue()))) {
                matches.add(entry.getKey());
            }
        }
        return matches;
    }

    public Set<String> getNamesForAnnotation(Class<? extends Annotation> annotation)
    {
        //updateTypesIfNecessary();
        Set matches = new LinkedHashSet();
        for (Map.Entry entry : this.beanTypes.entrySet()) {
            if ((entry.getValue() != null) && (AnnotationUtils.findAnnotation((Class)entry.getValue(), annotation) != null)) {
                matches.add(entry.getKey());
            }
        }
        return matches;
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.beanTypes.clear();
        this.lastBeanDefinitionCount = 0;
    }
}
