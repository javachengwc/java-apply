package com.component.rest;

import com.component.rest.config.RestProp;
import com.component.rest.filter.BalanceClientFilter;
import com.component.rest.util.RestUtil;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.client.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Service
@ConditionalOnMissingBean(ResourceFactory.class)
public class ResourceFactory implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ResourceFactory.class);

    @Autowired
    protected RestProp restProp;

    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired(required = false)
    protected ResourceLocator resourceLocator;

    @Autowired(required = false)
    protected BalanceClientFilter balanceClientFilter;

    @Autowired(required = false)
    protected Map<String, ClientRequestFilter> clientRequestFilters = new HashMap<String, ClientRequestFilter>();

    @Autowired(required = false)
    protected Map<String, ClientResponseFilter> clientResponseFilters = new HashMap<String, ClientResponseFilter>();

    protected Client createJerseyClient(){
        JerseyClient client = (JerseyClient) JerseyClientBuilder.newBuilder().build();

        client.register(JacksonConfig.class);
        client.property(ClientProperties.CONNECT_TIMEOUT, restProp.getRestConnectTimeoutSec()*1000);
        client.property(ClientProperties.READ_TIMEOUT, restProp.getRestReadTimeoutSec()*1000);

        Map<String, Object> clientFilters = new HashMap<String, Object>();
        clientFilters.putAll(clientRequestFilters);
        clientFilters.putAll(clientResponseFilters);

        for(Map.Entry<String, Object> entry : clientFilters.entrySet()){
            client.register(entry.getValue());
        }
        client.getConfiguration().connectorProvider(new HttpConnectorProvider());
        return client;
    }

    public <T> T getResource(Class<T> resourceClass) {
        String appName = RestUtil.getApplicationName(resourceClass);
        if (StringUtils.isBlank(appName)) {
            return null;
        }
        String resourceClassName= resourceClass.getName();
        logger.info("ResourceFactory getResource resourceClassName={},appName={}", resourceClassName, appName);

        String url = null;
        if (balanceClientFilter != null) {
            url = "http://" + appName;
        } else {
            url = resourceLocator.locate(appName);
        }
        logger.info("ResourceFactory getResource gen url, appName={},url={}",appName, url);

        WebTarget target = createJerseyClient().target(url);
        T t = RestResourceFactory.newResource(resourceClass, target);
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

    public <T> T getMvcResource(Class<T> resourceClass,RestTemplate restTemplate) {
        String appName = RestUtil.getApplicationName(resourceClass);
        if (StringUtils.isBlank(appName)) {
            return null;
        }
        String resourceClassName= resourceClass.getName();
        logger.info("ResourceFactory getMvcResource resourceClassName={},appName={}", resourceClassName, appName);

        String url = null;
        if (balanceClientFilter != null) {
            url = "http://" + appName;
        } else {
            url = resourceLocator.locate(appName);
        }
        logger.info("ResourceFactory getMvcResource gen url, appName={},url={}",appName, url);

        T t = MvcResourceFactory.newResource(resourceClass,url,restTemplate);
        return newResource(t, new ResourceWrapper(t));
    }
}