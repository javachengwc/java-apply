package com.spring.pseudocode.beans.factory.xml;

import com.spring.pseudocode.beans.factory.config.BeanDefinition;
import com.spring.pseudocode.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.parsing.ComponentDefinition;

import java.util.Stack;

public final class ParserContext {

    private final XmlReaderContext readerContext;
    private final BeanDefinitionParserDelegate delegate;
    private BeanDefinition containingBeanDefinition;
    private final Stack<ComponentDefinition> containingComponents = new Stack();

    public ParserContext(XmlReaderContext readerContext, BeanDefinitionParserDelegate delegate)
    {
        this.readerContext = readerContext;
        this.delegate = delegate;
    }

    public ParserContext(XmlReaderContext readerContext, BeanDefinitionParserDelegate delegate, BeanDefinition containingBeanDefinition)
    {
        this.readerContext = readerContext;
        this.delegate = delegate;
        this.containingBeanDefinition = containingBeanDefinition;
    }

    public final XmlReaderContext getReaderContext()
    {
        return this.readerContext;
    }

    public final BeanDefinitionRegistry getRegistry() {
        return this.readerContext.getRegistry();
    }

    public final BeanDefinitionParserDelegate getDelegate() {
        return this.delegate;
    }

    public final BeanDefinition getContainingBeanDefinition() {
        return this.containingBeanDefinition;
    }

    public final boolean isNested() {
        return this.containingBeanDefinition != null;
    }

}
