package com.spring.pseudocode.beans.factory;

public abstract interface InitializingBean
{
    public abstract void afterPropertiesSet() throws Exception;
}
