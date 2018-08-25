package com.spring.pseudocode.beans.factory.xml;

import com.spring.pseudocode.beans.factory.config.BeanDefinition;
import com.spring.pseudocode.beans.factory.config.BeanDefinitionHolder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

//命名空间处理器
//spring为了开放性提供了NamespaceHandler机制，可以根据需求自定义标签元素。
public abstract interface NamespaceHandler
{
    public abstract void init();

    public abstract BeanDefinition parse(Element element, ParserContext parserContext);

    public abstract BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder beanDefinitionHolder, ParserContext parserContext);
}
