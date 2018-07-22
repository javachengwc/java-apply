package com.spring.pseudocode.webmvc.handle;

import org.springframework.beans.BeansException;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class SimpleUrlHandlerMapping extends AbstractUrlHandlerMapping
{

    private final Map<String, Object> urlMap = new LinkedHashMap();

    public void setMappings(Properties mappings)
    {
        CollectionUtils.mergePropertiesIntoMap(mappings, this.urlMap);
    }

    public void setUrlMap(Map<String, ?> urlMap)
    {
        this.urlMap.putAll(urlMap);
    }

    public Map<String, ?> getUrlMap()
    {
        return this.urlMap;
    }

    public void initApplicationContext() throws BeansException
    {
        //super.initApplicationContext();
        registerHandlers(this.urlMap);
    }

    protected void registerHandlers(Map<String, Object> urlMap) throws BeansException
    {

        for (Map.Entry entry : urlMap.entrySet()) {
            String url = (String)entry.getKey();
            Object handler = entry.getValue();

            if (!url.startsWith("/")) {
                url = "/" + url;
            }

            if ((handler instanceof String)) {
                handler = ((String)handler).trim();
            }
            registerHandler(url, handler);
        }
    }

    public int getOrder() {
        return 0;
    }
}