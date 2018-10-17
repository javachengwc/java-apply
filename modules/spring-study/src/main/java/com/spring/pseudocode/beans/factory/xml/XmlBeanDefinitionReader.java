package com.spring.pseudocode.beans.factory.xml;

import com.spring.pseudocode.beans.factory.BeanDefinitionStoreException;
import com.spring.pseudocode.beans.factory.support.AbstractBeanDefinitionReader;
import com.spring.pseudocode.beans.factory.support.BeanDefinitionRegistry;
import com.spring.pseudocode.beans.factory.support.BeanNameGenerator;
import com.spring.pseudocode.core.core.io.Resource;

import com.spring.pseudocode.core.core.io.support.EncodedResource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


import java.io.IOException;
import java.io.InputStream;

//读取spring xml配置
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader
{

    private DocumentLoader documentLoader = new DefaultDocumentLoader();

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry)
    {
        super(registry);
    }

    public BeanDefinitionRegistry getRegistry() {
        return null;
    }

    public BeanNameGenerator getBeanNameGenerator() {
        return null;
    }

    //解析resource
    public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException
    {
        return loadBeanDefinitions(new EncodedResource(resource));
    }

    public int loadBeanDefinitions(EncodedResource encodedResource) throws BeanDefinitionStoreException
    {
        //....
        try
        {
            //从xml文件中获取流
            InputStream inputStream = encodedResource.getResource().getInputStream();
            try {
                InputSource inputSource = new InputSource(inputStream);
                if (encodedResource.getEncoding() != null) {
                    inputSource.setEncoding(encodedResource.getEncoding());
                }
                //解析文件流
                int i = doLoadBeanDefinitions(inputSource, encodedResource.getResource());
                //...
                return i;
            }
            finally {
                inputStream.close();
            }
        }
        catch (IOException ex)
        {
            throw new BeanDefinitionStoreException("IOException parsing XML document from " + encodedResource.getResource(), ex);
        }
        finally {
            //....
        }
    }

    //将文件流解析成Document对象
    protected int doLoadBeanDefinitions(InputSource inputSource, Resource resource) throws BeanDefinitionStoreException
    {
        try
        {
            Document doc = doLoadDocument(inputSource, resource);
            return registerBeanDefinitions(doc, resource);
        }
        catch (BeanDefinitionStoreException ex) {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new BeanDefinitionStoreException("xxx", ex);
        }
    }

    protected Document doLoadDocument(InputSource inputSource, Resource resource)
    {
//        return this.documentLoader.loadDocument(inputSource, getEntityResolver(), this.errorHandler,
//                getValidationModeForResource(resource),isNamespaceAware());
          return null;
    }

    public int registerBeanDefinitions(Document doc, Resource resource)  throws BeanDefinitionStoreException
    {
        //创建Document解析处理器
        BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
        int countBefore = getRegistry().getBeanDefinitionCount();
        //在BeanDefinitionDocumentReader中解析xml中配置的元素
        //documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
        return getRegistry().getBeanDefinitionCount() - countBefore;
    }
    protected int getValidationModeForResource(Resource resource)
    {
        //...
        return 0;
    }

    protected BeanDefinitionDocumentReader createBeanDefinitionDocumentReader()
    {
        //return (BeanDefinitionDocumentReader)BeanDefinitionDocumentReader.class.cast(BeanUtils.instantiateClass(this.documentReaderClass));
        return null;
    }
}