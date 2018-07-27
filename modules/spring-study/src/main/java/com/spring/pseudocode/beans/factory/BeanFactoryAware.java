package com.spring.pseudocode.beans.factory;

import com.spring.pseudocode.beans.BeansException;

public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
