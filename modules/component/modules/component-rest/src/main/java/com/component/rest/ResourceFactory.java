package com.component.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class ResourceFactory implements ApplicationContextAware
{
    private static final Logger logger = LoggerFactory.getLogger(ResourceFactory.class);

    public ResourceFactory()
    {

    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {

    }
}