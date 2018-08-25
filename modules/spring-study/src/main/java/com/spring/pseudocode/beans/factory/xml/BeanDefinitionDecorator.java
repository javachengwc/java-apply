package com.spring.pseudocode.beans.factory.xml;

import com.spring.pseudocode.beans.factory.config.BeanDefinitionHolder;
import org.w3c.dom.Node;

//beanDefinition装饰器
public abstract interface BeanDefinitionDecorator
{
    public abstract BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder beanDefinitionHolder, ParserContext parserContext);
}
