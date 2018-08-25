package com.spring.pseudocode.beans.factory.xml;

import com.spring.pseudocode.beans.factory.config.BeanDefinition;
import com.spring.pseudocode.beans.factory.config.BeanDefinitionHolder;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

public abstract class NamespaceHandlerSupport implements NamespaceHandler
{
    private final Map<String, BeanDefinitionParser> parsers = new HashMap();

    private final Map<String, BeanDefinitionDecorator> decorators = new HashMap();

    private final Map<String, BeanDefinitionDecorator> attributeDecorators = new HashMap();

    public BeanDefinition parse(Element element, ParserContext parserContext)
    {
        return findParserForElement(element, parserContext).parse(element, parserContext);
    }

    private BeanDefinitionParser findParserForElement(Element element, ParserContext parserContext)
    {
        String localName = parserContext.getDelegate().getLocalName(element);
        BeanDefinitionParser parser = (BeanDefinitionParser)this.parsers.get(localName);
        //...
        return parser;
    }

    public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext)
    {
        return findDecoratorForNode(node, parserContext).decorate(node, definition, parserContext);
    }

    private BeanDefinitionDecorator findDecoratorForNode(Node node, ParserContext parserContext)
    {
        BeanDefinitionDecorator decorator = null;
        String localName = parserContext.getDelegate().getLocalName(node);
        if ((node instanceof Element)) {
            decorator = (BeanDefinitionDecorator)this.decorators.get(localName);
        }
        else if ((node instanceof Attr)) {
            decorator = (BeanDefinitionDecorator)this.attributeDecorators.get(localName);
        }
        //...
        return decorator;
    }

    protected final void registerBeanDefinitionParser(String elementName, BeanDefinitionParser parser)
    {
        this.parsers.put(elementName, parser);
    }

    protected final void registerBeanDefinitionDecorator(String elementName, BeanDefinitionDecorator dec)
    {
        this.decorators.put(elementName, dec);
    }

    protected final void registerBeanDefinitionDecoratorForAttribute(String attrName, BeanDefinitionDecorator dec)
    {
        this.attributeDecorators.put(attrName, dec);
    }
}
