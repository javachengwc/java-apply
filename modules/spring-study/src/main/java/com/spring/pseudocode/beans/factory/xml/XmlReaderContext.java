package com.spring.pseudocode.beans.factory.xml;

import com.spring.pseudocode.beans.factory.support.BeanDefinitionRegistry;
import com.spring.pseudocode.core.core.env.Environment;
import com.spring.pseudocode.core.core.io.Resource;
import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.beans.factory.parsing.ReaderEventListener;
import org.springframework.beans.factory.parsing.SourceExtractor;
import org.w3c.dom.Element;

public class XmlReaderContext {

    private final XmlBeanDefinitionReader reader;
    private final NamespaceHandlerResolver namespaceHandlerResolver;

    public XmlReaderContext(Resource resource, ProblemReporter problemReporter,
                            ReaderEventListener eventListener, SourceExtractor sourceExtractor,
                            XmlBeanDefinitionReader reader, NamespaceHandlerResolver namespaceHandlerResolver)
    {
        //super(resource, problemReporter, eventListener, sourceExtractor);
        this.reader = reader;
        this.namespaceHandlerResolver = namespaceHandlerResolver;
    }

    public final XmlBeanDefinitionReader getReader()
    {
        return this.reader;
    }

    public final BeanDefinitionRegistry getRegistry() {
        return this.reader.getRegistry();
    }

    public Object extractSource(Element ele) {
        return null;
    }

    public Environment getEnvironment() {
        return null;
    }

    public final NamespaceHandlerResolver getNamespaceHandlerResolver() {
        return this.namespaceHandlerResolver;
    }


    public void error(String message, Object source) {
        //...
    }

    public void error(String message, Object source, Throwable ex) {
        //...
    }

}
