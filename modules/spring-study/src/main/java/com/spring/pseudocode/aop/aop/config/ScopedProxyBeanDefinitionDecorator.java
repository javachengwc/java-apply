package com.spring.pseudocode.aop.aop.config;

import com.spring.pseudocode.aop.aop.scope.ScopedProxyUtils;
import com.spring.pseudocode.beans.factory.config.BeanDefinitionHolder;
import com.spring.pseudocode.beans.factory.xml.BeanDefinitionDecorator;
import com.spring.pseudocode.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

//<aop:scoped-proxy/>的处理
public class ScopedProxyBeanDefinitionDecorator implements BeanDefinitionDecorator
{
    private static final String PROXY_TARGET_CLASS = "proxy-target-class";

    public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext)
    {
        boolean proxyTargetClass = true;
        if ((node instanceof Element)) {
            Element ele = (Element)node;
            if (ele.hasAttribute("proxy-target-class")) {
                proxyTargetClass = Boolean.valueOf(ele.getAttribute("proxy-target-class")).booleanValue();
            }

        }

        BeanDefinitionHolder holder = ScopedProxyUtils.createScopedProxy(definition, parserContext.getRegistry(), proxyTargetClass);
        String targetBeanName = ScopedProxyUtils.getTargetBeanName(definition.getBeanName());
        //parserContext.getReaderContext().fireComponentRegistered(new BeanComponentDefinition(definition.getBeanDefinition(), targetBeanName));
        return holder;
    }
}