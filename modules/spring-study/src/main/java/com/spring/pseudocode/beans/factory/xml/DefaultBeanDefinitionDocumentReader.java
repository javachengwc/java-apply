package com.spring.pseudocode.beans.factory.xml;

import com.spring.pseudocode.beans.factory.BeanDefinitionStoreException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DefaultBeanDefinitionDocumentReader implements BeanDefinitionDocumentReader
{
    public static final String BEAN_ELEMENT = "bean";
    public static final String NESTED_BEANS_ELEMENT = "beans";
    public static final String ALIAS_ELEMENT = "alias";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String ALIAS_ATTRIBUTE = "alias";
    public static final String IMPORT_ELEMENT = "import";
    public static final String RESOURCE_ATTRIBUTE = "resource";
    public static final String PROFILE_ATTRIBUTE = "profile";

    protected final Log logger = LogFactory.getLog(getClass());

    private XmlReaderContext readerContext;

    private BeanDefinitionParserDelegate delegate;

    public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext)
    {
        this.readerContext = readerContext;
        Element root = doc.getDocumentElement();
        doRegisterBeanDefinitions(root);
    }

    protected final XmlReaderContext getReaderContext()
    {
        return this.readerContext;
    }

    protected Object extractSource(Element ele)
    {
        return getReaderContext().extractSource(ele);
    }

    protected void doRegisterBeanDefinitions(Element root)
    {
        BeanDefinitionParserDelegate parent = this.delegate;
        //创建BeanDefinitionParserDelegate，真正xml中各种元素的解析
        this.delegate = createDelegate(getReaderContext(), root, parent);

        if (this.delegate.isDefaultNamespace(root)) {
            String profileSpec = root.getAttribute("profile");
            if (StringUtils.hasText(profileSpec)) {
                String[] specifiedProfiles = StringUtils.tokenizeToStringArray(profileSpec, ",; ");

                if (!getReaderContext().getEnvironment().acceptsProfiles(specifiedProfiles)) {
                    return;
                }
            }
        }

        preProcessXml(root);
        parseBeanDefinitions(root, this.delegate);
        postProcessXml(root);

        this.delegate = parent;
    }

    protected BeanDefinitionParserDelegate createDelegate(XmlReaderContext readerContext, Element root, BeanDefinitionParserDelegate parentDelegate)
    {
        BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate(readerContext);
        delegate.initDefaults(root, parentDelegate);
        return delegate;
    }

    //会对Node进行判断，如果是默认解析的spring标签，则会在DefaultBeanDefinitionDocumentReader中进行解析，
    // 否则就需要BeanDefinitionParserDelegate来委派找到解析处理器。
    protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate)
    {
        if (delegate.isDefaultNamespace(root)) {
            NodeList nl = root.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                if ((node instanceof Element)) {
                    Element ele = (Element)node;
                    if (delegate.isDefaultNamespace(ele)) {
                        parseDefaultElement(ele, delegate);
                    }
                    else
                        delegate.parseCustomElement(ele);
                }
            }
        }
        else
        {
            delegate.parseCustomElement(root);
        }
    }

    //对一些默认的标签import，alias,bean,beans标签进行处理
    private void parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate) {
        //解析import标签
        if (delegate.nodeNameEquals(ele, "import")) {
            importBeanDefinitionResource(ele);
        }
        else if (delegate.nodeNameEquals(ele, "alias")) {
            //解析alias标签
            processAliasRegistration(ele);
        }
        else if (delegate.nodeNameEquals(ele, "bean")) {
            //解析bean标签
            processBeanDefinition(ele, delegate);
        }
        else if (delegate.nodeNameEquals(ele, "beans"))
        {
            //解析beans标签，就是进行递归解析
            doRegisterBeanDefinitions(ele);
        }
    }

    protected void importBeanDefinitionResource(Element ele)
    {
        //......
    }

    protected void processAliasRegistration(Element ele)
    {
        String name = ele.getAttribute("name");
        String alias = ele.getAttribute("alias");
        boolean valid = true;
        if (!StringUtils.hasText(name)) {
            getReaderContext().error("Name must not be empty", ele);
            valid = false;
        }
        if (!StringUtils.hasText(alias)) {
            getReaderContext().error("Alias must not be empty", ele);
            valid = false;
        }
        if (valid) {
            try {
                getReaderContext().getRegistry().registerAlias(name, alias);
            }
            catch (Exception ex) {
                getReaderContext().error("Failed to register alias '" + alias + "' for bean with name '" + name + "'", ele, ex);
            }

            getReaderContext().fireAliasRegistered(name, alias, extractSource(ele));
        }
    }

    //对bean标签解析，最终还是在BeanDefinitionParserDelegate中对元素进行处理解析生成BeanDefinitionHolder，
    // bean元素的解析的结果是一个BeanDefinition对象，其包含了所有的bean的属性设置
    // processBeanDefinition中的BeanDefinitionReaderUtils的处理结果是将BeanDefinition注册到BeanFactory中
    protected void processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate)
    {
        //在BeanDefinitionParserDelegate中进行元素解析
        //BeanDefinitionHolder是对BeanDefinition的封装，包括BeanDefinition，beanName,aliases
        BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
        if (bdHolder != null) {
            bdHolder = delegate.decorateBeanDefinitionIfRequired(ele, bdHolder);
            try
            {
                //将bean的元数据BeanDifinition注册到BeanFactory中
                BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
            }
            catch (BeanDefinitionStoreException ex) {
                getReaderContext().error("Failed to register bean definition with name '" + bdHolder.getBeanName() + "'", ele, ex);
            }
            getReaderContext().fireComponentRegistered(new BeanComponentDefinition(bdHolder));
        }
    }

    protected void preProcessXml(Element root)
    {
    }

    protected void postProcessXml(Element root)
    {
    }
}
