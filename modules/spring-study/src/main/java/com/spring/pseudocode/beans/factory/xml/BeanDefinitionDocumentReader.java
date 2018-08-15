package com.spring.pseudocode.beans.factory.xml;

import com.spring.pseudocode.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.w3c.dom.Document;

public abstract interface BeanDefinitionDocumentReader
{
    public abstract void registerBeanDefinitions(Document document, XmlReaderContext xmlReaderContext)
            throws BeanDefinitionStoreException;
}
