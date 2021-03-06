package com.component.rest.springmvc;

import com.component.rest.springmvc.util.RestUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

@Component
public class ResourceFactory implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ResourceFactory.class);

    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired(required = false)
    protected ResourceLocator resourceLocator;

    public <T> T getSpringMvcResource(Class<T> resourceClass,RestTemplate restTemplate) {
        String appName = RestUtil.getApplicationName(resourceClass);
        if (StringUtils.isBlank(appName)) {
            return null;
        }
        String resourceClassName= resourceClass.getName();
        logger.info("ResourceFactory getSpringMvcResource resourceClassName={},appName={}", resourceClassName, appName);

        String url = null;
        if (resourceLocator == null) {
            url = "http://" + appName;
        } else {
            url = resourceLocator.locate(appName);
        }
        logger.info("ResourceFactory getSpringMvcResource gen url, appName={},url={}",appName, url);

        T t = SpringMvcResourceFactory.newResource(resourceClass,url,restTemplate);
        return newResource(t, new ResourceWrapper(t));
    }

    public <T> T getRestResource(Class<T> resourceClass,RestTemplate restTemplate) {
        String appName = RestUtil.getApplicationName(resourceClass);
        if (StringUtils.isBlank(appName)) {
            return null;
        }
        String resourceClassName= resourceClass.getName();
        logger.info("ResourceFactory getRestResource resourceClassName={},appName={}", resourceClassName, appName);

        String url = null;
        if (resourceLocator== null) {
            url = "http://" + appName;
        } else {
            url = resourceLocator.locate(appName);
        }
        logger.info("ResourceFactory getRestResource gen url, appName={},url={}",appName, url);

        T t = RestResourceFactory.newResource(resourceClass,url,restTemplate);
        return newResource(t, new ResourceWrapper(t));
    }

    protected <T> T newResource(T t, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(t.getClass().getClassLoader(), t.getClass().getInterfaces(), handler);
    }

    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(applicationContext == event.getApplicationContext()){
            logger.info("ResourceFactory onApplicationEvent ...................");
        }
    }

}