package com.spring.pseudocode.context.ui;

import org.springframework.util.Assert;

import java.util.LinkedHashMap;

public class ModelMap extends LinkedHashMap<String, Object>
{
    public ModelMap()
    {
    }

    public ModelMap(String attributeName, Object attributeValue)
    {
        addAttribute(attributeName, attributeValue);
    }

    public ModelMap(Object attributeValue)
    {
        addAttribute(attributeValue);
    }

    public ModelMap addAttribute(String attributeName, Object attributeValue)
    {
        Assert.notNull(attributeName, "Model attribute name must not be null");
        put(attributeName, attributeValue);
        return this;
    }

    public ModelMap addAttribute(Object attributeValue)
    {
        //伪代码
        return null;
    }

}
