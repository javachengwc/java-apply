package com.spring.pseudocode.beans.factory.xml;

import com.spring.pseudocode.beans.factory.config.BeanDefinition;
import org.w3c.dom.Element;

public abstract interface BeanDefinitionParser
{
    public abstract BeanDefinition parse(Element element, ParserContext parserContext);
}
